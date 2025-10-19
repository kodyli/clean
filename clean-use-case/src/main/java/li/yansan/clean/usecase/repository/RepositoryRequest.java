package li.yansan.clean.usecase.repository;

import li.yansan.clean.commons.convert.Convertible;
import li.yansan.clean.usecase.Actor;

public record RepositoryRequest<TBody>(Actor sender, TBody body) {
  public RepositoryRequest(Actor sender, TBody body) {
    this.sender = sender;
    this.body = body;
    validate();
  }

  public RepositoryRequest(Actor sender, Convertible<TBody> body) {
    this(sender, body.convert());
  }

  public void validate() {
    if (sender == null) {
      throw new IllegalArgumentException();
    }
    if (body == null) {
      throw new IllegalArgumentException();
    }
  }
}
