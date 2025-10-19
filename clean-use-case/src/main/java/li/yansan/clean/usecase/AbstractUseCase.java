package li.yansan.clean.usecase;

import static org.apache.commons.lang3.Validate.notNull;

public abstract class AbstractUseCase<I, O>
    implements UseCase<UseCaseRequest<I>, UseCaseResponse<O>> {

  @Override
  public UseCaseResponse<O> execute(UseCaseRequest<I> request) {
    notNull(request, "UseCaseRequest can not be null.");
    O output = doExecute(request.actor(), request.body());
    return new UseCaseResponse<>(output);
  }

  protected abstract O doExecute(Actor actor, I input);
}
