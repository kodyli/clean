# Platform Layer Examples

## 1. Adapter Implementations (Template Method Pattern)

### Repository Adapter
```java
public class JpaSaveCustomerRepository
        extends RepositoryBase<CustomerEntity, CustomerEntity, CreateCustomerPayload, Customer>
        implements SaveCustomerRepository {

    private final CustomerJpaRepository jpaRepository;

    @Override
    protected CustomerEntity convertPayload(Actor actor, CreateCustomerPayload payload) {
        CustomerEntity entity = new CustomerEntity();
        entity.setName(payload.name());
        entity.setEmail(payload.email());
        return entity;
    }

    @Override
    protected CustomerEntity process(CustomerEntity input) {
        return jpaRepository.save(input);
    }

    @Override
    protected Customer convertToBody(CustomerEntity output) {
        return new Customer(output.getId(), output.getEmail(), output.getName());
    }
}
```

### Client Adapter
```java
public class RestVerifyEmailClient
        extends ClientBase<EmailRequest, EmailResponse, EmailPayload, EmailVerificationResult>
        implements VerifyEmailClient {

    private final RestTemplate restTemplate;

    @Override
    protected EmailRequest convertPayload(Actor actor, EmailPayload payload) {
        return new EmailRequest(payload.email());
    }

    @Override
    protected EmailResponse process(EmailRequest input) {
        return restTemplate.postForObject("/verify", input, EmailResponse.class);
    }

    @Override
    protected EmailVerificationResult convertToBody(EmailResponse output) {
        return new EmailVerificationResult(output.isValid(), output.getReason());
    }
}
```

### Messenger Adapter
```java
public class KafkaNotifyCustomerMessenger
        extends MessengerBase<KafkaMessage, SendResult, Customer, PublishResult>
        implements NotifyCustomerMessenger {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    protected KafkaMessage convertPayload(Actor actor, Customer payload) {
        return new KafkaMessage("customer-notifications", payload.id(),
                objectMapper.writeValueAsString(payload));
    }

    @Override
    protected SendResult process(KafkaMessage input) {
        return kafkaTemplate.send(input.topic(), input.key(), input.value()).get();
    }

    @Override
    protected PublishResult convertToBody(SendResult output) {
        return new PublishResult(output.getRecordMetadata().offset());
    }
}
```

## 2. Delivery Mechanism (Controller & Service)

### Service Layer (Transactional Orchestration)
```java
@Service
public class CustomerService {
    private final CreateCustomerUseCase useCase;

    public CustomerService(VerifyEmailClient client, SaveCustomerRepository repository,
            NotifyCustomerMessenger messenger) {
        // Wiring the Use Case Implementation with concrete adapters
        this.useCase = new DefaultCreateCustomerUseCase(repository, client, messenger);
    }

    @Transactional
    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        // 1. Convert DTO to Domain Payload
        CreateCustomerPayload payload = new CreateCustomerPayload(request.email(), request.name());
        Actor actor = new Actor("system"); // Or from SecurityContext
        
        // 2. Prepare UseCaseRequest
        UseCaseRequest<CreateCustomerPayload> ucRequest = new UseCaseRequest<>(actor, payload);

        // 3. Execute Use Case
        UseCaseResponse<Customer> response = useCase.execute(ucRequest);

        // 4. Return DTO Response
        return new CustomerResponse(response.body().id(), response.body().email());
    }
}
```

### Web Controller (REST Interface)
```java
@RestController
@RequestMapping("/api/v1")
public class CreateCustomerController {
    private final CustomerService service;

    public CreateCustomerController(CustomerService service) {
        this.service = service;
    }

    @PostMapping("/customers")
    public CustomerResponse create(@RequestBody CreateCustomerRequest request) {
        return service.createCustomer(request);
    }
}
```
