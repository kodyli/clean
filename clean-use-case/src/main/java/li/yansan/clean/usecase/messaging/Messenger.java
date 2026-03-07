package li.yansan.clean.usecase.messaging;

import li.yansan.clean.usecase.Actor;

public interface Messenger<UPayload, UBody> {

	MessengerResponse<UBody> send(MessengerRequest<UPayload> request);

	default UBody send(Actor actor, UPayload payload) {
		return send(new MessengerRequest<>(actor, payload)).body();
	}

}
