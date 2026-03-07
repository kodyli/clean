package li.yansan.clean.usecase.client;

import java.util.HashMap;
import java.util.Map;

public class LocalClient implements Client<Object, Object> {

	private final Map<ClientRequest<Object>, ClientResponse<Object>> store;

	public LocalClient() {
		this.store = new HashMap<>();
	}

	@Override
	public ClientResponse<Object> send(ClientRequest<Object> request) {
		System.out.println("Sending Client Request");
		System.out.println(request.toString());
		System.out.println("Client Request Sent");
		return this.store.get(request);
	}

	public void put(ClientRequest<Object> request, ClientResponse<Object> response) {
		this.store.put(request, response);
	}

}
