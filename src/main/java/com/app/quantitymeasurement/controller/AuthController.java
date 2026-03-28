package com.app.quantitymeasurement.controller;

import lombok.extern.slf4j.Slf4j;

import com.app.quantitymeasurement.dto.request.AuthRequest;
import com.app.quantitymeasurement.dto.response.AuthResponse;
import com.app.quantitymeasurement.dto.request.RegisterRequest;
import com.app.quantitymeasurement.entity.User;
import com.app.quantitymeasurement.enums.AuthProvider;
import com.app.quantitymeasurement.enums.Role;
import com.app.quantitymeasurement.repository.UserRepository;
import com.app.quantitymeasurement.security.jwt.JwtTokenProvider;
import com.app.quantitymeasurement.security.UserPrincipal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


/**
 * AuthController
 *
 * REST controller exposing local authentication endpoints:
 * <ul>
 *   <li>{@code POST /api/v1/auth/register} — create a new LOCAL account and
 *       return a JWT immediately (no separate login step required).</li>
 *   <li>{@code POST /api/v1/auth/login}    — authenticate an existing LOCAL
 *       account and return a JWT.</li>
 *   <li>{@code GET  /api/v1/auth/me}       — return profile information for
 *       the currently authenticated user (works for both LOCAL and Google accounts).</li>
 * </ul>
 *
 * <p><b>Google OAuth2</b> authentication is <em>not</em> handled here; it is
 * managed entirely by Spring Security's built-in OAuth2 login filter chain.
 * The flow starts at {@code /oauth2/authorization/google} and ends with
 * {@code OAuth2AuthenticationSuccessHandler} redirecting the browser to the
 * frontend URL with a JWT in the query string.</p>
 *
 * <p><b>Security:</b> all three endpoints are public (no authentication
 * required) as declared in {@code SecurityConfig}. The {@code /me} endpoint
 * is authenticated via the JWT filter before the controller method is reached.</p>
 *
 * <p><b>Base URL:</b> {@code /api/v1/auth}</p>
 *
 * @author Abhishek Puri Goswami
 * @version 18.0
 * @since 18.0
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication",
     description = "Local registration, login, and profile endpoints. " +
                   "For Google OAuth2, navigate to /oauth2/authorization/google.")
public class AuthController {


    /*
     * -------------------------------------------------------------------------
     * Dependencies
     * -------------------------------------------------------------------------
     */

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /*
     * =========================================================================
     * Endpoints
     * =========================================================================
     */

    /**
     * Registers a new local user account.
     *
     * <p><b>Flow:</b></p>
     * <ol>
     *   <li>Validate the request body (Bean Validation via {@code @Valid}).</li>
     *   <li>Reject duplicate email addresses with {@code 409 Conflict}.</li>
     *   <li>BCrypt-hash the raw password before persisting.</li>
     *   <li>Save the new {@link User} entity with role {@link Role#USER}
     *       and provider {@link AuthProvider#LOCAL}.</li>
     *   <li>Authenticate the new user programmatically (so we can generate a JWT
     *       without requiring an extra login round-trip).</li>
     *   <li>Return {@code 201 Created} with an {@link AuthResponse} containing
     *       the signed JWT.</li>
     * </ol>
     *
     * <p><b>Example request:</b></p>
     * <pre>
     * POST /api/v1/auth/register
     * Content-Type: application/json
     *
     * { "email": "user@example.com", "password": "strongP@ss1", "name": "Jane Doe" }
     * </pre>
     *
     * @param registerRequest the registration payload; validated by Bean Validation
     * @return {@code 201 Created} with a JWT, or {@code 409 Conflict} if the
     *         email is already registered
     */
    @PostMapping("/register")
    @Operation(
        summary     = "Register a new local account",
        description = "Creates an account with email + BCrypt-hashed password. " +
                      "Returns a JWT immediately — no separate login required."
    )
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest registerRequest) {

        log.info("POST /api/v1/auth/register — email: " + registerRequest.getEmail());

        /*
         * Step 1 — duplicate-email guard.
         * Check before inserting to give a clean 409 instead of a database
         * constraint violation that would propagate as a 500 error.
         */
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .build();
        }

        /*
         * Step 2 — build and persist the new user.
         * The BCrypt hash is generated here; the raw password is never stored.
         */
        User newUser = User.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .name(registerRequest.getName())
                .provider(AuthProvider.LOCAL)
                .role(Role.USER)
                .build();

        userRepository.save(newUser);
        log.info("Registered new user: " + newUser.getEmail());

        /*
         * Step 3 — authenticate programmatically so we can generate a JWT.
         * authenticate() calls DaoAuthenticationProvider which:
         *   a) loads the user via CustomUserDetailsService
         *   b) verifies the password (which we just set, so this always succeeds)
         *   c) returns a fully-authenticated UsernamePasswordAuthenticationToken
         */
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        registerRequest.getEmail(),
                        registerRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        /*
         * Step 4 — generate the JWT and build the response.
         */
        String token = jwtTokenProvider.generateToken(authentication);

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        AuthResponse authResponse = AuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .email(principal.getEmail())
                .name(principal.getUser().getName())
                .role(principal.getUser().getRole().name())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
    }

    /**
     * Authenticates an existing local user.
     *
     * <p><b>Flow:</b></p>
     * <ol>
     *   <li>Validate the request body.</li>
     *   <li>Delegate to {@link AuthenticationManager#authenticate}; this calls
     *       {@code DaoAuthenticationProvider}, which loads the user by email and
     *       verifies the BCrypt password hash.</li>
     *   <li>If authentication succeeds, generate a JWT and return {@code 200 OK}.</li>
     *   <li>If authentication fails (wrong password or unknown email), Spring
     *       Security throws {@code BadCredentialsException} which propagates as
     *       {@code 401 Unauthorized} via {@code JwtAuthenticationEntryPoint}.</li>
     * </ol>
     *
     * <p><b>Note:</b> if the email belongs to a Google account (provider = GOOGLE),
     * the user has no stored password and the login will fail with 401.
     * They should use the Google OAuth2 flow instead.</p>
     *
     * @param authRequest the login payload containing email and password
     * @return {@code 200 OK} with a JWT on success
     */
    @PostMapping("/login")
    @Operation(
        summary     = "Log in with email and password",
        description = "Authenticates a LOCAL account and returns a signed JWT. " +
                      "Google accounts must use /oauth2/authorization/google."
    )
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody AuthRequest authRequest) {

        log.info("POST /api/v1/auth/login — email: " + authRequest.getEmail());

        try {
            /*
             * Attempt authentication. If this throws (wrong credentials, user not
             * found), Spring Security's exception translation layer converts it to
             * a 401 response via JwtAuthenticationEntryPoint.
             */
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getEmail(),
                            authRequest.getPassword()
                    )
            );
    
            SecurityContextHolder.getContext().setAuthentication(authentication);
    
            String token = jwtTokenProvider.generateToken(authentication);
    
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
    
            AuthResponse authResponse = AuthResponse.builder()
                    .accessToken(token)
                    .tokenType("Bearer")
                    .email(principal.getEmail())
                    .name(principal.getUser().getName())
                    .role(principal.getUser().getRole().name())
                    .build();
    
            return ResponseEntity.ok(authResponse);
        } catch (AuthenticationException ex) {

            log.warn("Login failed for email: "
                    + authRequest.getEmail());

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }
    }

    /**
     * Returns the profile of the currently authenticated user.
     *
     * <p>This endpoint works for both LOCAL and Google-authenticated users.
     * The {@link JwtAuthenticationFilter} has already validated the JWT and
     * populated the {@link SecurityContextHolder} before this method is called,
     * so the principal is always a {@link UserPrincipal}.</p>
     *
     * <p>The endpoint is secured at the URL level in {@code SecurityConfig}
     * via {@code .anyRequest().authenticated()}, so no JWT → 401 automatically.</p>
     *
     * @param authentication injected by Spring MVC from the SecurityContext;
     *                       always non-null here because of the URL-level security rule
     * @return {@code 200 OK} with the user's email, name, and role
     */
    @GetMapping("/me")
    @Operation(
        summary     = "Get current user profile",
        description = "Returns the profile of the authenticated user. " +
                      "Requires a valid Bearer token in the Authorization header."
    )
    public ResponseEntity<AuthResponse> getCurrentUser(Authentication authentication) {

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();

        AuthResponse profileResponse = AuthResponse.builder()
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .build();

        return ResponseEntity.ok(profileResponse);
    }
}
