package li.yansan.clean.usecase.client;

import li.yansan.clean.usecase.Actor;

public abstract class AbstractClient<I, O> implements Client<ClientRequest<I>, ClientResponse<O>> {
  @Override
  public ClientResponse<O> send(ClientRequest<I> request) {
    if (request == null) {
      throw new IllegalArgumentException("ClientRequest can not be null.");
    }
    O output = doSend(request.actor(), request.body());
    return new ClientResponse<>(output);
  }

  protected abstract O doSend(Actor actor, I input);
}
