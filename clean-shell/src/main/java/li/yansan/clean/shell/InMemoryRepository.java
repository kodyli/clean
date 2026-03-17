package li.yansan.clean.shell;

import java.util.HashMap;
import java.util.Map;
import li.yansan.clean.usecase.repository.Repository;
import li.yansan.clean.usecase.repository.RepositoryRequest;
import li.yansan.clean.usecase.repository.RepositoryResponse;

public class InMemoryRepository<UPayload, UBody> implements Repository<UPayload, UBody> {

  private final Map<RepositoryRequest<UPayload>, RepositoryResponse<UBody>> store;

  public InMemoryRepository() {
    this.store = new HashMap<>();
  }

  @Override
  public RepositoryResponse<UBody> send(RepositoryRequest<UPayload> request) {
    System.out.println("Sending Repository Request");
    System.out.println(request.toString());
    System.out.println("Repository Request Sent");
    return this.store.get(request);
  }

  public void put(RepositoryRequest<UPayload> request, RepositoryResponse<UBody> response) {
    this.store.put(request, response);
  }
}
