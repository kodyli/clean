package li.yansan.clean.usecase.client;

public interface Client<UPayload, UBody> {

	ClientResponse<UBody> send(ClientRequest<UPayload> request);

}
