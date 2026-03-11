---
name: iconix-process
description: Trigger this skill for any project initiation, requirements gathering, or software modeling tasks using the ICONIX process. Enforces a strict phased approach (Analysis -> Preliminary Design -> Detailed Design -> Implementation) with PlantUML modeling and persistent session tracking. Prevents premature coding in early phases.
---

# ICONIX Process (Iterative Workflow)

Use the ICONIX Process to bridge the gap between requirements and code through a "lean" modeling approach. By following this structure, you ensure that implementation is always grounded in verified design.

## 1. Collaborative Roadmap

| Phase | Activity | Rationale | Reference |
| :--- | :--- | :--- | :--- |
| **Phase 0** | **Initiation** | Understand the *purpose* before the *process*. | [phase0-initiation.md](references/phase0-initiation.md) |
| **Phase 1** | **Analysis** | Build a shared glossary of domain terms. | [phase1-analysis.md](references/phase1-analysis.md) |
| **Phases 2-4** | **Iterative Design** | Solve design problems on paper (UML) before coding. | [P2](references/phase2-preliminary-design.md), [P3](references/phase3-detailed-design.md), [P4](references/phase4-implementation.md) |

## 2. Session Management

Persist all findings in `.agent/iconix/{feature-name}/` to allow seamless recovery across sessions:
- `phase0-plan-v{N}.md` (Initiation)
- `phase1-v{N}.md` (Analysis)
- `phase{2-4}-{use-case}-v{N}.md` (Iterative Design/Implementation)

---

## Mandatory Rules

- **Silent Session Recovery** — Inform the user **once** at the start of Phases about the tracking file. Perform subsequent saves silently to minimize noise in the console.
- **Knowledge-First Discovery** — In Phase 0, perform mandatory domain research using `search_web` BEFORE interviewing the user. Use research findings to ground your discovery questions in industry standards and common pain points.
- **Exhaustive Purpose Discovery** — Conduct at least **3-5 interaction rounds** to resolve the high-level **main purpose**. You MUST present a **Discovery Synthesis** for confirmation before drafting the formal Project Description.
- **Sign-off Threshold** — Do not move to Phase 1 until the user explicitly approves both the **Draft Description** and the **High-level Requirements**.
- **Micro-Interaction Style** — In Phase 0, ask clarifying questions **one by one**. This mimics a human consultant and prevents overwhelming the user with massive checklists.
- **Requirement/Logic Boundary** — Reserve Phase 0 for **business requirements only**. Do not introduce actors or objects until the vocabulary is stable in Phase 1.
- **Strict Implementation Wall** — Do not write code or project files until **Phase 4**. Early coding often leads to wasted effort on unverified designs.
- **Action-Oriented Verbs** — Use imperative verbs (e.g., "Draft", "Update") in all checklists. This provides a clear "command" for the model to execute.
- **Versioning Over History** — Never overwrite a tracking file. Increment `v{N+1}` for every change to maintain an audit trail of design decisions.
- **UML-First Design** — Create all diagrams in ` ```plantuml ` using the verified templates in [uml-patterns.md](references/uml-patterns.md).
