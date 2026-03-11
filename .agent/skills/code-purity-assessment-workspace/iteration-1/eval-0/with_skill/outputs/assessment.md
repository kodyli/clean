# Assessment: Pure Logic

## Summary
This code demonstrates **GOOD design (Pure Code)**.

## Evaluation
- **Explicit Inputs and Outputs**: The function clearly takes `price` and `rules` as parameters and returns a numerical result. There are no hidden dependencies.
- **Determinism**: The logic is entirely deterministic; the output depends solely on the inputs provided.
- **No Side Effects**: The function iterates and calculates a result without modifying any external state or interacting with I/O.
- **Easy to Test**: This function can be unit tested easily by providing various prices and rule sets and asserting on the return value.

## Conclusion
This logic follows the "Functional Core" principle and does not require refactoring for purity.
