package li.yansan.clean.usecase.client;

import li.yansan.clean.commons.convert.Convertible;
import li.yansan.clean.usecase.Actor;

public record ClientRequest<UPayload>(Actor actor, UPayload payload) {
  public ClientRequest(Actor actor, UPayload payload) {
    this.actor = actor;
    this.payload = payload;
  }

  public ClientRequest(Actor actor, Convertible<UPayload> payload) {
    this(actor, payload.convert());
  }
}
