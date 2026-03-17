package li.yansan.clean.shell;

import java.util.HashMap;
import java.util.Map;
import li.yansan.clean.usecase.client.Client;
import li.yansan.clean.usecase.client.ClientRequest;
import li.yansan.clean.usecase.client.ClientResponse;

public class LocalClient<UPayload, UBody> implements Client<UPayload, UBody> {

  private final Map<ClientRequest<UPayload>, ClientResponse<UBody>> store;

  public LocalClient() {
    this.store = new HashMap<>();
  }

  @Override
  public ClientResponse<UBody> send(ClientRequest<UPayload> request) {
    if (request == null) {
      throw new IllegalArgumentException("ClientRequest cannot be null");
    }

    System.out.println("Sending Client Request: " + request);
    ClientResponse<UBody> response = this.store.get(request);
    System.out.println("Client Request Sent. Response: " + response);

    return response;
  }

  public void put(ClientRequest<UPayload> request, ClientResponse<UBody> response) {
    this.store.put(request, response);
  }
}
