package li.yansan.clean.usecase.repository;

import li.yansan.clean.usecase.Actor;

/**
 * Interface representing a repository for database communication.
 *
 * <p>Each database interaction is isolated and focused.
 *
 * <p><b>Key Benefits:</b>
 *
 * <ul>
 *   <li><b>Database Interaction:</b> Serves as the gateway for all persistence operations.
 *   <li><b>Operation-Specific Implementation:</b> Each operation defines its own repository
 *       implementation, promoting the Single Responsibility Principle.
 *   <li><b>Small Context for AI:</b> By slicing repositories into smaller, operation-specific
 *       files, we ensure the context remains small. This is optimized for AI code generation and
 *       understanding.
 *   <li><b>Decoupling:</b> Ensures that changes to one operation's persistence logic do not affect
 *       others.
 * </ul>
 *
 * @param <UPayload> the type of the request payload
 * @param <UBody> the type of the response body
 */
public interface Repository<UPayload, UBody> {

  /**
   * Sends a repository request and returns a repository response.
   *
   * <p>The implementation of {@link #send(RepositoryRequest)} should follow a structured 3-step
   * pipeline to ensure consistency and maintainability. It is highly recommended to split the logic
   * into three distinct protected methods:
   *
   * <ol>
   *   <li><b>Payload Conversion</b>: {@code convertPayload(Actor, UPayload) -> TI} - Convert the
   *       use case payload into a database request format.
   *   <li><b>Processing</b>: {@code execute(TI) -> TO} - Execute the actual database operation and
   *       receive a response.
   *   <li><b>Body Conversion</b>: {@code convertBody(TO) -> UBody} - Convert the database response
   *       back into the use case body.
   * </ol>
   *
   * <p>Example implementation using the Adapter pattern with JpaRepository:
   *
   * <pre>{@code
   * public class UserRepository implements Repository<UserPayload, UserBody> {
   *   private final UserJpaRepository jpaRepository;
   *
   *   public UserRepository(UserJpaRepository jpaRepository) {
   *     this.jpaRepository = jpaRepository;
   *   }
   *
   *   @Override
   *   public RepositoryResponse<UserBody> send(RepositoryRequest<UserPayload> request) {
   *     UserEntity entity = convertPayload(request.actor(), request.payload());
   *     UserEntity savedEntity = execute(entity);
   *     UserBody body = convertBody(savedEntity);
   *     return new RepositoryResponse<>(body);
   *   }
   *
   *   protected UserEntity convertPayload(Actor actor, UserPayload payload) {
   *     // Conversion logic using Actor and payload...
   *   }
   *
   *   protected UserEntity execute(UserEntity entity) {
   *     return jpaRepository.save(entity);
   *   }
   *
   *   protected UserBody convertBody(UserEntity entity) {
   *     // Conversion logic using entity...
   *   }
   * }
   * }</pre>
   *
   * @param request the repository request containing actor and payload
   * @return the repository response containing the response body
   */
  RepositoryResponse<UBody> send(RepositoryRequest<UPayload> request);

  /**
   * Convenience method to send a request and directly return the response body.
   *
   * @param actor the actor performing the request
   * @param payload the use case payload
   * @return the response body
   */
  default UBody send(Actor actor, UPayload payload) {
    return send(new RepositoryRequest<UPayload>(actor, payload)).body();
  }
}
