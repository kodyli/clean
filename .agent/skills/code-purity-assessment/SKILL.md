---
name: code-purity-assessment
description: Assess the purity and testability of domain code by identifying indicators of entangled I/O and non-deterministic behavior. Use this skill when reviewing core business logic, domain models, or functional services to ensure they follow the "Functional Core, Imperative Shell" pattern.
---

# Code Purity Assessment

This skill helps you evaluate whether a piece of core domain logic is "pure"—meaning it is deterministic, side-effect free, and easy to test—or "impure," which leads to fragile and hard-to-maintain code.

## How to Use

Review a section of core domain logic and evaluate it against the following criteria:

### Indicators of GOOD Design (Pure Code)

- **Explicit Inputs and Outputs**: The function takes all necessary data as parameters and returns a clear result. There are no "magic numbers" or hidden data sources.
- **Determinism**: Given the same inputs, the function always produces the exact same output. It does not rely on random numbers, the current system time, or other external state internally.
- **No Side Effects**: The function only performs its stated calculation or decision-making. It does not modify external state, save to databases, send notifications, or interact with any external systems.
- **Clear Decision Types**: Complex decisions are represented by specific data structures (like enums or dedicated result types) rather than implicit actions or side effects.
- **Easy to Test**: Unit tests can be written by simply providing inputs and asserting on the returned outputs, without needing sophisticated mocks or stubs.

### Indicators of BAD Design (Impure Code / Red Flags)

- **No Input and No Output**: Functions that take no parameters and return nothing (void/unit) often perform hidden I/O or have unclear responsibilities.
- **Output but No Input**: Functions that return a value but take no input often rely on hidden state or I/O (e.g., reading from a persistent store or system environment), making them non-deterministic.
- **Asynchronous Operations**: The presence of asynchronous keywords or patterns in core domain logic usually indicates hidden I/O.
- **External Exception Handling**: Catching exceptions related to external systems (e.g., connectivity errors, infrastructure failures) inside domain code indicates entangled I/O.
- **Exceptions for Control Flow**: Using exceptions to signal common business outcomes instead of returning specialized result types suggests an impure design.
- **Direct I/O Operations**: Look for direct calls to console output, database operations, or network requests embedded within the core business logic.
- **Implicit Persistence**: Reliance on frameworks that automatically persist changes or perform background saves within the domain logic can lead to hidden side effects and impurity.

## Goal

The ultimate goal is to identify areas that would benefit from refactoring using the **"Functional Core, Imperative Shell"** pattern, leading to more understandable, testable, and robust applications.
