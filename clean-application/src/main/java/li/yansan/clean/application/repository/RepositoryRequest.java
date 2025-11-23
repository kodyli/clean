package li.yansan.clean.usecase.repository;

import jakarta.validation.constraints.NotNull;
import li.yansan.clean.commons.convert.Convertible;
import li.yansan.clean.commons.validation.Validator;
import li.yansan.clean.usecase.Actor;

public record RepositoryRequest<UPayload>(@NotNull Actor sender, @NotNull UPayload payload) {
	public RepositoryRequest(Actor sender, UPayload payload) {
		this.sender = sender;
		this.payload = payload;
		validate();
	}

	public RepositoryRequest(Actor sender, Convertible<UPayload> body) {
		this(sender, body.convert());
	}

	private void validate() {
		Validator.validate(this);
	}
}
