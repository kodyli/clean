package li.yansan.clean.usecase.repository;

/**
 * Defines the contract for interacting with the database.
 *
 * <p>
 * This interface is designed to be implemented per operation, ensuring that each database
 * interaction is isolated and focused.
 *
 * <p>
 * <b>Key Benefits:</b>
 *
 * <ul>
 * <li><b>Database Interaction:</b> Serves as the gateway for all persistence operations.
 * <li><b>Operation-Specific Implementation:</b> Each operation defines its own repository
 * implementation, promoting the Single Responsibility Principle.
 * <li><b>Small Context for AI:</b> By slicing repositories into smaller,
 * operation-specific files, we ensure the context remains small. This is optimized for AI
 * code generation and understanding.
 * <li><b>Decoupling:</b> Ensures that changes to one operation's persistence logic do not
 * affect others.
 * </ul>
 *
 * @param <UPayload> the type of the request payload
 * @param <UBody> the type of the response body
 */
public interface Repository<UPayload, UBody> {

	RepositoryResponse<UBody> send(RepositoryRequest<UPayload> request);

}
