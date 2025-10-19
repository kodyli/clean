package li.yansan.clean.usecase.repository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import li.yansan.clean.commons.validation.Validator;

public record RepositoryResponse<TBody>(@NotNull @Valid TBody body) {
  public RepositoryResponse(TBody body) {
    this.body = body;
    validate();
  }

  private void validate() {
    Validator.validate(this);
  }
}
