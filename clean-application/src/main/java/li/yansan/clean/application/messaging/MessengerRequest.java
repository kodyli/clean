package li.yansan.clean.application.messaging;

import jakarta.validation.constraints.NotNull;
import li.yansan.clean.application.Actor;
import li.yansan.clean.commons.convert.Convertible;
import li.yansan.clean.commons.validation.Validator;

public record MessengerRequest<UPayload>(@NotNull Actor sender, @NotNull UPayload payload) {
	public MessengerRequest(Actor sender, UPayload payload) {
		this.sender = sender;
		this.payload = payload;
		validate();
	}

	public MessengerRequest(Actor sender, Convertible<UPayload> payload) {
		this(sender, payload.convert());
	}

	private void validate() {
		Validator.validate(this);
	}
}
