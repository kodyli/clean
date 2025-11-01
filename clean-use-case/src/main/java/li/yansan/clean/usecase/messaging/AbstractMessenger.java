package li.yansan.clean.usecase.messaging;

import li.yansan.clean.usecase.Actor;

public abstract class AbstractMessenger<UPayload, TBody> implements Messenger<UPayload, TBody> {
  @Override
  public MessengerResponse<TBody> send(MessengerRequest<UPayload> request) {
    if (request == null) {
      throw new IllegalArgumentException("MessengerRequest can not be null.");
    }
    TBody output = doSend(request.sender(), request.payload());
    return new MessengerResponse<>(output);
  }

  protected abstract TBody doSend(Actor actor, UPayload message);
}
