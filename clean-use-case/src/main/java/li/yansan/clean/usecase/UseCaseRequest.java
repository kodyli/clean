package li.yansan.clean.usecase;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import li.yansan.clean.commons.convert.Converter;
import li.yansan.clean.commons.convert.Convertible;
import li.yansan.clean.commons.validation.Validator;

public record UseCaseRequest<TBody>(@NotNull Actor actor, @Valid @NotNull TBody body) {
  public UseCaseRequest(Actor actor, TBody body) {
    this.actor = actor;
    this.body = body;
    validate();
  }

  public UseCaseRequest(Actor actor, Convertible<TBody> body) {
    this(actor, body.convert());
  }

  public <T> UseCaseRequest(Converter<T, Actor> converter, T user, Convertible<TBody> body) {
    this(converter.convert(user), body.convert());
  }

  public <T> UseCaseRequest(Converter<T, Actor> converter, T user, TBody body) {
    this(converter.convert(user), body);
  }

  private void validate() {
    Validator.validate(this);
  }
}
