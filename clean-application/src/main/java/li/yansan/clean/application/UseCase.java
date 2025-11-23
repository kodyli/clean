package li.yansan.clean.usecase;

import li.yansan.clean.usecase.client.Client;
import li.yansan.clean.usecase.messaging.Messenger;
import li.yansan.clean.usecase.repository.Repository;

/**
 * Defines the contract for a business Use Case.
 *
 * <p>
 * A Use Case represents a specific business action or process. It orchestrates the flow
 * of data to achieve a business goal.
 *
 * <p>
 * <b>Responsibilities:</b>
 *
 * <ul>
 * <li><b>Take Request:</b> Accepts a standardized {@link UseCaseRequest} containing the
 * input data.
 * <li><b>Validate Business Rules:</b> Enforces domain-specific rules and invariants.
 * <li><b>Manipulate Model State:</b> Interacts with domain entities and external systems
 * to update the system state. This includes communicating with databases using
 * {@link Repository}, third-party APIs using {@link Client}, and messaging systems using
 * {@link Messenger}.
 * <li><b>Return Response:</b> Produces a {@link UseCaseResponse} containing the result of
 * the operation.
 * </ul>
 *
 * @param <UPayload> the type of the request payload
 * @param <UBody> the type of the response body
 */
public interface UseCase<UPayload, UBody> {

	/**
	 * Executes the business logic for this use case.
	 * @param request the input request containing the actor and payload
	 * @return the response containing the result of the execution
	 */
	UseCaseResponse<UBody> execute(UseCaseRequest<UPayload> request);

}
