# Quantity Measurement App – UC1 (Feet Equality)

### 📌 Overview

- This module checks whether two measurements given in feet are equal.
- It focuses on correct `object equality`, `safe floating-point comparison`, and clean OOP design.

---

## ⚙️ Use Case: UC1 – Feet Measurement Equality

- Accepts two numerical values in feet
- Compares them for equality
- Returns `true` if equal, otherwise false

---

## ⚙️ Key Implementation Points

- Uses a separate Feet class to represent a measurement
- Measurement value is `private` and `final` (immutable)
- `equals()` is overridden correctly
- `Double.compare()` is used instead of `==`
- Handles `null`, type mismatch, and same reference cases safely\#QunatityMeasurement

# Quantity Measurement App – UC2 (Inches Equality)

### 📌 Overview

- This module checks whether two measurements given in **inches** are equal.
- It extends UC1 by supporting equality checks for inches while following the same design principles.

### ⚙️ Use Case: UC2 – Inches Measurement Equality

- Accepts two numerical values in inches
- Compares them for equality
- Returns `true` if equal, otherwise false

### ⚙️ Key Implementation Points

- Uses a separate **Inches** class to represent a measurement
- Measurement value is `private` and `final` (immutable)
- `equals()` is overridden correctly
- `Double.compare()` is used instead of `==`
- Handles `null`, type mismatch, and same reference cases safely

---

# Quantity Measurement App – UC3 (Generic Quantity Length)

### 📌 Overview

- This module refactors Feet and Inches into a **single generic Length class**.
- It eliminates code duplication by applying the **DRY (Don’t Repeat Yourself) principle**.
- Supports equality comparison **across different units** (feet ↔ inches).


### ⚙️ Use Case: UC3 – Generic Quantity Length Equality

- Accepts two numerical values along with their respective unit types
- Converts different units to a **common base unit**
- Compares values for equality
- Returns `true` if equivalent, otherwise false


### ⚙️ Key Implementation Points

- Uses a **single Length class** to represent all length measurements
- Introduces a `LengthUnit` **enum** for supported units and conversion factors
- Eliminates separate Feet and Inches classes
- Conversion logic is centralised and reusable
- Measurement value and unit are **encapsulated**
- `equals()` is overridden for **cross-unit value-based equality**
- Uses safe floating-point comparison
- Handles:

  - `null` values
  - invalid units
  - same reference checks
  - type mismatch safely
  
---

# Quantity Measurement App – UC4 (Extended Unit Support)

### 📌 Overview
 
- This module extends the generic Length class introduced in UC3 by adding support for Yards and Centimeters.
- It demonstrates how a well-designed generic solution scales to new units without code duplication.
- Supports equality comparison across `feet ↔ inches ↔ yards ↔ centimeters`.

### ⚙️ Use Case: UC4 – Extended Quantity Length Equality

- Accepts two numerical values along with their respective unit types
- Supports additional units: `YARDS` and `CENTIMETERS`
- Converts different units to a common base unit
- Compares values for equality
- Returns `true` if equivalent, otherwise `false`

### ⚙️ Key Implementation Points

- Continues using the single generic Length class
- Extends the existing LengthUnit enum with:
- YARDS `(1 yard = 3 feet)`
- CENTIMETERS `(1 cm = 0.393701 inches)`
- No changes required in Length class logic
- Conversion logic remains centralised in the enum
- Measurement value and unit stay encapsulated
- `equals()` supports cross-unit comparisons seamlessly
- Uses safe `floating-point comparison`

---

# Quantity Measurement App – UC5 (Unit-to-Unit Conversion)

### 📌 Overview
- This module extends UC4 by adding `explicit unit-to-unit conversion support` to the Quantity Measurement App.
- Instead of only `checking equality`, the `Length API` now allows `converting a measurement` from one unit to another using centralised conversion factors.
- Supports conversion across `feet ↔ inches ↔ yards ↔ centimeters`.

### ⚙️ Use Case: UC5 – Unit-to-Unit Conversion (Same Measurement Type)

- Accepts a numerical value along with a `source unit` and a `target unit`
- Supports conversion between all supported length units
- Normalises values using a `common base unit`
- Converts the normalised value into the target unit
- Returns the converted numeric value

### ⚙️ Key Implementation Points

- Continues using the same immutable `Length` class
- Reuses the existing `LengthUnit` enum with predefined conversion factors
- Conversion logic is centralised and consistent
- Supports both:
    - Static conversion using raw values
    - Instance-level conversion using `convertTo()`
- Validation ensures:
    - Units are `non-null` and valid
    - Values are finite (not NaN or infinite)
- Conversion preserves mathematical accuracy within `floating-point` tolerance
- No mutation of existing objects; conversions return new values or instances

 ---

  # Quantity Measurement App – UC6 (Addition of Two Length Units)

### 📌 Overview

- This module enables addition operations between two length measurements.
- It supports adding lengths in the same or different units (within the length category) and returns the result in the unit of the first operand.
- For example, adding 1 foot and 12 inches yields 2 feet.

### ⚙️ Use Case: UC6 – Addition of Two Length Units (potentially different units)

- Accepts two numerical values with their respective units.
- Adds them and returns the sum in the unit of the first operand.

### ⚙️ Key Concepts Learned

- Addition of value objects with unit conversion.
- Immutability and safe handling of operands.
- Normalisation to a base unit for accurate arithmetic.
- Floating-point precision management.
- Commutativity and identity element behaviour.
- Robust validation for null or invalid inputs.

---

# Quantity Measurement App – UC7 (Addition with Target Unit Specification)

### 📌 Overview

- This module extends UC6 by allowing the caller to explicitly specify a target unit for addition results.
- Instead of defaulting to the first operand’s unit, the result can be returned in any supported unit.
-  Example: 1 foot + 12 inches with target unit YARDS ≈ 0.667 yards.

### ⚙️ Use Case: UC7 – Addition with Target Unit Specification

- Accepts two numerical values with their respective units and a target unit.
- Adds them and returns the sum in the explicitly specified target unit.

### ⚙️ Key Implementation Points (UC7 – Explicit Target Unit Addition)

- Uses the same immutable Length class and LengthUnit enum.
- Overloaded add() method:
   - UC6: add(A, B) → result in the first operand’s unit.
   - UC7: add(A, B, targetUnit) → result in explicitly specified unit.
- Private utility method handles conversion → addition → target unit conversion.
- Validation added: target unit must be non-null and valid.
- Preserves immutability, precision, and commutativity.
- Maintains backward compatibility with the UC6 addition.

---

# Quantity Measurement App – UC8 (Standalone LengthUnit Refactoring)

### 📌 Overview

- This module refactors the `LengthUnit enum` to a `standalone`, `top-level class` with full responsibility for unit conversions.
- QuantityLength is simplified to focus on value comparison and arithmetic, delegating all conversion logic to LengthUnit.
- The change improves cohesion, eliminates circular dependencies, and establishes a scalable pattern for `multiple measurement categories`.

### ⚙️ Use Case: UC8 – Refactoring Unit Enum to Standalone with Conversion Responsibility

- `LengthUnit` manages all conversion logic (to/from base unit).
- `QuantityLength` handles equality, addition, and arithmetic only.
- Supports all functionality from UC1–UC7 without modifying client code.

### ⚙️ Key Implementation Points

- LengthUnit handles all unit conversion logic.
- `QuantityLength` delegates conversions → focuses on comparisons/addition.
- Methods:
   - `convertToBaseUnit`(double value)
   - `convertFromBaseUnit`(double baseValue)
- Preserves immutability, precision, and commutativity.
- `Public API` unchanged → `backward compatibility`.
- Establishes scalable design pattern for other measurement categories.

---

# Quantity Measurement App - UC9 (Weight Measurement)

- Description: UC9 extends the Quantity Measurement App to support weight measurements (kilogram, gram, pound) alongside length. Weight operations mirror length operations: equality, conversion, and addition. Weight and length are separate, type-safe categories.

- Units & Conversion:
  - KILOGRAM (kg) – base unit
  - GRAM (g) – 1 kg = 1000 g
  - POUND (lb) – 1 lb ≈ 0.453592 kg

- Implementation:
  - WeightUnit enum handles all conversion logic.
  - QuantityWeight class handles equality and arithmetic, delegating conversions to WeightUnit.
  - Supports cross-unit equality and addition, explicit target unit, and immutable objects.
  - Round-trip conversions maintain precision using epsilon.
  - Weight vs. length comparisons are not allowed.

- Example:
  - Quantity(1.0, KILOGRAM).equals(Quantity(1000.0, GRAM)) → true
  - Quantity(2.20462, POUND).convertTo(KILOGRAM) → Quantity(~1.0, KILOGRAM)
  - Quantity(1.0, KILOGRAM).add(Quantity(1000.0, GRAM), GRAM) → Quantity(2000.0, GRAM)

 ---
 # Quantity Measurement App – UC10 (Generic Quantity Class with Unit Interface)

### 📌 Overview

- This module refactors the previous category-specific Quantity classes into a single, generic `Quantity<U>` class that works with any measurement category implementing the `IMeasurable` interface.
- It eliminates code duplication, simplifies demonstration methods, and ensures type-safe operations across multiple measurement categories like length and weight.

### ⚙️ Use Case: UC10 – Generic Quantity and Multi-Category Support

- Accepts two numerical values with their respective units
- Supports equality comparison, unit conversion, and addition
- Prevents invalid cross-category comparisons (e.g., length vs. weight)
- Returns a new `Quantity` object for conversion or addition; equality returns a boolean

### ⚙️ Key Implementation Points

- Uses a single generic class: `Quantity<U extends IMeasurable>`
- Holds private final fields: `value` and `unit` (immutable)
- `IMeasurable` interface standardises unit behaviour across categories
- Enums (`LengthUnit`, `WeightUnit`) implement `IMeasurable` and encapsulate conversion logic
- `equals()` compares base unit values using `Double.compare()` and validates unit types
- `convertTo(U targetUnit)` delegates to the unit’s conversion methods and returns new instance
- `add(Quantity<U> other)` and `add(Quantity<U> other, U targetUnit)` perform arithmetic safely
- `hashCode()` and `toString()` overridden for collections and readable output
- Type safety ensured at compile-time via generics; runtime unit class checks prevent cross-category errors
- Demonstration methods in `QuantityMeasurementApp` are generic and unified for all categories

---

## ⚙️ Use Case:  UC11 – Volume Measurement Equality, Conversion, and Addition

- Accepts numerical values with their respective volume units (LITRE, MILLILITRE, GALLON)
- Compares volumes for equality
- Converts between volume units
- Adds two volume quantities, optionally specifying a target unit

### ⚙️ Key Implementation Points

- `VolumeUnit` enum implements `IMeasurable` with LITRE as the base unit
- Conversion factors: MILLILITRE = 0.001 L, GALLON ≈ 3.78541 L
- Equality uses base unit comparison with epsilon tolerance
- Generic `Quantity<U>` handles conversion and addition without modification
- Maintains type safety: volume cannot be mixed with length or weight
- Objects are immutable; addition and conversion return new instances

---

## ⚙️ Use Case: UC12 – Quantity Subtraction & Division

- Subtract two quantities of the same measurement category
- Divide two quantities to obtain a dimensionless ratio
- Support `cross-unit` arithmetic (e.g., feet ↔ inches, litre ↔ millilitre)
- Prevent `cross-category` operations (e.g., length vs weight)

### ⚙️Key Implementation Points

 - Convert operands to base unit before arithmetic
- Validate:
    - Null operands
    - Same measurement category
    - Finite numeric values
    - Division by zero
- Implicit target unit → first operand’s unit
- Explicit target unit supported
- Results rounded to two decimal places (subtraction only)

---
  ## UC13: Centralized Arithmetic Operations Using Enum Strategy

- Description: UC14 refactors the Quantity Measurement App by introducing an ArithmeticOperation enum (ADD, SUBTRACT, DIVIDE) to centralize arithmetic behavior. All operations now delegate to a single private helper method, eliminating duplicate validation and conversion logic while preserving existing functionality.

- Implementation:
  - ArithmeticOperation enum handles operation-specific computation.
  - Private helper method performs validation, base unit conversion, enum dispatch, and result conversion.
  - Add and subtract results are rounded to two decimals.
  - Divide returns a dimensionless raw double value.
  - No changes required to existing unit enums (LengthUnit, WeightUnit, VolumeUnit).
  - Full backward compatibility with UC12 maintained.

- Example:
  - Quantity(10.0, FEET).add(Quantity(5.0, FEET)) → 15.00 FEET
  - Quantity(10.0, FEET).subtract(Quantity(5.0, FEET)) → 5.00 FEET
  - Quantity(10.0, FEET).divide(Quantity(5.0, FEET)) → 2.0
 
---

# Quantity Measurement App - UC14 (Temperature Measurement with Selective Arithmetic Support)

### 📌 Overview

- UC14 extends the Quantity Measurement Application to support temperature measurements while respecting real-world arithmetic constraints.
- Unlike length, weight, and volume, temperature does not support full arithmetic.
- This use case refactors the IMeasurable interface to make arithmetic optional, enabling temperature units to support only equality and conversion while rejecting unsupported operations with clear errors.

### ⚙️ Use Case: UC14 (Temperature Measurement)

- Introduces **temperature measurement support** with unit conversion and equality
- Restricts **unsupported arithmetic operations** on temperature with clear validation
- Refactors `IMeasurable` to allow **selective operation support** while keeping existing units unchanged

### ⚙️ UC14 – Key Implementation Points

* Introduced `TemperatureUnit` (Celsius, Fahrenheit, Kelvin) with non-linear conversion formulas.
* Refactored `IMeasurable` to add `default methods` for optional arithmetic support.
* Added **SupportsArithmetic** functional interface with lambda-based capability flags.
* Non-temperature units inherit default arithmetic support (**backwards compatible**).
* Temperature explicitly **disables arithmetic** (add, subtract, divide) via overrides.
* `Quantity` validates `operation support upfront` before performing arithmetic.
* Equality and conversion work uniformly via **base-unit normalisation**.
* Cross-category comparisons remain `prohibited and type-safe`.
* Unsupported operations fail fast with **clear UnsupportedOperationException** messages.
* All **UC1–UC13 tests pass unchanged**, ensuring non-breaking evolution.
---

### 📅 UC15: N-Tier Architecture Refactoring

- Description: UC15 restructures the Quantity Measurement App into a layered architecture by introducing Controller, Service, Repository, DTO, Model, and Entity layers. This separation improves maintainability, modularity, and testability while preserving all measurement logic implemented in previous use cases.

- Architecture:

  - **Controller** – Handles requests and delegates operations to the service layer.
  - **Service** – Contains business logic and coordinates conversions and operations.
  - **Repository** – Provides a cache-based storage layer.
  - **DTO / Model / Entity** – Used for structured data transfer and internal representation.

- Implementation:

  - Introduced `QuantityMeasurementController`, `QuantityMeasurementServiceImpl`, and `QuantityMeasurementCacheRepository`.
  - Added `QuantityDTO`, `QuantityModel`, and `QuantityMeasurementEntity`.
  - Service performs **DTO → Model → Quantity → Model → DTO** transformation.
  - Reuses the existing generic `Quantity` engine and unit enums from previous UCs.

- Example:

  - `QuantityDTO(10, FEET, LENGTH) + QuantityDTO(12, INCHES, LENGTH) → QuantityDTO(11, FEET, LENGTH)`
  - `QuantityDTO(100, CELSIUS, TEMPERATURE).equals(QuantityDTO(212, FAHRENHEIT, TEMPERATURE)) → true`
  - 
---

### 📅 UC16: Database Persistence Layer Integration

* **Description:**
  UC16 extends the N-Tier architecture by replacing the cache-based repository with a **database-backed persistence layer**. The application now stores and retrieves quantity measurements using JDBC and a connection pool. This improves scalability and enables persistent storage while maintaining the same layered architecture introduced in UC15.

* **Architecture:**

  * **Controller** – Handles incoming requests and forwards them to the service layer.
  * **Service** – Performs business logic, conversions, and arithmetic operations.
  * **Repository** – Provides **database-based storage** using JDBC instead of in-memory caching.
  * **Connection Pool** – Manages reusable database connections for efficient access.
  * **DTO / Model / Entity** – Continue to support structured data transfer and internal representation.

* **Implementation:**

  * Introduced `QuantityMeasurementDatabaseRepository` to replace the cache repository.
  * Implemented database operations using **JDBC (`Connection`, `PreparedStatement`, `ResultSet`)**.
  * Added `ConnectionPool` utility for managing database connections.
  * Repository stores measurement results in the **`quantity_measurement` table**.
  * Service layer continues performing **DTO → Model → Quantity → Model → DTO** transformations.
  * Existing **Controller and Service logic remain unchanged**, ensuring backward compatibility.

* **Example:**

  * `QuantityDTO(5, FEET, LENGTH) + QuantityDTO(24, INCHES, LENGTH) → QuantityDTO(7, FEET, LENGTH)`
  * Result is **stored in the database** with a unique key.
  * `find(key)` retrieves the stored measurement entity from the database.

---
