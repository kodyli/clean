package li.yansan.clean.usecase.messaging;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import li.yansan.clean.commons.convert.Convertible;
import li.yansan.clean.commons.validation.Validator;

public record MessengerResponse<TBody>(@NotNull @Valid TBody body) {
  public MessengerResponse(TBody body) {
    this.body = body;
    validate();
  }

  public MessengerResponse(Convertible<TBody> body) {
    this(body.convert());
  }

  private void validate() {
    Validator.validate(this);
  }
}
