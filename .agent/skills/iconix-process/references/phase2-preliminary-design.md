# Phase 2: Preliminary Design — Reference

## Step 1: Write Use Case Text
- Use the use case name as the title.
- Draft the use case body using formal domain model terms.

## Step 2: Robustness Analysis
**Goal**: Discover missing objects and operations; disambiguate the use case text.
- **Rationale**: Robustness analysis bridges the gap between the "What" (requirements) and "How" (design). It helps identify missing entities and operations early.

- **Identify Roles**: For each use case, identify:
  - **Boundary** object(s) — UI/system boundary interactions.
  - **Controller** object(s) — business logic coordination.
  - **Entity** object(s) — domain data holders.
- **Draw Diagram**: Create the robustness diagram using **PlantUML**. Use the `Robustness Diagram (BCE)` template in [uml-patterns.md](uml-patterns.md).
- **Update Model**: Update the domain model class diagram with newly discovered objects and attributes.
- **Disambiguate Text**: Refine the use case text to match the robustness diagram.
- **Finalize Analysis**: Finish updating the class diagram to reflect completion of the analysis phase.

---

## Tracking File
**Save to**: `.agent/iconix/{feature-name}/phase2-{use-case}-v{N}.md`

### Required YAML Front-matter
```yaml
---
phase: 2
use_case: "kebab-case-use-case-name"
version: 1
status: draft   # draft | approved
updated: YYYY-MM-DD
---
```

## Phase Quality Checklist
Ensure the tracking file contains:
- [ ] Full use case text (using domain model terms).
- [ ] Robustness diagram (BCE objects identified).
- [ ] Updated domain model (with new objects/attributes).
- [ ] Disambiguated text matching the diagram.
- [ ] All alternate courses covered.
