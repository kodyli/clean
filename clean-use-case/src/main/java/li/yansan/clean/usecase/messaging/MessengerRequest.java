package li.yansan.clean.usecase.messaging;

import li.yansan.clean.commons.convert.Convertible;
import li.yansan.clean.usecase.Actor;

public record MessengerRequest<UPayload>(Actor sender, UPayload payload) {
  public MessengerRequest(Actor sender, UPayload payload) {
    this.sender = sender;
    this.payload = payload;
  }

  public MessengerRequest(Actor sender, Convertible<UPayload> payload) {
    this(sender, payload.convert());
  }
}
