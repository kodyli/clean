# Clean Project: From Architecture to Implementation

**Clean Project** is a reference implementation of Clean Architecture, designed to isolate core business value from technical volatility. It draws inspiration from Uncle Bob's (Robert C. Martin) structural principles and Ivar Jacobson's use case-driven philosophy.

---

## 1. The Philosophy: Purpose over Platforms

The core goal of this project is to ensure that the system is **Independent of Frameworks, UI, and Databases**. By placing the business logic at the center, we ensure the following **Key Principles**:

1.  **Independent of Frameworks**: The architecture does not depend on the existence of some library of feature-laden software. This allows you to use such frameworks as tools, rather than having to cram your system into their limited constraints.
2.  **Testable**: The business rules can be tested without the UI, Database, Web Server, or any other external element.
3.  **Independent of UI**: The UI can change easily, without changing the rest of the system. A Web UI could be replaced with a console UI, for example, without changing the business rules.
4.  **Independent of Database**: You can swap out Oracle or SQL Server, for Mongo, BigTable, CouchDB, or something else. Your business rules are not bound to the database.
5.  **Independent of External Agency**: In fact your business rules simply don’t know anything at all about the outside world.

---

## 2. The Rule: Total Isolation

The system is organized into concentric circles. The overriding rule is: **Source code dependencies can only point inwards.**

### Core Constraints
- **Mechanisms vs. Policies**: Outer circles (Mechanisms) represent *how* things are done (Web, DB); inner circles (Policies) represent *what* is being achieved.
- **No Name Referencing**: Inner layers must not mention any names (classes, variables) from outer layers.
- **Data Format Isolation**: No technical data formats (e.g., Spring or Hibernate objects) should ever leak into the inner circles.

### Implementation: The Bridge Design Pattern
This isolation is mechanically enforced using the **Bridge Design Pattern**. We define abstract adapters in the **Platform Layer** that bridge high-level application requirements with low-level technical implementations.

---

## 3. The Logical Layers: Main Components

These are the conceptual layers of the architecture, representing distinct roles and responsibilities. While we package by feature physically (see Section 4), logically the code is separated into these rings.

### Entities
Entities encapsulate **enterprise-wide business rules**. They are the least likely to change. In this project, they are our core domain objects (e.g., `Order`, `ID`).

### Use Cases
The "Brain" of the application. This layer contains **application-specific business rules**. Use cases orchestrate the flow of data to and from entities to achieve specific system goals. They are isolated from externalities but respond to changes in operation logic.

### Interface Adapters
Adapters convert data between the formats convenient for use cases/entities and those required by **external agencies**. This layer contains MVC structures (Controllers, Presenters), persistence mapping, and is responsible for **handling database transactions**.

### Frameworks and Drivers
The outermost layer of "Glue Code." This is where the actual Web Frameworks and Databases live. We keep them here so they do the least harm to the core logic.

---

## 4. The Physical Map: Feature Folders

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
├── myapp-place-order/      # FEATURE MODULE: Place Order
│    ├── pom.xml
│    └── order/
│        └── create/
│             ├── platform/
│             │     ├── repository/
│             │     │    ├── BaseOrderRepository.java
│             │     │    └── InMemoryOrderRepository.java
│             │     ├── client/
│             │     │    ├── BasePaymentClient.java
│             │     │    └── LocalPaymentClient.java
│             │     └── messaging/
│             │          ├── BaseOrderCreatedMessenger.java  
│             │          └── SystemOutOrderCreatedMessenger.java
│             └── application/
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
6.  **Use Case Tests**: Use **Test Fakes** (local in-memory implementations) instead of mocks to drive the Use Case and verify logic rapidly.
7.  **Unit Tests**: Test Entities in isolation.
8.  **Architecture Tests**: Use ArchUnit to ensure the Dependency Rule is never violated.

### How to Implement a Runtime (Web/Console)
1.  **Implement the Adapters**: Inside your delivery mechanism module (e.g., `myapp-spring-api`), provide concrete classes extending the abstract adapters.
2.  **Maintain Encapsulation**: Keep classes package-private between layers.
3.  **Injection**: Wire these adapters using Spring/Application framework only in this layer.
4. **Platform Tests**: Verify tech-specific adapter behavior (e.g., DB queries).
5.  **Architecture Tests**: Use ArchUnit to ensure the Dependency Rule is never violated.
