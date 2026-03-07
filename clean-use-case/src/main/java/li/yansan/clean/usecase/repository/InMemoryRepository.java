package li.yansan.clean.usecase.repository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryRepository implements Repository<Object, Object> {

	private final Map<RepositoryRequest<Object>, RepositoryResponse<Object>> store;

	public InMemoryRepository() {
		this.store = new HashMap<>();
	}

	@Override
	public RepositoryResponse<Object> send(RepositoryRequest<Object> request) {
		System.out.println("Sending Repository Request");
		System.out.println(request.toString());
		System.out.println("Repository Request Sent");
		return this.store.get(request);
	}

	public void put(RepositoryRequest<Object> request, RepositoryResponse<Object> response) {
		this.store.put(request, response);
	}

}
