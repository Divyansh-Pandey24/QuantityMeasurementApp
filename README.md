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

