package li.yansan.clean.dispatcher;

import java.util.List;
import java.util.PriorityQueue;
import li.yansan.clean.dispatcher.handler.Handler;
import li.yansan.clean.usecase.UseCase;
import org.apache.commons.lang3.Validate;

public interface DispatcherUseCase<UPayload, UBody> extends UseCase<UPayload, UBody> {

  List<Handler<UPayload, UBody>> getHandlers();

  void addHandler(Handler<UPayload, UBody> handler);

  default void addHandlers(List<Handler<UPayload, UBody>> handlers) {
    Validate.notEmpty(handlers, "The handler list cannot be null or empty.");
    handlers.forEach(this::addHandler);
  }

  default void addHandlers(PriorityQueue<Handler<UPayload, UBody>> handlers) {
    Validate.notEmpty(handlers, "The handler queue cannot be null or empty.");
    while (!handlers.isEmpty()) {
      this.addHandler(handlers.poll());
    }
  }
}
