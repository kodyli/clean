package li.yansan.clean.platform;

import li.yansan.clean.commons.validation.Validator;
import li.yansan.clean.usecase.Actor;
import li.yansan.clean.usecase.repository.Repository;
import li.yansan.clean.usecase.repository.RepositoryRequest;
import li.yansan.clean.usecase.repository.RepositoryResponse;

public abstract class AbstractRepositoryAdapter<TI, TO, UPayload, UBody>
    implements Repository<UPayload, UBody> {
  public RepositoryResponse<UBody> send(RepositoryRequest<UPayload> request) {
    if (request == null) {
      throw new IllegalArgumentException("RepositoryRequest can not be null.");
    }
    TI input = convertPayload(request.sender(), request.payload());
    validate(input);
    TO output = process(input);
    UBody body = convertToBody(output);
    return new RepositoryResponse<>(body);
  }

  protected abstract TI convertPayload(Actor actor, UPayload payload);

  protected void validate(TI input) {
    Validator.validate(input);
  }

  protected abstract TO process(TI input);

  protected abstract UBody convertToBody(TO output);
}
