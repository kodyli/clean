package li.yansan.clean.dispatcure.handler;

import li.yansan.clean.usecase.UseCase;
import li.yansan.clean.usecase.UseCaseRequest;

/**
 * A specialized {@link UseCase} that represents a single, executable unit of work within a larger
 * application architecture. Handlers are typically registered with a {@link DispatcherUseCase} and
 * are responsible for processing specific types of requests.
 *
 * <p>Each handler should implement the {@link #support(UseCaseRequest)} method to indicate whether
 * it can process a given request, and the {@link #execute(UseCaseRequest)} method to perform the
 * actual business logic.
 *
 * @param <UPayload> the type of the request payload
 * @param <UBody> the type of the response body
 */
public interface Handler<UPayload, UBody> extends UseCase<UPayload, UBody> {
  /**
   * Determines whether this handler can process the given request.
   *
   * @param request the request to evaluate
   * @return {@code true} if this handler supports the request, {@code false} otherwise
   */
  boolean support(UseCaseRequest<UPayload> request);
}
