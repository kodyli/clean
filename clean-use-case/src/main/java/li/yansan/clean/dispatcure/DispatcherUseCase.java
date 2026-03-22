package li.yansan.clean.dispatcure;

import java.util.List;
import java.util.PriorityQueue;
import li.yansan.clean.dispatcure.handler.Handler;
import li.yansan.clean.usecase.UseCase;
import org.apache.commons.lang3.Validate;

/**
 * A specialized {@link UseCase} that acts as a router, dispatching requests to an appropriate
 * {@link Handler}.
 *
 * <p>When a request is executed, the dispatcher will iterate through its registered handlers and
 * delegate the request to the first handler that indicates it supports the request. If no
 * supporting handler is found, an exception will be thrown (e.g., UnsupportedHandlerException).
 *
 * @param <UPayload> the type of the request payload
 * @param <UBody> the type of the response body
 */
public interface DispatcherUseCase<UPayload, UBody> extends UseCase<UPayload, UBody> {

  /**
   * Gets the list of currently registered handlers.
   *
   * @return the list of handlers
   */
  List<Handler<UPayload, UBody>> getHandlers();

  /**
   * Registers a single handler to be considered for dispatching requests.
   *
   * @param handler the handler to register
   */
  void addHandler(Handler<UPayload, UBody> handler);

  /**
   * Registers a list of handlers to be considered for dispatching requests.
   *
   * @param handlers the list of handlers to add
   */
  default void addHandlers(List<Handler<UPayload, UBody>> handlers) {
    Validate.notEmpty(handlers, "The handler list cannot be null or empty.");
    handlers.forEach(this::addHandler);
  }

  /**
   * Registers a priority queue of handlers to be considered for dispatching requests.
   *
   * <p>Handlers will be added in the order they are polled from the queue, allowing for prioritized
   * resolution of which handler should process a request.
   *
   * @param handlers the queue of prioritized handlers to add
   */
  default void addHandlers(PriorityQueue<Handler<UPayload, UBody>> handlers) {
    Validate.notEmpty(handlers, "The handler queue cannot be null or empty.");
    while (!handlers.isEmpty()) {
      this.addHandler(handlers.poll());
    }
  }
}
