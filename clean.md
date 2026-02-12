# Clean Project: From Architecture to Implementation

**Clean Project** is a reference implementation of Clean Architecture, designed to isolate core business value from technical volatility. It draws inspiration from Uncle Bob's (Robert C. Martin) structural principles and Ivar Jacobson's use case-driven philosophy.

> This document is a developer-facing guide to the Clean Project architecture. It explains the principles, structure, and workflow needed to understand and extend the system.

| Section | What You'll Learn |
|:---|:---|
| §1 Philosophy | Why this architecture exists and its intellectual roots |
| §2 The Rule | The one rule you must never break |
| §3 Layers | What each concentric ring does |
| §4 Feature Folders | How the code is physically organized |
| §5 Playbook | Step-by-step guide for adding features |
| §6 Testing | How to test at each layer |

---

## 1. The Philosophy: Purpose over Platforms

The core goal of this project is to ensure that the system is **Independent of Frameworks, UI, and Databases**. By placing the business logic at the center, we ensure the following **Key Principles**:

1.  **Independent of Frameworks**: The architecture does not depend on the existence of some library of feature-laden software. This allows you to use such frameworks as tools, rather than having to cram your system into their limited constraints.
2.  **Testable**: The business rules can be tested without the UI, Database, Web Server, or any other external element.
3.  **Independent of UI**: The UI can change easily, without changing the rest of the system. A Web UI could be replaced with a console UI, for example, without changing the business rules.
4.  **Independent of Database**: You can swap out Oracle or SQL Server, for Mongo, BigTable, CouchDB, or something else. Your business rules are not bound to the database.
5.  **Independent of External Agency**: In fact your business rules simply don't know anything at all about the outside world.

### Jacobson's Influence: Use Cases as the Organizing Principle

Ivar Jacobson's *Object-Oriented Software Engineering* (1992) introduced the **Boundary-Control-Entity (BCE)** model and the idea that use cases should drive the entire architecture—not just requirements. Uncle Bob explicitly cites BCE as one of the foundations for Clean Architecture.

In this project, Jacobson's influence is most visible in:
- **Feature Folders** (Section 4): Each top-level module IS a use case, making the system's intent visible at a glance.
- **One Use Case, One Module**: Mirrors Jacobson's Use Case 2.0 principle of "building the system in slices," where each slice delivers distinct value.
- **BCE Mapping**: Entities → Entity, Use Cases → Control, Interface Adapters → Boundary.

---

## 2. The Rule: Total Isolation

The system is organized into concentric circles. The overriding rule is: **Source code dependencies can only point inwards.**

### Core Constraints
- **Mechanisms vs. Policies**: Outer circles (Mechanisms) represent *how* things are done (Web, DB); inner circles (Policies) represent *what* is being achieved.
- **No Name Referencing**: Inner layers must not mention any names (classes, variables) from outer layers.
- **Data Format Isolation**: No technical data formats (e.g., Spring or Hibernate objects) should ever leak into the inner circles.

### Implementation: Dependency Inversion via Ports & Adapters
This isolation is mechanically enforced using the **Dependency Inversion Principle (DIP)**. Inner layers define abstract interfaces (Ports) in the `application` package. The `platform` package provides base adapter implementations that bridge those ports to concrete technologies—following the Ports & Adapters (Hexagonal) pattern.

### Crossing Boundaries: Data Transfer

When data crosses a layer boundary, it must take the form most convenient for the **inner** circle—never the outer. Concretely:

- Use Cases accept and return **plain Java records** (Request/Response DTOs).
- JPA Entities, Spring objects, and HTTP-specific types must NEVER leak into the `application` package.
- Adapters are responsible for converting between external formats and application-level data structures.

---

## 3. The Concentric Layers: Roles and Responsibilities

**These are the conceptual layers of the architecture, representing distinct roles and responsibilities.** While we package by feature physically (see Section 4), logically the code is separated into these rings.

### Entities
Entities encapsulate the **most general business rules**—those that would exist even if no particular application consumed them. They are the least likely to change. In this project, Entities are core domain objects (e.g., `Order`, `ID`) representing business concepts independent of any specific use case or delivery mechanism.

### Use Cases
Following Jacobson's philosophy, Use Cases are the system's **reason for existing**. Each Use Case represents a single user goal and contains the **application-specific business rules** needed to achieve it. Use Cases orchestrate Entities and coordinate with external systems through Ports (interfaces) to fulfill that goal. They are isolated from externalities but respond to changes in operation logic.

### Interface Adapters
Adapters convert data between the formats convenient for use cases/entities and those required by **external agencies**. This layer contains MVC structures (Controllers, Presenters), persistence mapping, and is responsible for **handling database transactions**.

### Frameworks and Drivers
The outermost layer of "Glue Code." This is where the actual Web Frameworks and Databases live. We keep them here so they do the least harm to the core logic.

---

## 4. The Physical Map: Feature Folders

### Why Feature Folders?

Uncle Bob's *Screaming Architecture* principle states that a project's structure should declare its **business purpose**, not its technology stack. When a developer opens the project, they should immediately see `place-order`, `cancel-order`, `search-customer`— not `controllers`, `services`, `repositories`.

Jacobson reinforces this: if Use Cases drive the architecture, the folder structure should reflect the use cases, not the technical layers.

Feature Folders also make the codebase **AI-friendly**: all context for a single business capability—entities, use cases, ports, and adapters—lives in one folder. This allows AI tools to load the complete context of a feature in a single pass and immediately understand the business intent without navigating across scattered technical layers.

The `application` / `platform` split also naturally **divides work by risk**. In the `application` layer, **humans lead and AI assists**—humans drive domain decisions, define business rules, and design use cases, while AI accelerates the implementation of pure, side-effect-free Java code. In the `platform` layer, **AI leads and humans review**—AI generates the repetitive adapter boilerplate, while humans verify the infrastructure boundary for security, performance, and correctness against real external systems.

The project structure is designed to "scream" its intent. We use **Feature Folders** (implementing the Package by Feature pattern). Top-level modules represent **Use Cases** (features), and the architectural layers (Application/Platform) are internal details of each feature.

### Structural Rules

1.  **Modules are Features**: Each Maven module is a distinct business capability (e.g., `myapp-place-order`, `myapp-cancel-order`).
2.  **Internal Layers**: Within each feature module, we strictly separate:
    -   `application`: Contains **Entities** and **Use Cases**. (Pure business logic).
    -   `platform`: Contains **Interface Adapters** and their **implementations** (Frameworks & Drivers).
3.  **Cross-Cutting Concerns**: Aggregator (`myapp-core`) and delivery mechanisms (`myapp-spring-api`) are kept separate.

### Project Layout

Naming follows the pattern: `[feature-module] -> [resource]/[action] -> application|platform`

![Application Design](./clean.png)

~~~
myapp/
├── pom.xml
│
├── myapp-core/             # Aggregates all use cases
│    └── pom.xml
│
├── myapp-place-order/      # ← FEATURE MODULE (one per use case)
│    ├── pom.xml
│    └── order/
│        └── create/
│             ├── platform/              # ← OUTER CIRCLE (adapters and fakes)
│             │     ├── repository/
│             │     │    ├── BaseOrderRepository.java
│             │     │    └── InMemoryOrderRepository.java
│             │     ├── client/
│             │     │    ├── BasePaymentClient.java
│             │     │    └── LocalPaymentClient.java
│             │     └── messaging/
│             │          ├── BaseOrderCreatedMessenger.java  
│             │          └── SystemOutOrderCreatedMessenger.java
│             └── application/           # ← INNER CIRCLE (pure Java, no frameworks)
│                   ├── Order.java
│                   ├── ID.java
│                   ├── PlaceOrderUseCase.java
│                   ├── DefaultPlaceOrderUseCase.java
│                   ├── repository/
│                   │    └── OrderRepository.java
│                   ├── client/
│                   │    └── PaymentClient.java
│                   └── messaging/
│                        └── OrderCreatedMessenger.java
│
├── myapp-cancel-order/
│    ├── pom.xml
│    └── order/
│         └── cancel/
│              ├── application/
│              └── platform/
│
├── myapp-spring-api/       # DELIVERY MECHANISM: Web/REST
│    ├── pom.xml
│    ├── config/
│    │    └── BeanConfig.java
│    ├── order/
│    │     ├── place/
│    │     │    └── platform/
│    │     │        ├── PlaceOrderController.java
│    │     │        ├── PlaceOrderService.java
│    │     │        ├── repository/
│    │     │        │    └── JpaOrderRepository.java
│    │     │        ├── client/
│    │     │        │    └── PaypalPaymentClient.java
│    │     │        └── messaging/
│    │     │             └── KafkaOrderCreatedMessenger.java
│    │     ├── cancel/
│    │     └── search/
│    └── customer/
│
├── myapp-spring-console/
│    ├── pom.xml
│    ├── order/
│    └── customer/
│
└── myapp-architecture-tests/
     ├── pom.xml
     └── ArchitectureTest.java
~~~

## 5. Developer Playbook: Extending the System

### How to Add a New Use Case
1.  **Define the Domain**: Create Entities/Value Objects in the `application` package.
2.  **Define the Ports**: Create interfaces for what the use case needs (Repository, Client, Messenger).
3.  **Implement the Use Case**: Write **exactly one** UseCase class in the `application` package.
4.  **Bridge the Boundary**: Create abstract adapter classes in the `platform` package that implement your ports.
5.  **Add to Aggregator**: Add the new use case to the `myapp-core` module.
6.  **Test**: See Section 6 for the full testing strategy.

### How to Implement a Runtime (Web/Console)
1.  **Implement the Adapters**: Inside your delivery mechanism module (e.g., `myapp-spring-api`), provide concrete classes extending the abstract adapters.
2.  **Maintain Encapsulation**: Keep classes package-private between layers.
3.  **Injection**: Wire these adapters using Spring/Application framework only in this layer.
4.  **Test**: See Section 6 for the full testing strategy.

---

## 6. Testing Strategy

Testability is one of the five foundational principles of Clean Architecture. Each layer has its own testing approach:

| Test Type | Location | Dependencies | Purpose |
|:---|:---|:---|:---|
| **Unit Tests** | `application/` | None | Test Entities and Value Objects in isolation |
| **Use Case Tests** | `application/` | Test Fakes only | Drive interactors with in-memory adapters |
| **Platform Tests** | `platform/` | Real/mocked infra | Verify adapter behavior (e.g., DB queries) |
| **Architecture Tests** | `myapp-architecture-tests/` | ArchUnit | Enforce the Dependency Rule is never violated |

> **No mocking frameworks** (e.g., Mockito) in Use Case Tests. Use **Test Fakes** (`InMemory...`, `Local...`, `Console...`) instead—local in-memory implementations that drive the Use Case and verify logic rapidly.
