package li.yansan.clean.platform;

import java.security.Principal;

import li.yansan.clean.application.Actor;
import li.yansan.clean.application.UseCase;
import li.yansan.clean.application.UseCaseRequest;
import li.yansan.clean.application.UseCaseResponse;
import li.yansan.clean.commons.validation.Validator;

/**
 * Abstract adapter that orchestrates the execution of a Use Case from an external
 * interface (e.g., HTTP/REST).
 *
 * <p>
 * This class acts as a bridge between the delivery mechanism (web, console) and the core
 * business logic.
 *
 * <p>
 * <b>Responsibilities:</b>
 *
 * <ul>
 * <li><b>Map HTTP Request:</b> Converts external input ({@code TI}) into Java objects.
 * <li><b>Authorization:</b> Performs authorization checks by converting the
 * {@link Principal} into a domain {@link Actor}.
 * <li><b>Map Input:</b> Converts the input object into the use case's input model ({@code
 *       UPayload}).
 * <li><b>Call Use Case:</b> Executes the underlying {@link UseCase}.
 * <li><b>Map Output:</b> Converts the use case's output ({@code UBody}) back to the
 * external response format ({@code TO}).
 * <li><b>Return Response:</b> Returns the final response object to the caller.
 * </ul>
 *
 * @param <TI> the type of the input (e.g., Request DTO)
 * @param <TO> the type of the output (e.g., Response DTO)
 * @param <UPayload> the type of the use case payload
 * @param <UBody> the type of the use case response body
 */
public abstract class UseCaseBase<TI, TO, UPayload, UBody> {

	public TO execute(Principal user, TI input) {
		Actor actor = convertToActor(user);
		UPayload payload = convertToPayload(input);
		UseCase<UPayload, UBody> delegate = getDelegate();
		if (actor == null || payload == null || delegate == null) {
			throw new IllegalArgumentException();
		}
		UseCaseResponse<UBody> uRes = delegate.execute(new UseCaseRequest<>(actor, payload));
		if (uRes == null) {
			throw new IllegalArgumentException("UseCaseResponse can not be null.");
		}
		TO output = convertBody(uRes.body());
		validate(output);
		return output;
	}

	protected void validate(TO output) {
		Validator.validate(output);
	}

	protected abstract UseCase<UPayload, UBody> getDelegate();

	protected abstract Actor convertToActor(Principal principal);

	protected abstract UPayload convertToPayload(TI input);

	protected abstract TO convertBody(UBody body);

}
