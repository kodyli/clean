package li.yansan.clean.usecase.client;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import li.yansan.clean.commons.convert.Convertible;
import li.yansan.clean.commons.validation.Validator;

public record ClientResponse<UBody>(@NotNull @Valid UBody body) {
  public ClientResponse(UBody body) {
    this.body = body;
    validate();
  }

  public ClientResponse(Convertible<UBody> body) {
    this(body.convert());
  }

  private void validate() {
    Validator.validate(this);
  }
}
