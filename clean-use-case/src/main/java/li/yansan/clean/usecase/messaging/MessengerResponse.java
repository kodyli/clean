package li.yansan.clean.usecase.messaging;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import li.yansan.clean.commons.convert.Convertible;
import li.yansan.clean.commons.validation.Validator;

public record MessengerResponse<UBody>(@Valid @NotNull UBody body) {
  public MessengerResponse(UBody body) {
    this.body = body;
    validate();
  }

  public MessengerResponse(Convertible<UBody> body) {
    this(body.convert());
  }

  private void validate() {
    Validator.validate(this);
  }
}
