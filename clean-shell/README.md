# Clean Platform Layer

This project defines the **Platform Layer**, which encompasses the **Interface Adapters** and **Frameworks & Drivers**. It serves as the bridge between the high-level business logic (Application) and the external world.

## Layer Overview

The Platform Layer contains the concrete implementations of how the system interacts with technologies, divided into two distinct responsibilities:

1. **Driving Side**: Triggers the Use Case execution (Entry Points).
2. **Driven Side**: Implements the Port interfaces defined by the Application.

---

### 1. Driving Side (The "Commanders")

The Driving Side components are responsible for accepting input from various sources, converting it into a `UseCaseRequest`, and executing the Use Case.

**Common Implementations:**
- **Web API**: Spring Boot Controllers (REST/SOAP).
- **CLI/Console**: Spring Console applications or command-line tools.
- **AI Services**: Autonomous agents or AI-driven triggers.
- **Testing**: Cucumber test cases that drive the Use Case for validation.
- **All you can imagine**: Any external trigger that starts a business process.

---

### 2. Driven Side (The "Executors")

The Driven Side components implement the contracts (Port Interfaces) required by the Use Case to interact with persistence, messaging, or external services.

**Common Implementations:**
- **Databases**: JPA/Hibernate, JDBC, NoSQL (MongoDB, Redis).
- **Messaging**: Kafka, RabbitMQ, AWS SQS/SNS.
- **External Integration**: 3rd-party SaaS APIs, AI/ML Service integrations.

---

## Adapter Design Pattern (Architecture)

All driven adapters are implemented using the **Adapter Design Pattern**. The base classes (`ClientBase`, `RepositoryBase`, `MessengerBase`) act as the **Adapters**, bridging the Application Layer's Ports to specific technical **Executors**.

**Common Base Classes:**
- **ClientBase**: For external API integrations.
- **RepositoryBase**: For database persistence.
- **MessengerBase**: For asynchronous event publishing.

**Key Components:**
- **Adapter**: Your concrete implementation extending the base class (e.g., `JpaSaveCustomerRepository`).
- **Executor**: The external technical component wrapped by the adapter (e.g., `RestTemplate`, `JpaRepository`, `KafkaTemplate`, or AI SDKs).

**Workflow:** The Adapter receives a domain request, converts it for the **Executor**, lets the **Executor** process the technical operation, and converts the result back to the domain.

### Example - Technology Adapter wrapping an Executor

```java
public class RestVerifyEmailClient
        extends ClientBase<EmailRequest, EmailResponse, EmailPayload, EmailVerificationResult>
        implements VerifyEmailClient {

    // The 'Executor' or 'Adaptee' in the Adapter Pattern
    private final RestTemplate restTemplate;

    public RestVerifyEmailClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    protected EmailRequest convertPayload(Actor actor, EmailPayload payload) {
        return new EmailRequest(payload.email());
    }

    @Override
    protected EmailResponse process(EmailRequest input) {
        // Delegating execution to the technical Executor
        return restTemplate.postForObject("/verify", input, EmailResponse.class);
    }

    @Override
    protected EmailVerificationResult convertToBody(EmailResponse output) {
        return new EmailVerificationResult(output.isValid(), output.getReason());
    }
}
```

## Development & Testing Workflow

Adapters are tested to ensure technical correctness and connectivity while maintaining the independence of the core logic.

- **Integration Tests**: Individual adapters are tested with real external systems (or Testcontainers) to verify translation logic.
- **Generic Type Parameters**: For strict type safety, adapters define:
    - **TI (Technology Input)**: Tech-specific request format.
    - **TO (Technology Output)**: Tech-specific response format.
    - **UPayload (Use Case Payload)**: Domain input.
    - **UBody (Use Case Body)**: Domain output.

This approach ensures that technical details are isolated, allowing the core business value to evolve independently of infrastructure changes.
