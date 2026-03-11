package li.yansan.clean.usecase.client;

import java.util.HashMap;
import java.util.Map;

public class LocalClient<UPayload, UBody> implements Client<UPayload, UBody> {

	private final Map<ClientRequest<UPayload>, ClientResponse<UBody>> store;

	public LocalClient() {
		this.store = new HashMap<>();
	}

	@Override
	public ClientResponse<UBody> send(ClientRequest<UPayload> request) {
		System.out.println("Sending Client Request");
		System.out.println(request.toString());
		System.out.println("Client Request Sent");
		return this.store.get(request);
	}

	public void put(ClientRequest<UPayload> request, ClientResponse<UBody> response) {
		this.store.put(request, response);
	}

}
