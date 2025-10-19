package li.yansan.clean.usecase.messaging;

import li.yansan.clean.usecase.Actor;

public abstract class AbstractMessenger<I, O>
    implements Messenger<MessengerRequest<I>, MessengerResponse<O>> {
  @Override
  public MessengerResponse<O> send(MessengerRequest<I> request) {
    if (request == null) {
      throw new IllegalArgumentException("MessengerRequest can not be null.");
    }
    O output = doSend(request.sender(), request.message());
    return new MessengerResponse<>(output);
  }

  protected abstract O doSend(Actor actor, I message);
}
