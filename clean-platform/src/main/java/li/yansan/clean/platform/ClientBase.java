package li.yansan.clean.platform;

import li.yansan.clean.application.Actor;
import li.yansan.clean.application.client.Client;
import li.yansan.clean.application.client.ClientRequest;
import li.yansan.clean.application.client.ClientResponse;
import li.yansan.clean.commons.validation.Validator;

public abstract class ClientBase<TI, TO, UPayload, UBody> implements Client<UPayload, UBody> {

	public ClientResponse<UBody> send(ClientRequest<UPayload> request) {
		if (request == null) {
			throw new IllegalArgumentException("ClientRequest can not be null.");
		}
		TI input = convertPayload(request.actor(), request.payload());
		validate(input);
		TO output = process(input);
		UBody body = convertToBody(output);
		return new ClientResponse<>(body);
	}

	protected abstract TI convertPayload(Actor actor, UPayload payload);

	protected void validate(TI input) {
		Validator.validate(input);
	}

	protected abstract TO process(TI input);

	protected abstract UBody convertToBody(TO output);

}
