package li.yansan.clean.application.client;

public interface Client<UPayload, UBody> {

	ClientResponse<UBody> send(ClientRequest<UPayload> request);

}
