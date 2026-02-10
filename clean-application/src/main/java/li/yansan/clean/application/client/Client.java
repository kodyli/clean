package li.yansan.clean.application.client;

import li.yansan.clean.application.Actor;

public interface Client<UPayload, UBody> {

	ClientResponse<UBody> send(ClientRequest<UPayload> request);

	default UBody send(Actor actor, UPayload uPayload) {
		return send(new ClientRequest<>(actor, uPayload)).body();
	}

}
