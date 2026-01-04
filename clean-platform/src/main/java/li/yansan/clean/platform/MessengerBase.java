package li.yansan.clean.platform;

import li.yansan.clean.application.Actor;
import li.yansan.clean.application.messaging.Messenger;
import li.yansan.clean.application.messaging.MessengerRequest;
import li.yansan.clean.application.messaging.MessengerResponse;
import li.yansan.clean.commons.validation.Validator;

public abstract class MessengerBase<TI, TO, UPayload, UBody> implements Messenger<UPayload, UBody> {

	public MessengerResponse<UBody> send(MessengerRequest<UPayload> request) {
		if (request == null) {
			throw new IllegalArgumentException("MessengerRequest can not be null.");
		}
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
