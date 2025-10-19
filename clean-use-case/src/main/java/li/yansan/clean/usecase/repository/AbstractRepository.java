package li.yansan.clean.usecase.repository;

import li.yansan.clean.usecase.Actor;

public abstract class AbstractRepository<I, O>
    implements Repository<RepositoryRequest<I>, RepositoryResponse<O>> {
  @Override
  public RepositoryResponse<O> send(RepositoryRequest<I> request) {
    if (request == null) {
      throw new IllegalArgumentException("RepositoryRequest can not be null.");
    }
    O output = doSend(request.sender(), request.body());
    return new RepositoryResponse<>(output);
  }

  protected abstract O doSend(Actor actor, I input);

  protected abstract Class<?> getGroup();
}
