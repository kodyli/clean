package li.yansan.clean.usecase.repository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import li.yansan.clean.commons.validation.Validator;

public record RepositoryResponse<UBody>(@NotNull @Valid UBody body) {
  public RepositoryResponse(UBody body) {
    this.body = body;
    validate();
  }

  private void validate() {
    Validator.validate(this);
  }
}
