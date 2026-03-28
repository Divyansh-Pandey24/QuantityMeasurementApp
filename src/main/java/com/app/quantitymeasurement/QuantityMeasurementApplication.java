package com.app.quantitymeasurement;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * QuantityMeasurementApplication
 *
 * Entry point for the Quantity Measurement Spring Boot application.
 *
 * {@code @SpringBootApplication} combines three annotations:
 * <ul>
 *   <li>{@code @Configuration} — marks this class as a source of bean definitions.</li>
 *   <li>{@code @EnableAutoConfiguration} — activates Spring Boot's auto-configuration
 *       based on classpath dependencies (H2, JPA, Tomcat, Jackson, Security).</li>
 *   <li>{@code @ComponentScan} — scans the package tree for {@code @Component},
 *       {@code @Service}, {@code @Repository}, and {@code @Controller} beans.</li>
 * </ul>
 *
 * {@code @OpenAPIDefinition} supplies the Swagger / OpenAPI metadata displayed at
 * {@code http://localhost:8080/swagger-ui.html} after the application starts.
 *
 * On startup, the embedded Tomcat server starts on port 8080, the H2 in-memory
 * database is initialised, and Spring Data JPA creates the schema automatically
 * from the entity annotations.
 *
 * @author Abhishek Puri Goswami
 * @version 17.0
 * @since 17.0
 */
@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title       = "Quantity Measurement API",
        version     = "17.0",
        description = "REST API for quantity measurement operations — comparison, conversion, " +
                      "addition, subtraction, and division across Length, Weight, Volume, " +
                      "and Temperature units."
    )
)
public class QuantityMeasurementApplication {

    /**
     * Bootstraps the Spring Boot application.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(QuantityMeasurementApplication.class, args);
    }
}
