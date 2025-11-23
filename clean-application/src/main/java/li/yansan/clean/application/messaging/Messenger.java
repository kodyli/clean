package li.yansan.clean.application.messaging;

public interface Messenger<UPayload, UBody> {

	MessengerResponse<UBody> send(MessengerRequest<UPayload> request);

}
