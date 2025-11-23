package li.yansan.clean.usecase.messaging;

public interface Messenger<UPayload, UBody> {

	MessengerResponse<UBody> send(MessengerRequest<UPayload> request);

}
