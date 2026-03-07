package li.yansan.clean.client;

import jakarta.validation.constraints.NotNull;
import li.yansan.clean.usecase.Actor;
import li.yansan.clean.commons.convert.Convertible;
import li.yansan.clean.commons.validation.Validator;

public record ClientRequest<UPayload>(@NotNull Actor actor, @NotNull UPayload payload) {
	public ClientRequest(Actor actor, UPayload payload) {
		this.actor = actor;
		this.payload = payload;
		validate();
	}

	public ClientRequest(Actor actor, Convertible<UPayload> payload) {
		this(actor, payload.convert());
	}

	private void validate() {
		Validator.validate(this);
	}
}
