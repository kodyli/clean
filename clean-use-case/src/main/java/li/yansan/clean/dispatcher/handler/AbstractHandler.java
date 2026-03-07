package li.yansan.clean.usecase.handler;

import li.yansan.clean.usecase.Actor;
import li.yansan.clean.usecase.UseCaseRequest;
import org.apache.commons.lang3.Validate;

public abstract class AbstractHandler<UPayload, UBody> implements Handler<UPayload, UBody> {

	@Override
	public boolean support(UseCaseRequest<UPayload> request) {
		Validate.notNull(request, "");
		return support(request.actor(), request.payload());
	}

	abstract protected boolean support(Actor actor, UPayload payload);

}
