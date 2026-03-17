package li.yansan.clean.usecase.handler;

import li.yansan.clean.usecase.Actor;
import li.yansan.clean.usecase.UseCaseRequest;
import org.apache.commons.lang3.Validate;

public abstract class AbstractHandler<UPayload, UBody> implements Handler<UPayload, UBody> {

  @Override
  public boolean support(UseCaseRequest<UPayload> request) {
    Validate.notNull(
        request,
        "Cannot support a null request. Check the caller of %s",
        this.getClass().getSimpleName());
    return doSupport(request.actor(), request.payload());
  }

  protected abstract boolean doSupport(Actor actor, UPayload payload);
}
