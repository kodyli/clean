package li.yansan.clean.usecase.repository;

import li.yansan.clean.commons.convert.Convertible;
import li.yansan.clean.usecase.Actor;

public record RepositoryRequest<UPayload>(Actor sender, UPayload payload) {
  public RepositoryRequest(Actor sender, UPayload payload) {
    this.sender = sender;
    this.payload = payload;
    validate();
  }

  public RepositoryRequest(Actor sender, Convertible<UPayload> body) {
    this(sender, body.convert());
  }

  public void validate() {
    if (sender == null) {
      throw new IllegalArgumentException();
    }
    if (payload == null) {
      throw new IllegalArgumentException();
    }
  }
}
