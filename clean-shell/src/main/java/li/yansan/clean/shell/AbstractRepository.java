package li.yansan.clean.shell;

import java.util.Objects;

import li.yansan.clean.usecase.Actor;
import li.yansan.clean.usecase.repository.Repository;
import li.yansan.clean.usecase.repository.RepositoryRequest;
import li.yansan.clean.usecase.repository.RepositoryResponse;
import li.yansan.clean.commons.validation.Validator;

/**
 * Abstract adapter that orchestrates the interaction with a database.
 *
 * <p>
 * This class acts as a bridge between the core business logic and the persistence layer.
 *
 * <p>
 * <b>Responsibilities:</b>
 *
 * <ul>
 * <li><b>Take Input:</b> Accepts a standardized {@link RepositoryRequest} containing the
 * data to be persisted or queried.
 * <li><b>Map Input:</b> Converts the domain input ({@code UPayload}) into the
 * database-specific format ({@code TI}).
 * <li><b>Validate Input:</b> Validates the converted input ({@code TI}) to ensure it
 * meets data format requirements (e.g., field length, nullability). <b>Note:</b> This is
 * NOT business validation.
 * <li><b>Send Input:</b> Sends the converted input to the database via the
 * {@link #process(Object)} method.
 * <li><b>Map Output:</b> Converts the database output ({@code TO}) back into the usecase
 * domain format ({@code UBody}).
 * <li><b>Return Output:</b> Returns a {@link RepositoryResponse} containing the result.
 * </ul>
 *
 * @param <TI> the type of the database input (e.g., Entity or DTO)
 * @param <TO> the type of the database output (e.g., Entity or DTO)
 * @param <UPayload> the type of the use case payload
 * @param <UBody> the type of the use case response body
 */
public abstract class AbstractRepository<TI, TO, UPayload, UBody> implements Repository<UPayload, UBody> {

	/**
	 * Sends a repository request.
	 * @param request the repository request
	 * @return the repository response
	 */
	@Override
	public final RepositoryResponse<UBody> send(final RepositoryRequest<UPayload> request) {
		Objects.requireNonNull(request, "RepositoryRequest can not be null.");
		TI input = convertPayload(request.sender(), request.payload());
		Validator.validate(input);
		TO output = process(input);
		UBody body = convertBody(output);
		return new RepositoryResponse<>(body);
	}

	protected abstract TI convertPayload(Actor actor, UPayload payload);

	protected abstract TO process(TI input);

	protected abstract UBody convertBody(TO output);

}
