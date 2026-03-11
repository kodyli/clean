# Phase 1: System-wide Analysis — Reference

Perform system-wide analysis once per feature set.

## Step 1: Read Business Requirements
- **Define system capabilities**.
- **Clarify ambiguous requirements with the user**.
- **Rationale**: You cannot model what you don't understand. Clarifying ambiguity now prevents logical errors in the domain model.

## Step 2: Domain Modeling
**Goal**: Build a glossary of object names for use case text.
- **Rationale**: A shared vocabulary is the "anchor" for all subsequent design phases. If the domain model is weak, the use case text will be ambiguous.

- **Identify Objects**: Locate real-world domain objects and their relationships (generalization/aggregation).
- **Draft Diagram**: Create a high-level class diagram using **PlantUML**. Use the `Domain Model` template in [uml-patterns.md](uml-patterns.md).
- **Create Glossary**: Define each object name clearly.
- **Inspect Grammatically**: Highlight nouns (potential classes/attributes) and verbs (potential operations/associations).

## Step 3: Refine Business Requirements
- **Rewrite requirements** using formal domain model terms.
- **Rationale**: Using the glossary ensures the requirements are precise and measurable.

## Step 4: Use Case Modeling
**Goal**: Describe system usage in the context of the domain model.
- **Rationale**: This defines the *behavior* of the system from the user's perspective, using the *objects* defined in the domain model.

## Step 4: Use Case Modeling
**Goal**: Describe system usage in the context of the domain model.

- **Identify Actors**: List all users or systems interacting with the feature.
- **Define Use Cases**: Ask "What does the actor want to do?" and list the answers.
- **Draw Diagram**: Create use case diagrams using **PlantUML**. Use the `Use Case Diagram` template in [uml-patterns.md](uml-patterns.md).
- **Organize**: Group use cases into packages if necessary.
- **Write Text**: Draft initial use case text using terms from the domain model.

---

## Tracking File
**Save to**: `.agent/iconix/{feature-name}/phase1-v{N}.md`

### Required YAML Front-matter
```yaml
---
phase: 1
use_case: ""
version: 1
status: draft   # draft | approved
updated: YYYY-MM-DD
---
```

## Phase Quality Checklist
Ensure the tracking file contains:
- [ ] Domain glossary (names and definitions).
- [ ] Refined requirements using domain terms.
- [ ] Full use case list (actor, name, summary).
- [ ] Use case diagram or structured table.
