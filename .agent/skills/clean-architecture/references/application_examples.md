# Application Layer Examples

## 1. Domain Object
```java
public class Customer {
    private final String id;
    private final String email;
    private final String name;
    private final CustomerType type;
}
```

## 2. Port Interfaces
```java
public record CreateCustomerPayload(@NotBlank String email,
                                    @NotBlank String name,
                                    @NotNull CustomerType type){}
// Repository Port
public interface SaveCustomerRepository extends Repository<CreateCustomerPayload, Customer> {}

// Client Port
public interface VerifyEmailClient extends Client<EmailPayload, EmailVerificationResult> {}

// Messenger Port
public interface NotifyCustomerMessenger extends Messenger<Customer, PublishResult> {}
```

## 3. Use Case Implementation (Interactor)
```java
public class DefaultCreateCustomerUseCase implements CreateCustomerUseCase {
    private final SaveCustomerRepository repository;
    private final VerifyEmailClient client;
    private final NotifyCustomerMessenger messenger;

    public DefaultCreateCustomerUseCase(SaveCustomerRepository repository,
            VerifyEmailClient client,
            NotifyCustomerMessenger messenger) {
        this.repository = repository;
        this.client = client;
        this.messenger = messenger;
    }

    @Override
    public UseCaseResponse<Customer> execute(UseCaseRequest<CreateCustomerPayload> request) {
        // 1. Validate Business Rules/Technical details via Client
        ClientRequest<EmailPayload> clientReq = new ClientRequest<>(new Actor("system"),
                new EmailPayload(request.payload().email()));
        ClientResponse<EmailVerificationResult> clientRes = client.send(clientReq);
        if (!clientRes.body().isValid()) {
            throw new InvalidCustomerException("Email is invalid");
        }

        // 2. Orchestrate Driven Sides
        RepositoryRequest<CreateCustomerPayload> repoReq = new RepositoryRequest<>(request.actor(),
                request.payload());
        RepositoryResponse<Customer> repoRes = repository.send(repoReq);

        // 3. Notify via Messenger
        MessengerRequest<Customer> msgReq = new MessengerRequest<>(repoRes.body());
        messenger.send(msgReq);

        // 4. Return Response
        return new UseCaseResponse<>(repoRes.body());
    }
}
```

## 4. Test Fakes (In-Memory/Console)
```java
// Repository Fake
public class InMemorySaveCustomerRepository implements SaveCustomerRepository {
    private final Map<String, Customer> customers = new HashMap<>();

    @Override
    public RepositoryResponse<Customer> send(RepositoryRequest<CreateCustomerPayload> request) {
        String id = UUID.randomUUID().toString();
        Customer customer = new Customer(id, request.payload().email(), request.payload().name(), request.payload().type());
        customers.put(id, customer);
        return new RepositoryResponse<>(customer);
    }
}

// Client Fake
public class LocalVerifyEmailClient implements VerifyEmailClient {
    private final Map<String, EmailVerificationResult> responses = new HashMap<>();

    public void addResponse(String email, EmailVerificationResult result) {
        responses.put(email, result);
    }

    @Override
    public ClientResponse<EmailVerificationResult> send(ClientRequest<EmailPayload> request) {
        EmailVerificationResult result = responses.getOrDefault(request.payload().email(),
                new EmailVerificationResult(true, "Auto-verified"));
        return new ClientResponse<>(result);
    }
}

// Messenger Fake
public class ConsoleNotifyCustomerMessenger implements NotifyCustomerMessenger {
    @Override
    public MessengerResponse<PublishResult> send(MessengerRequest<Customer> request) {
        System.out.println("Pushing notification for customer: " + request.payload().id());
        return new MessengerResponse<>(new PublishResult("msg-123"));
    }
}
```

## 5. Use Case Test
```java
class CreateCustomerUseCaseTest {
    private InMemorySaveCustomerRepository repository;
    private LocalVerifyEmailClient client;
    private ConsoleNotifyCustomerMessenger messenger;
    private DefaultCreateCustomerUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = new InMemorySaveCustomerRepository();
        client = new LocalVerifyEmailClient();
        messenger = new ConsoleNotifyCustomerMessenger();
        useCase = new DefaultCreateCustomerUseCase(repository, client, messenger);
    }

    @Test
    void shouldCreateCustomerSuccessfully() {
        // Arrange
        client.addResponse("test@example.com", new EmailVerificationResult(true, "OK"));
        CreateCustomerPayload payload = new CreateCustomerPayload("test@example.com", "John Doe", CustomerType.INDIVIDUAL);
        UseCaseRequest<CreateCustomerPayload> request = new UseCaseRequest<>(new Actor("admin"), payload);

        // Act
        UseCaseResponse<Customer> response = useCase.execute(request);

        // Assert
        assertNotNull(response.body().id());
        assertEquals("test@example.com", response.body().email());
    }
}
```
