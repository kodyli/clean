---
name: clean-architecture
description: Guide for implementing and maintaining project using clean projects to implement Clean Architecture. use it when creating new features.
---

# Clean Architecture: Developer Onboarding Guide

Welcome to the clean project! This guide will help you understand our Clean Architecture implementation and how to build new features using clean projects.

---

## 1. Project Foundation

The project is split into two primary modules to enforce a strict boundary between business logic and technical implementation.

### `application layer` (The Inner Circle)
Contains the core business rules, entities, and use cases.
> [!IMPORTANT]
> **Constraints for Application Layer:**
> - **Pure Java ONLY** (`java.*` packages).
> - **NO** Spring Framework or any other DI frameworks.
> - **NO** Persistence libraries (Hibernate, JPA).
> - **NO** IO/Web libraries (Servlets, REST clients).
> - **NO** Mocking frameworks (Mockito).

### `platform layer` (The Outer Circle)
Contains the infrastructure, frameworks, and technical details.
> [!NOTE]
> **Constraints for Platform Layer:**
> - All framework code (Spring Boot, etc.) lives here.
> - Third-party integrations (Databases, External APIs) live here.
> - Delivery mechanisms (Web Controllers) live here.
> - **Constraint**: Must NEVER contain business logic; only "thin" adaptation code.

---

## 2. Step-by-Step Implementation Guide

Follow this sequence when implementing a new feature like `CreateCustomer`.

### Step 1: Define Your Domain
Create records for your data and entities for your business objects.
- **Reference**: [Application Examples: Domain Objects](references/application_examples.md#1-domain-object)

### Step 2: Define Your Ports (Interfaces)
Define what the application needs from the outside world (DB, API).
- **Reference**: [Application Examples: Port Interfaces](references/application_examples.md#2-port-interfaces)

### Step 3: Write a Use Case Test (TDD)
Implement **Test Fakes** natively using in-memory structures or console logs.
- **Rules**: NO Mockito. Use `InMemory...`, `Local...`, or `Console...` names.
- **Reference**: [Application Examples: Use Case Test](references/application_examples.md#5-use-case-test)

### Step 4: Implement the Use Case
Implement the logic in a class named `Default[Feature]UseCase`.
- **Reference**: [Application Examples: Use Case Implementation](references/application_examples.md#3-use-case-implementation-interactor)

### Step 5: Implement Adapters
Implement the technical side of your ports using `RepositoryBase`, `ClientBase`, or `MessengerBase`.
- **Reference**: [Platform Examples: Adapter Implementations](references/platform_examples.md#1-adapter-implementations-template-method-pattern)

### Step 6: Wire the Delivery Mechanism
Expose your use case via a `@RestController` and a `@Transactional` `@Service`.
- **Reference**: [Platform Examples: Delivery Mechanism](references/platform_examples.md#2-delivery-mechanism-controller--service)

---

## 3. Testing Strategy

| Test Type | Target | Dependencies | Rule |
| :--- | :--- | :--- | :--- |
| **Unit Test** | Entities / Records | None | Isolation |
| **Use Case Test** | Interactors | **Test Fakes** | No Mocks |
| **Platform Test** | Adapters | External / Mocked API | Tech Verification |

> [!TIP]
> Always drive your `DefaultUseCase` implementation via a **Use Case Test** using fakes to ensure maximum speed and isolation.

---

## 4. Technical Reference: Generic Type Parameters

| Parameter | Meaning | Example |
| :--- | :--- | :--- |
| **TI** | Technology Input | `CustomerEntity`, `StripeRequest` |
| **TO** | Technology Output | `CustomerEntity`, `StripeResponse` |
| **UPayload** | Use Case Payload | `CreateCustomerPayload` |
| **UBody** | Use Case Body | `Customer` |

---

## 5. Rules & Best Practices

1. **Strict Layering**: `clean-application` must NEVER depend on `clean-platform`.
2. **Fail-Fast**: All data that enters the application must be validated.
3. **Thin Adapters**: Keep translation logic minimal. Don't hide business logic in `convertPayload`.
4. **Maintenance**: Always check the [Application Examples](references/application_examples.md) and [Platform Examples](references/platform_examples.md) for the latest patterns.

## 6. Project Structure Example
This project is a multi-module Maven application organized according to Clean Architecture principles.
### **1. High-Level Folder Structure**
```text
cdn/
├── cdn-core/                      # Shared logic and module aggregator
├── cdn-spring-api/                # Spring Boot REST API (Infrastructure)
├── cdn-spring-console/            # Console interface (Infrastructure)
└── use-cases/                     # Core Business Logic (Domain)
    ├── cdn-search-customer/       # Logic for searching customers
    └── cdn-verify-customer-deceased-info/ # Logic for decedent verification
```
### **2. Module Details**

#### **`use-cases/` (Domain & Application Layer)**
Each use case is implemented as an independent module to ensure strict separation of business logic from infrastructure.
- **`application/`**: Contains the "Pure" business logic:
  - **Use Case Implementations**: Core services (e.g., `DefaultVerifyDecedentUseCase`).
  - **Ports (Interfaces)**: Definitions for repositories or external clients (e.g., `DeceasedRecordClient`).
  - **Entities**: Domain models and value objects used within the use case.
- **`platform/`**: Contains "Local" infrastructure adapters, such as test fakes or stubs specifically for that use case.

#### **`cdn-spring-api/` (Infrastructure Layer)**
The primary web entry point, responsible for exposing use cases via REST.
- **Adapters**: REST Controllers mapping HTTP requests to Use Case executable calls.
- **Shared Infrastructure**: Site-wide implementations for Kafka messaging, JPA repositories, and global configurations.

#### **`cdn-spring-console/` (Console Interface)**
A CLI-based infrastructure layer for running logic via the terminal or batch processes.

#### **`cdn-core/` (Library Aggregator)**
Consolidates multiple use cases and common Clean utilities into a single dependency for higher-level modules.