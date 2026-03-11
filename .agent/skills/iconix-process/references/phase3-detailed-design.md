# Phase 3: Detailed Design — Reference

## Step 1: Behavior Allocation (Sequence Diagrams)
**Goal**: Identify messages between objects and the methods to invoke.
- **Rationale**: Sequence diagrams ensure that the static model can actually support the required dynamic behavior.

- **Identify Messages**: Define messages passed between objects and their associated methods.
- **Draw Sequence Diagram**: Create a sequence diagram using **PlantUML**. Use the `Sequence Diagram` template in [uml-patterns.md](uml-patterns.md).
  - Place use case text on the **left** as `note left` blocks.
  - Show design decisions on the **right**.
- **Refine Model**: Update class diagrams with discovered attributes and operations.

## Step 2: Static Modeling (Class Diagram)
**Goal**: Produce a complete, implementation-ready static model.
- **Rationale**: This is the "blueprint" for the code. Completing this now makes the implementation in Phase 4 trivial and reduces bugs.

- **Draw Detailed Diagram**: Create the full class diagram using **PlantUML**. Use the `Class Diagram (Detailed Design)` template in [uml-patterns.md](uml-patterns.md).
  - Include visibility (`+`, `-`, `#`), types, and multiplicity.
- **Verify Design**: Ensure the design satisfies all use case requirements.

---

## Tracking File
**Save to**: `.agent/iconix/{feature-name}/phase3-{use-case}-v{N}.md`

### Required YAML Front-matter
```yaml
---
phase: 3
use_case: "kebab-case-use-case-name"
version: 1
status: draft   # draft | approved
updated: YYYY-MM-DD
---
```

## Phase Quality Checklist
Ensure the tracking file contains:
- [ ] Sequence diagram(s) (text on left, design on right).
- [ ] Named objects and methods.
- [ ] Full class diagram (visibility, types, multiplicity).
- [ ] Design verification against requirements.
