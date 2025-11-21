package li.yansan.clean.usecase;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import li.yansan.clean.commons.convert.Converter;
import li.yansan.clean.commons.convert.Convertible;
import li.yansan.clean.commons.validation.Validator;

public record UseCaseRequest<UPayload>(
    @Valid @NotNull Actor actor, @Valid @NotNull UPayload payload) {
  public UseCaseRequest(Actor actor, UPayload payload) {
    this.actor = actor;
    this.payload = payload;
    validate();
  }

  public UseCaseRequest(Actor actor, Convertible<UPayload> payload) {
    this(actor, payload.convert());
  }

  public <T> UseCaseRequest(Converter<T, Actor> converter, T user, Convertible<UPayload> payload) {
    this(converter.convert(user), payload.convert());
  }

  public <T> UseCaseRequest(Converter<T, Actor> converter, T user, UPayload payload) {
    this(converter.convert(user), payload);
  }

  private void validate() {
    Validator.validate(this);
  }
}
