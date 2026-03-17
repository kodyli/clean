package li.yansan.clean.usecase;

import java.util.ArrayList;
import java.util.List;
import li.yansan.clean.usecase.handler.Handler;
import li.yansan.clean.usecase.handler.UnsupportedHandlerException;
import org.apache.commons.lang3.Validate;

public class DefaultDispatcherUseCase<UPayload, UBody>
    implements DispatcherUseCase<UPayload, UBody> {

  protected final List<Handler<UPayload, UBody>> handlers;

  public DefaultDispatcherUseCase() {
    this.handlers = new ArrayList<>();
  }

  @Override
  public List<Handler<UPayload, UBody>> getHandlers() {
    return this.handlers;
  }

  @Override
  public void addHandler(Handler<UPayload, UBody> handler) {
    Validate.notNull(handler, "Cannot add a null handler to the dispatcher");
    this.handlers.add(handler);
  }

  @Override
  public UseCaseResponse<UBody> execute(UseCaseRequest<UPayload> request) {
    Validate.notNull(
        request,
        "Dispatcher cannot execute a null request. Check the caller of %s",
        this.getClass().getSimpleName());
    for (Handler<UPayload, UBody> handler : this.getHandlers()) {
      if (handler.support(request)) {
        return handler.execute(request);
      }
    }
    throw new UnsupportedHandlerException(request, this.getHandlers());
  }
}
