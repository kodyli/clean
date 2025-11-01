package li.yansan.clean.usecase.repository;

import li.yansan.clean.usecase.Actor;

public abstract class AbstractRepository<UPayload, UBody> implements Repository<UPayload, UBody> {
  @Override
  public RepositoryResponse<UBody> send(RepositoryRequest<UPayload> request) {
    if (request == null) {
      throw new IllegalArgumentException("RepositoryRequest can not be null.");
    }
    UBody output = doSend(request.sender(), request.payload());
    return new RepositoryResponse<>(output);
  }

  protected abstract UBody doSend(Actor actor, UPayload payload);
}
