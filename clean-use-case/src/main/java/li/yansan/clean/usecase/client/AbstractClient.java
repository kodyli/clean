package li.yansan.clean.usecase.client;

import li.yansan.clean.usecase.Actor;

public abstract class AbstractClient<UPayload, UBody> implements Client<UPayload, UBody> {
  @Override
  public ClientResponse<UBody> send(ClientRequest<UPayload> request) {
    if (request == null) {
      throw new IllegalArgumentException("ClientRequest can not be null.");
    }
    UBody output = doSend(request.actor(), request.payload());
    return new ClientResponse<>(output);
  }

  protected abstract UBody doSend(Actor actor, UPayload input);
}
