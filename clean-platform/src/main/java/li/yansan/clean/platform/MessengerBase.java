package li.yansan.clean.platform;

import java.util.Objects;

import li.yansan.clean.application.Actor;
import li.yansan.clean.application.messaging.Messenger;
import li.yansan.clean.application.messaging.MessengerRequest;
import li.yansan.clean.application.messaging.MessengerResponse;
import li.yansan.clean.commons.validation.Validator;

/**
 * Abstract adapter that orchestrates the interaction with an external messaging system.
 *
 * <p>
 * This class acts as a bridge between the core business logic and the external messaging
 * infrastructure (e.g., REST client, Queue publisher).
 *
 * <p>
 * <b>Responsibilities:</b>
 *
 * <ul>
 * <li><b>Take Input:</b> Accepts a standardized {@link MessengerRequest} containing the
 * payload to be sent.
 * <li><b>Map Input:</b> Converts the domain payload ({@code UPayload}) into the
 * infrastructure-specific format ({@code TI}).
 * <li><b>Validate Input:</b> Validates the converted input ({@code TI}) to ensure it
 * meets data format requirements.
 * <li><b>Send Input:</b> Sends the converted input to the external system via the
 * {@link #process(Object)} method.
 * <li><b>Map Output:</b> Converts the infrastructure output ({@code TO}) back into the
 * application domain format ({@code UBody}).
 * <li><b>Return Output:</b> Returns a {@link MessengerResponse} containing the result.
 * </ul>
 *
 * @param <TI> the type of the infrastructure input (e.g., Request DTO)
 * @param <TO> the type of the infrastructure output (e.g., Response DTO)
 * @param <UPayload> the type of the use case payload
 * @param <UBody> the type of the use case response body
 */
public abstract class MessengerBase<TI, TO, UPayload, UBody> implements Messenger<UPayload, UBody> {

	public MessengerResponse<UBody> send(MessengerRequest<UPayload> request) {
		Objects.requireNonNull(request, "MessengerRequest can not be null.");
		TI input = convertPayload(request.sender(), request.payload());
		validate(input);
		TO output = process(input);
		UBody body = convertToBody(output);
		return new MessengerResponse<>(body);
	}

	protected abstract TI convertPayload(Actor actor, UPayload payload);

	protected void validate(TI input) {
		Validator.validate(input);
	}

	protected abstract TO process(TI input);

	protected abstract UBody convertToBody(TO output);

}
