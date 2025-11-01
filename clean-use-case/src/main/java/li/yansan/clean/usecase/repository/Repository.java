package li.yansan.clean.usecase.repository;

public interface Repository<UPayload, UBody> {
  RepositoryResponse<UBody> send(RepositoryRequest<UPayload> request);
}
