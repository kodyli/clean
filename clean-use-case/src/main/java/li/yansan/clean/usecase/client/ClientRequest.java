package li.yansan.clean.usecase.client;

import li.yansan.clean.commons.convert.Convertible;
import li.yansan.clean.usecase.Actor;

public record ClientRequest<TBody>(Actor actor, TBody body) {
  public ClientRequest(Actor actor, TBody body) {
    this.actor = actor;
    this.body = body;
  }

  public ClientRequest(Actor actor, Convertible<TBody> body) {
    this(actor, body.convert());
  }
}
