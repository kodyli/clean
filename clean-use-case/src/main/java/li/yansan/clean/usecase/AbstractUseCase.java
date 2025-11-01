package li.yansan.clean.usecase;

import static org.apache.commons.lang3.Validate.notNull;

public abstract class AbstractUseCase<UPayload, UBody> implements UseCase<UPayload, UBody> {

  @Override
  public UseCaseResponse<UBody> execute(UseCaseRequest<UPayload> request) {
    notNull(request, "UseCaseRequest can not be null.");
    UBody output = doExecute(request.actor(), request.payload());
    return new UseCaseResponse<>(output);
  }

  protected abstract UBody doExecute(Actor actor, UPayload input);
}
