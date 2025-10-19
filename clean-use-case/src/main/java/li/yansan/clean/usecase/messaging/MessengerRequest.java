package li.yansan.clean.usecase.messaging;

import li.yansan.clean.commons.convert.Convertible;
import li.yansan.clean.usecase.Actor;

public record MessengerRequest<TMsg>(Actor sender, TMsg message) {
  public MessengerRequest(Actor sender, TMsg message) {
    this.sender = sender;
    this.message = message;
  }

  public MessengerRequest(Actor sender, Convertible<TMsg> message) {
    this(sender, message.convert());
  }
}
