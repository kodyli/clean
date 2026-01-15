/**
 * Platform adapters that bridge the application layer with external systems.
 *
 * <p>
 * This package provides abstract base classes that implement the <b>Adapter Pattern</b>,
 * serving as the infrastructure layer in Clean Architecture. These adapters translate
 * between domain-centric application interfaces and technology-specific external
 * implementations.
 *
 * <h2>Architecture Overview</h2>
 *
 * <p>
 * In Clean Architecture, the application layer defines <i>what</i> needs to be done
 * (business logic), while the platform layer defines <i>how</i> it's done (technical
 * implementation). This separation ensures:
 *
 * <ul>
 * <li><b>Technology Independence:</b> Business logic doesn't depend on frameworks,
 * databases, or external APIs
 * <li><b>Testability:</b> Use cases can be tested without external dependencies
 * <li><b>Flexibility:</b> External systems can be swapped without changing business logic
 * <li><b>Maintainability:</b> Changes to infrastructure don't affect core business rules
 * </ul>
 *
 * <h2>Available Adapters</h2>
 *
 * <p>
 * This package provides four abstract adapter base classes:
 *
 * <h3>1. {@link ClientBase} - External API Integration</h3>
 *
 * <p>
 * Implements {@link li.yansan.clean.application.client.Client Client} to integrate with
 * external APIs (REST, SOAP, gRPC, third-party SDKs).
 *
 * <p>
 * <b>Use Cases:</b>
 * <ul>
 * <li>Payment gateways (Stripe, PayPal, Square)
 * <li>Email providers (SendGrid, Mailgun, AWS SES)
 * <li>SMS services (Twilio, AWS SNS)
 * <li>Geocoding APIs (Google Maps, Mapbox)
 * <li>Authentication providers (Auth0, Okta)
 * </ul>
 *
 * <p>
 * <b>Example:</b> <pre>{@code
 * public class StripePaymentClient
 *     extends ClientBase<StripeRequest, StripeResponse,
 *                                    PaymentPayload, PaymentResult> {
 *
 *   private final StripeClient stripeClient;
 *
 *   &#64;Override
 *   protected StripeRequest convertPayload(Actor actor, PaymentPayload payload) {
 *     return new StripeRequest(payload.amount(), payload.currency());
 *   }
 *
 *   &#64;Override
 *   protected StripeResponse process(StripeRequest input) {
 *     return stripeClient.charge(input); // External API call
 *   }
 *
 *   &#64;Override
 *   protected PaymentResult convertToBody(StripeResponse output) {
 *     return new PaymentResult(output.getId(), output.getStatus());
 *   }
 * }
 * }</pre>
 *
 * <h3>2. {@link RepositoryBase} - Database Persistence</h3>
 *
 * <p>
 * Implements {@link li.yansan.clean.application.repository.Repository Repository} to
 * persist and retrieve domain entities from databases.
 *
 * <p>
 * <b>Use Cases:</b>
 * <ul>
 * <li>JPA/Hibernate repositories
 * <li>JDBC data access
 * <li>MongoDB repositories
 * <li>Redis caching
 * <li>Elasticsearch indexing
 * </ul>
 *
 * <p>
 * <b>Example:</b> <pre>{@code
 * public class JpaCustomerRepository
 *     extends RepositoryBase<CustomerEntity, CustomerEntity,
 *                                        CustomerData, Customer> {
 *
 *   private final CustomerJpaRepository jpaRepository;
 *
 *   &#64;Override
 *   protected CustomerEntity convertPayload(Actor actor, CustomerData payload) {
 *     CustomerEntity entity = new CustomerEntity();
 *     entity.setName(payload.name());
 *     entity.setEmail(payload.email());
 *     entity.setCreatedBy(actor.id());
 *     return entity;
 *   }
 *
 *   &#64;Override
 *   protected CustomerEntity process(CustomerEntity input) {
 *     return jpaRepository.save(input); // JPA persistence
 *   }
 *
 *   &#64;Override
 *   protected Customer convertToBody(CustomerEntity output) {
 *     return new Customer(output.getId(), output.getName(), output.getEmail());
 *   }
 * }
 * }</pre>
 *
 * <h3>3. {@link MessengerBase} - Asynchronous Messaging</h3>
 *
 * <p>
 * Implements {@link li.yansan.clean.application.messaging.Messenger Messenger} to publish
 * events and messages to messaging systems.
 *
 * <p>
 * <b>Use Cases:</b>
 * <ul>
 * <li>Kafka event publishing
 * <li>RabbitMQ message queues
 * <li>AWS SQS/SNS
 * <li>Azure Service Bus
 * <li>Email notifications
 * </ul>
 *
 * <p>
 * <b>Example:</b> <pre>{@code
 * public class KafkaEventPublisher
 *     extends MessengerBase<KafkaMessage, SendResult,
 *                                       OrderEvent, PublishResult> {
 *
 *   private final KafkaTemplate<String, String> kafkaTemplate;
 *
 *   &#64;Override
 *   protected KafkaMessage convertPayload(Actor actor, OrderEvent payload) {
 *     return new KafkaMessage("order-events", payload.orderId(),
 *                             objectMapper.writeValueAsString(payload));
 *   }
 *
 *   &#64;Override
 *   protected SendResult process(KafkaMessage input) {
 *     return kafkaTemplate.send(input.topic(), input.key(), input.value()).get();
 *   }
 *
 *   &#64;Override
 *   protected PublishResult convertToBody(SendResult output) {
 *     return new PublishResult(output.getRecordMetadata().offset());
 *   }
 * }
 * }</pre>
 *
 * <h3>4. {@link UseCaseBase} - Delivery Mechanism</h3>
 *
 * <p>
 * Bridges delivery mechanisms (REST controllers, GraphQL resolvers, CLI handlers) with
 * use cases from the application layer.
 *
 * <p>
 * <b>Use Cases:</b>
 * <ul>
 * <li>REST controllers (Spring MVC, JAX-RS)
 * <li>GraphQL resolvers
 * <li>gRPC services
 * <li>Command-line interfaces
 * <li>WebSocket handlers
 * </ul>
 *
 * <p>
 * <b>Example:</b> <pre>{@code
 * &#64;RestController
 * &#64;RequestMapping("/api/customers")
 * public class CreateCustomerController
 *     extends UseCaseBase<CreateCustomerRequest, CustomerResponse,
 *                                     CustomerPayload, Customer> {
 *
 *   private final CreateCustomerUseCase useCase;
 *
 *   &#64;PostMapping
 *   public CustomerResponse createCustomer(
 *       &#64;AuthenticationPrincipal Principal user,
 *       &#64;RequestBody CreateCustomerRequest request) {
 *     return execute(user, request);
 *   }
 *
 *   &#64;Override
 *   protected CreateCustomerUseCase getDelegate() {
 *     return useCase;
 *   }
 *
 *   &#64;Override
 *   protected Actor convertToActor(Principal principal) {
 *     return new Actor(principal.getName());
 *   }
 *
 *   &#64;Override
 *   protected CustomerPayload convertToPayload(CreateCustomerRequest input) {
 *     return new CustomerPayload(input.name(), input.email());
 *   }
 *
 *   &#64;Override
 *   protected CustomerResponse convertBody(Customer body) {
 *     return new CustomerResponse(body.id(), body.name(), body.email());
 *   }
 * }
 * }</pre>
 *
 * <h2>Adapter Workflow (Template Method Pattern)</h2>
 *
 * <p>
 * All adapters follow a consistent workflow using the Template Method pattern:
 *
 * <ol>
 * <li><b>Validate Request:</b> Ensure the request is not null
 * <li><b>Convert Input:</b> Transform domain objects to technology-specific format
 * <li><b>Validate Converted Input:</b> Ensure technical constraints are met (using
 * Jakarta Bean Validation)
 * <li><b>Process:</b> Execute the external operation (API call, DB query, message
 * publish)
 * <li><b>Convert Output:</b> Transform technology-specific response to domain format
 * <li><b>Return Response:</b> Provide the result to the caller
 * </ol>
 *
 * <p>
 * This ensures consistency across all adapters and reduces boilerplate code.
 *
 * <h2>Generic Type Parameters</h2>
 *
 * <p>
 * Each adapter is parameterized with four generic types:
 *
 * <ul>
 * <li><b>TI (Technology Input):</b> The input format expected by the external system <br>
 * Examples: {@code StripeRequest}, {@code CustomerEntity}, {@code KafkaMessage}
 * <li><b>TO (Technology Output):</b> The output format returned by the external system
 * <br>
 * Examples: {@code StripeResponse}, {@code CustomerEntity}, {@code SendResult}
 * <li><b>UPayload (Use Case Payload):</b> The domain input type from the application
 * layer <br>
 * Examples: {@code PaymentPayload}, {@code CustomerData}, {@code OrderEvent}
 * <li><b>UBody (Use Case Body):</b> The domain output type for the application layer <br>
 * Examples: {@code PaymentResult}, {@code Customer}, {@code PublishResult}
 * </ul>
 *
 * <p>
 * This provides compile-time type safety and makes the data flow explicit.
 *
 * <h2>Key Features</h2>
 *
 * <h3>Automatic Validation</h3>
 *
 * <p>
 * All adapters automatically validate converted inputs using Jakarta Bean Validation:
 *
 * <pre>{@code
 * public class StripeRequest {
 *   &#64;NotNull @Positive
 *   private Long amount;
 *
 *   &#64;NotBlank @Size(min = 3, max = 3)
 *   private String currency;
 * }
 * }</pre>
 *
 * <p>
 * The {@link #validate(Object)} method can be overridden for custom validation:
 *
 * <pre>{@code
 * &#64;Override
 * protected void validate(StripeRequest input) {
 *   super.validate(input); // Standard validation
 *
 *   if (input.getAmount() > 1_000_000) {
 *     throw new IllegalArgumentException("Amount exceeds maximum limit");
 *   }
 * }
 * }</pre>
 *
 * <h3>Separation of Concerns</h3>
 *
 * <table border="1">
 * <tr>
 * <th>Concern</th>
 * <th>Layer</th>
 * <th>Example</th>
 * </tr>
 * <tr>
 * <td>Business Rules</td>
 * <td>Use Case</td>
 * <td>"Order total must exceed minimum"</td>
 * </tr>
 * <tr>
 * <td>Technical Constraints</td>
 * <td>Adapter</td>
 * <td>"Currency code must be 3 letters"</td>
 * </tr>
 * <tr>
 * <td>Data Transformation</td>
 * <td>Adapter</td>
 * <td>Domain ↔ DTO conversion</td>
 * </tr>
 * <tr>
 * <td>External Communication</td>
 * <td>Adapter</td>
 * <td>HTTP call, DB query</td>
 * </tr>
 * </table>
 *
 * <h2>Design Principles</h2>
 *
 * <p>
 * This package follows SOLID principles:
 *
 * <ul>
 * <li><b>Dependency Inversion Principle (DIP):</b> High-level business logic depends on
 * abstractions (interfaces), not on low-level details (implementations)
 * <li><b>Single Responsibility Principle (SRP):</b> Each adapter has one reason to
 * change: the external system it adapts
 * <li><b>Open/Closed Principle (OCP):</b> Adapters are open for extension (via
 * inheritance) but closed for modification
 * <li><b>Interface Segregation Principle (ISP):</b> Small, focused interfaces (Client,
 * Repository, Messenger)
 * <li><b>Don't Repeat Yourself (DRY):</b> Common adapter logic centralized in base
 * classes
 * </ul>
 *
 * <h2>Testing Strategy</h2>
 *
 * <h3>Unit Testing Use Cases</h3> <pre>{@code
 * &#64;Test
 * void shouldCreateCustomer() {
 *   // Mock the repository adapter
 *   Repository<CustomerData, Customer> mockRepo = mock(Repository.class);
 *   when(mockRepo.send(any())).thenReturn(new RepositoryResponse<>(expectedCustomer));
 *
 *   // Test the use case in isolation
 *   CreateCustomerUseCase useCase = new CreateCustomerUseCase(mockRepo);
 *   UseCaseResponse<Customer> response = useCase.execute(request);
 *
 *   assertThat(response.body()).isEqualTo(expectedCustomer);
 * }
 * }</pre>
 *
 * <h3>Integration Testing Adapters</h3> <pre>{@code
 * &#64;Test
 * void shouldPersistToDatabase() {
 *   JpaCustomerRepository adapter = new JpaCustomerRepository(jpaRepository);
 *   RepositoryRequest<CustomerData> request = new RepositoryRequest<>(actor, data);
 *
 *   RepositoryResponse<Customer> response = adapter.send(request);
 *
 *   assertThat(response.body().id()).isNotNull();
 * }
 * }</pre>
 *
 * <h2>Best Practices</h2>
 *
 * <ol>
 * <li><b>Keep Adapters Thin:</b> Only translate data and delegate to external systems.
 * Business logic belongs in use cases.
 * <li><b>Use Immutable DTOs:</b> Technology input/output types should be immutable
 * (records or final classes).
 * <li><b>Handle Exceptions:</b> Catch technology-specific exceptions and translate them
 * to domain exceptions.
 * <li><b>Log at Boundaries:</b> Log requests/responses at adapter boundaries for
 * debugging.
 * <li><b>Use Dependency Injection:</b> Inject external clients/repositories rather than
 * creating them directly.
 * <li><b>Add Resilience:</b> Consider retry logic, circuit breakers, and timeouts for
 * external calls.
 * </ol>
 *
 * @see li.yansan.clean.application Application layer interfaces
 * @since 2.0.0
 */
package li.yansan.clean.platform;
