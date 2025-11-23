package li.yansan.clean.platform;

import li.yansan.clean.commons.validation.Validator;
import li.yansan.clean.usecase.Actor;
import li.yansan.clean.usecase.client.Client;
import li.yansan.clean.usecase.client.ClientRequest;
import li.yansan.clean.usecase.client.ClientResponse;

public abstract class AbstractClientAdapter<TI, TO, UPayload, UBody> implements Client<UPayload, UBody> {

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
