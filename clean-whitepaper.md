# Structural Clean Architecture: Compiler-Enforced Isolation of Business Logic

**A Structural Approach to Business Isolation, Reuse, and Architectural Integrity**

---

## Abstract

Clean Architecture, popularized by Robert C. Martin, emphasizes the isolation of business rules from frameworks, databases, and delivery mechanisms. Many implementations follow the conceptual model but collapse all layers into a single project, relying on discipline or runtime tests to prevent architectural erosion.

This paper presents **Clean Project**, a multi-module Maven implementation that elevates use cases to **first-class architectural units**. Inspired by both Clean Architecture and Ivar Jacobson's use case–driven philosophy, the system enforces architectural constraints at compile time. Each use case is an independent module that separates pure business logic (`application`) from infrastructure adapters (`platform`) at the build level, making business rules independently compilable, publishable, and reusable.

The paper details the motivation, governing constraints, layer responsibilities, physical structure, testing strategy, and structural trade-offs. It also examines scalability considerations and a counter-architecture grounded in domain-centric modularization.

---

## 1. Introduction

Robert C. Martin's Clean Architecture (2012) synthesizes ideas from Hexagonal Architecture, Onion Architecture, and Ivar Jacobson's Boundary-Control-Entity (BCE) model into a unified set of principles. The architecture organizes software into concentric layers where source code dependencies point exclusively inward, isolating core business rules from frameworks, databases, and delivery mechanisms.

These principles are well understood. However, the gap between understanding and enforcement persists in practice. The majority of Clean Architecture implementations place all layers within a single build module, relying on naming conventions and developer discipline to maintain boundaries. Over time, annotations from Spring leak into use cases, JPA entities get returned from application logic, and framework types infiltrate domain classes. The architecture appears intact in diagrams while the Dependency Rule is quietly violated in code.

Clean Project addresses this gap through **structural enforcement**: physical module boundaries that the compiler validates on every build.

---

## 2. Motivation

### 2.1 The Reusability Problem

When business logic and infrastructure are packaged in the same module, the business layer cannot be extracted or reused independently. If the same business rules must serve a different runtime (console instead of REST), a different framework, or another organization's system, every reuse effort requires untangling infrastructure concerns. Framework dependencies propagate with the business code, creating a form of **"Dependency Hell"** that defeats a core goal of Clean Architecture: that business rules should be portable.

### 2.2 The Enforcement Problem

Single-module projects can use tools like ArchUnit to detect dependency violations at test time. These are valuable but insufficient—they catch violations *after* the code compiles, not *before*. A developer can write code that violates the Dependency Rule, see it compile, and only discover the problem when architecture tests run. Multi-module separation moves enforcement to the compiler itself, making violations impossible to introduce accidentally.

### 2.3 The Solution

Clean Project uses a Maven multi-module structure to make business logic **structurally reusable** and architectural violations **mechanically difficult**. Each use case is its own module, strictly separating `application` (pure business logic) from `platform` (adapter implementations). Delivery mechanisms live in separate runtime modules. Because modules are physically separated, the business layer can be published, shared, and reused independently.

---

## 3. Foundational Principles: Independence as a Design Goal

The architecture ensures the system is **Independent of Frameworks, UI, Databases, and any external systems**. Business rules can be tested without a web server, database, or UI. The UI can change from web to console without modifying business logic. The database can be swapped without affecting the core domain.

---

## 4. The Governing Rule: Unidirectional Dependency Constraint

The system is organized into concentric circles. The overriding rule is that **source code dependencies can only point inwards**.

### 4.1 Core Constraints

- **Mechanisms vs. Policies** — Outer circles (Mechanisms) represent *how* things are done; inner circles (Policies) represent *what* is being achieved.
- **No Compile-Time Dependency Inward to Outward** — Inner layers must not have any compile-time dependency on classes, variables, or types from outer layers.
- **Data Format Isolation** — No technical data formats (e.g., Spring or Hibernate objects) should ever leak into the inner circles.

### 4.2 Dependency Inversion via Ports and Adapters

This isolation is mechanically enforced using the Dependency Inversion Principle (DIP). Inner layers define abstract interfaces (Ports) in the `application` package. The `platform` package provides base adapter implementations that bridge those ports to concrete technologies, following the Ports and Adapters (Hexagonal) pattern.

### 4.3 Crossing Boundaries

When data crosses a layer boundary, it must take the form most convenient for the inner circle—never the outer. Use Cases accept and return plain Java records (Request/Response DTOs). JPA Entities, Spring objects, and HTTP-specific types must never leak into the `application` package. Adapters are responsible for converting between external formats and application-level data structures.

### 4.4 Transaction Boundary Ownership

Use Cases must never open or manage transactions. Transactional demarcation belongs to the delivery mechanism (e.g., a Service class in the Spring module), which wraps the Use Case invocation. This ensures business logic remains portable and reusable outside any specific framework.

---

## 5. Layer Responsibilities

While the project packages code by feature physically, logically the code is separated into four concentric rings.

### 5.1 Use Cases

Following Jacobson's philosophy, Use Cases are the system's reason for existing. Each Use Case represents a single user goal and contains the application-specific business rules needed to achieve it. Use Cases orchestrate Entities and coordinate with external systems through Ports (interfaces) to fulfill that goal. They are isolated from externalities but respond to changes in operation logic.

### 5.2 Entities (Use Case–Scoped Domain)

Uncle Bob defines Entities as encapsulating the most general business rules—the least likely to change. Clean Project adopts a use case–scoped approach: each feature module owns its own domain model.

An `Order` inside a place-order module is not a global domain abstraction—it is the domain model as understood by that use case. Different use cases may represent the same real-world concept differently, shaped by what that specific use case needs to accomplish. This eliminates coupling between features and keeps each module independently deployable and reusable.

### 5.3 Interface Adapters

Adapters convert data between the formats convenient for use cases and entities and those required by external systems. This layer contains MVC structures (Controllers, Presenters), persistence mapping, and handles database transactions.

### 5.4 Frameworks and Drivers

The outermost layer of glue code. This is where actual web frameworks and databases live. These components are kept at the periphery so they do the least harm to the core logic.

---

## 6. Physical Structure: Feature Folders

### 6.1 Screaming Architecture

Uncle Bob's *Screaming Architecture* principle states that a project's structure should declare its **business purpose**, not its technology stack. When a developer opens the project, they should immediately see `place-order`, `cancel-order`, `search-customer`—not `controllers`, `services`, `repositories`.

This aligns with Ivar Jacobson's **Use Case 2.0** principle of building systems in "slices," where each slice delivers distinct value. If use cases drive the architecture, the folder structure should reflect the use cases, not the technical layers.

### 6.2 Implications for Automated Reasoning

Feature Folders make the codebase highly amenable to automated reasoning and AI assistance. By co-locating entities, use cases, ports, and adapters within a single directory, the architecture ensures that the complete context of a business capability is structurally contiguous. This allows automated tools to analyze the business intent and technical implementation of a feature in a single pass, without the need to traverse scattered technical layers.

Furthermore, the strict separation between `application` and `platform` layers facilitates a risk-based division of labor between human developers and automated assistants. The `application` layer, containing critical business rules, remains the primary domain of human reasoning, with AI serving as an accelerant for pure logic implementation. Conversely, the `platform` layer, characterized by repetitive adapter patterns, is well-suited for high-automation generation, with humans focusing on verification of security and performance boundaries.

### 6.3 Structural Rules

1. **Modules are Features** — Each Maven module is a distinct business capability.
2. **Internal Layers** — Within each feature module, `application` contains Entities and Use Cases (pure business logic), while `platform` contains Interface Adapters and their implementations.
3. **Cross-Cutting Concerns** — Aggregator modules and delivery mechanisms are kept separate.

### 6.4 Reference Layout

```
myapp/
├── pom.xml
│
├── myapp-core/                 # Aggregates all use cases
│
├── myapp-place-order/          # Feature Module (one per use case)
│    └── order/create/
│         ├── application/      # Inner Circle: pure Java, no frameworks
│         │    ├── Order.java
│         │    ├── PlaceOrderUseCase.java
│         │    ├── DefaultPlaceOrderUseCase.java
│         │    └── repository/
│         │         └── OrderRepository.java
│         └── platform/         # Outer Circle: adapters and fakes
│              └── repository/
│                   ├── BaseOrderRepository.java
│                   └── InMemoryOrderRepository.java
│
├── myapp-cancel-order/         # Feature Module
│
├── myapp-spring-api/           # Delivery Mechanism: Web/REST, Depends on myapp-core module
│    ├── config/BeanConfig.java
│    └── order/place/platform/
│         ├── PlaceOrderController.java
│         ├── PlaceOrderService.java
│         └── repository/
│              └── JpaOrderRepository.java
│
├── myapp-spring-console/       # Delivery Mechanism: Console/Batch, Depends on myapp-core module
│
└── myapp-architecture-tests/   # ArchUnit enforcement
```

---

## 7. Extending the System

### 7.1 Adding a New Use Case

1. Define the domain — create Entities and Value Objects in the `application` package.
2. Define the ports — create interfaces for what the use case needs (Repository, Client, Messenger).
3. Implement the use case — write exactly one Use Case class in the `application` package.
4. Bridge the boundary — create abstract adapter classes in the `platform` package that implement the ports.
5. Add to the aggregator — register the new use case in the core module.

### 7.2 Implementing a Delivery Mechanism

1. Implement the adapters — inside the delivery mechanism module, provide concrete classes extending the abstract adapters.
2. Maintain encapsulation — keep classes package-private between layers.
3. Wire via framework — use Spring or the application framework for dependency injection only in this layer.

---

## 8. Testing Strategy

Testability is one of the five foundational principles of Clean Architecture. Each layer has its own testing approach.

| Test Type | Location | Dependencies | Purpose |
|:---|:---|:---|:---|
| Unit Tests | `application/` | None | Test Entities and Value Objects in isolation |
| Use Case Tests | `application/` | Test Fakes only | Drive interactors with in-memory adapters |
| Platform Tests | `platform/` | Real or mocked infrastructure | Verify adapter behavior |
| Architecture Tests | Architecture test module | ArchUnit | Enforce the Dependency Rule is never violated |

Use Case Tests use **Test Fakes**—local in-memory implementations named `InMemory...`, `Local...`, or `Console...`—rather than mocking frameworks like Mockito. This ensures maximum speed, isolation, and readability while verifying that the Use Case behaves correctly against realistic adapter behavior.

---

## 9. Trade-offs and Alternatives

No architecture is without cost. Elevating use cases to independent modules introduces trade-offs that teams must evaluate against their context.

### 9.1 Module Proliferation

A system with thirty use cases produces thirty feature modules plus aggregator and delivery modules. This increases the surface area of the build system and can feel heavy in small teams or early-stage projects. The cost is justified as the system grows and multiple teams contribute concurrently—but in a two-person prototype, a single-module approach with ArchUnit enforcement may be more pragmatic.

### 9.2 Entity Duplication

The use case–scoped entity model (Section 5.1) means that the same real-world concept—say, `Customer`—may be represented differently in multiple modules. This is a deliberate trade-off: it eliminates coupling between features but creates potential for drift if the same business concept evolves independently in separate modules. Teams must decide whether to accept bounded-context-style duplication or introduce a shared domain module for truly stable, cross-cutting concepts.

### 9.3 Build Complexity

Multi-module Maven projects require careful dependency management. Parent POM configuration, dependency convergence, and build ordering become non-trivial concerns at scale. CI pipelines must be structured to build and test modules in the correct order, and incremental build tools become valuable as module count increases.

### 9.4 Counter-Architecture: Domain-Centric Modularization

An alternative approach modularizes by **domain aggregate** rather than by use case. In this model, all use cases involving `Order` (place, cancel, search, amend) live in a single `myapp-order` module. This reduces module count, keeps related domain logic co-located, and simplifies cross-use-case operations.

The trade-off is reduced isolation: use cases within the same module can access each other's internals, and the module becomes a deployment unit larger than any single feature. Clean Project favors use case–level modules to maximize reusability and enforce the narrowest possible dependency boundary. Teams with fewer, tightly related use cases may find domain-centric modularization more appropriate.

### 9.5 When This Architecture May Not Fit

- **Rapid prototyping** — Module overhead slows iteration when requirements are unstable.
- **Monolithic deployments with no reuse intent** — If business logic will never serve multiple runtimes, the reusability benefit is unrealized.
- **Extremely small systems** — A single module with disciplined conventions and ArchUnit may suffice.

---

## 10. Conclusion

Clean Architecture's principles are widely taught but rarely enforced at the structural level. Most implementations rely on naming conventions, code reviews, and runtime test tools to maintain boundaries that the compiler cannot see. When those conventions slip, architectural erosion begins.

Clean Project demonstrates that multi-module separation transforms Clean Architecture from a conceptual model into a compiler-enforced structure. Business logic becomes independently compilable, testable, and deployable. Framework dependencies cannot leak inward because the build system forbids it. Each use case is a self-contained module that can be published, reused, and understood in isolation.

This approach is not without cost—module proliferation, entity duplication, and build complexity are real concerns that teams must weigh against their context. For systems that value long-term reusability, multi-team scalability, and structural integrity, the trade-off is worthwhile.

By combining Martin's structural rigor with Jacobson's use case–driven organization, Clean Project delivers an architecture that screams its business intent, enforces its own rules, and remains reusable across delivery mechanisms—from REST APIs to console applications to systems not yet imagined.

---

## References

1. Martin, R.C. (2012). *The Clean Architecture*. The Clean Code Blog. https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html
2. Martin, R.C. (2017). *Clean Architecture: A Craftsman's Guide to Software Structure and Design*. Prentice Hall.
3. Jacobson, I. (1992). *Object-Oriented Software Engineering: A Use-Case Driven Approach*. Addison-Wesley.
4. Jacobson, I., Spence, I., Kerr, B. (2011). *Use Case 2.0: The Guide to Succeeding with Use Cases*. Ivar Jacobson International.
5. Martin, R.C. (2011). *Screaming Architecture*. The Clean Code Blog. https://blog.cleancoder.com/uncle-bob/2011/09/30/Screaming-Architecture.html
6. Cockburn, A. (2005). *Hexagonal Architecture (Ports and Adapters)*. https://alistair.cockburn.us/hexagonal-architecture/
7. Hombergs, T. (2019). *Get Your Hands Dirty on Clean Architecture*. Packt Publishing.
