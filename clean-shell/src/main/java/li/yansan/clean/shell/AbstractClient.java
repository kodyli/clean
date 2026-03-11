package li.yansan.clean.shell;

import java.util.Objects;

import li.yansan.clean.usecase.Actor;
import li.yansan.clean.usecase.client.Client;
import li.yansan.clean.usecase.client.ClientRequest;
import li.yansan.clean.usecase.client.ClientResponse;
import li.yansan.clean.commons.validation.Validator;

/**
 * Abstract adapter for clients.
 *
 * @param <TI> the type of the infrastructure input
 * @param <TO> the type of the infrastructure output
 * @param <UPayload> the type of the use case payload
 * @param <UBody> the type of the use case response body
 */
public abstract class AbstractClient<TI, TO, UPayload, UBody> implements Client<UPayload, UBody> {

	/**
	 * Sends a client request.
	 * @param request the client request
	 * @return the client response
	 */
	@Override
	public final ClientResponse<UBody> send(final ClientRequest<UPayload> request) {
		Objects.requireNonNull(request, "ClientRequest can not be null.");
		TI input = convertPayload(request.actor(), request.payload());
		Validator.validate(input);
		TO output = process(input);
		UBody body = convertBody(output);
		return new ClientResponse<>(body);
	}

	protected abstract TI convertPayload(Actor actor, UPayload payload);

	protected abstract TO process(TI input);

	protected abstract UBody convertBody(TO output);

}
