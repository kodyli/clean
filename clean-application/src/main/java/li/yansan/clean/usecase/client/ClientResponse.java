package li.yansan.clean.usecase.client;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import li.yansan.clean.commons.convert.Convertible;
import li.yansan.clean.commons.validation.Validator;

/**
 * Represents a standardized response from a third-party Client.
 *
 * <p>
 * This record encapsulates the result of an external API call, ensuring that the returned
 * data is valid and not null.
 *
 * <p>
 * <b>Responsibilities:</b>
 *
 * <ul>
 * <li><b>Encapsulation:</b> Holds the {@code body} of the response returned by the
 * client.
 * <li><b>Validation:</b> Enforces that the {@code body} is not null and satisfies any
 * constraints defined on it (e.g., via Bean Validation annotations) upon instantiation.
 * <li><b>Transformation:</b> Provides constructors to facilitate the conversion of
 * different data types into the required domain object ({@code UBody}).
 * </ul>
 *
 * @param <UBody> the type of the response body
 * @param body the body of the response; must not be null
 */
public record ClientResponse<UBody>(@Valid @NotNull UBody body) {
	public ClientResponse(UBody body) {
		this.body = body;
		validate();
	}

	public ClientResponse(Convertible<UBody> body) {
		this(body.convert());
	}

	private void validate() {
		Validator.validate(this);
	}
}
