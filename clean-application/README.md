# Clean Application Layer

This project defines the **Core Application Layer** for a specific business feature. In this architecture, each application module encapsulates **exactly one use case**.

## Layer Overview

The Application Layer is the "Brain" of the system. It is isolated from all technical frameworks and drivers, depending only on core Java and chosen domain-agnostic utilities.

It contains:
- **The Use Case**: The orchestration logic for the specific feature.
- **Entities**: Enterprise-wide business rules used by this use case.
- **Port Interfaces**: Abstractions for "Driven Sides" (Database, Messaging, 3rd Party APIs).

### 1. The Use Case (Interactor)

Each module focuses on a single business goal. The Use Case orchestrates the flow of data, using entities to apply business rules and ports to interact with external systems.

**Implementation Pattern:** Dependencies (Repositories, Clients, Messengers) are injected via constructor.

```java
public class SpecificUseCase implements UseCase<RequestPayload, ResponseBody> {
    private final DataRepository repository;
    private final ExternalClient client;

    public SpecificUseCase(DataRepository repository, ExternalClient client) {
        this.repository = repository;
        this.client = client;
    }

    @Override
    public UseCaseResponse<ResponseBody> execute(UseCaseRequest<RequestPayload> request) {
        // 1. Business logic & Orchestration
        // 2. Call repository/client ports
        // 3. Return results
    }
}
```

### 2. Driven Side Ports (Interfaces)

These interfaces define the contracts for the external details the Use Case needs:
- **Repository**: Persistence and database operations.
- **Messenger**: Asynchronous event publishing and messaging.
- **Client**: Interactions with 3rd-party APIs, **AI/ML Services**, and external integrations.

### 3. Development & Testing Workflow

The Application Layer contains **no main class** or entry point. It is the "Brain" of the system and is driven entirely by automated tests that ensure business requirements are met in isolation.

- **Component Tests (Cucumber)**: These are the primary drivers of the Use Case. Behavior is defined in Gherkin features and executed via Cucumber to validate the feature from a business perspective.
- **Unit Tests (JUnit 5)**: Fine-grained testing of complex orchestration or entity logic.
- **Test Fakes**: All outbound ports (Driven Sides) must be implemented as **In-Memory Fakes** within the project's test source. Mocking frameworks (e.g., Mockito) are avoided for use case orchestration tests. Messengers or external calls that do not return business data should **print messages to the console** to provide visibility during test execution.

#### Example - Cucumber Feature

```gherkin
Feature: Business goal name
  Scenario: Successful execution
    Given some initial state
    When the use case is executed with valid data
    Then the result should reflect success
    And a message should be published
```

This approach ensures that the core business value is documented, tested, and protected from technical changes in the platform or infrastructure.
