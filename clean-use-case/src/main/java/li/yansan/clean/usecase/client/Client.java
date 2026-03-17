package li.yansan.clean.usecase.client;

import li.yansan.clean.usecase.Actor;

/**
 * Interface representing a client for 3rd party API communication.
 *
 * @param <UPayload> the type of the use case payload
 * @param <UBody> the type of the use case response body
 */
public interface Client<UPayload, UBody> {

  /**
   * Sends a client request and returns a client response.
   *
   * <p>The implementation of {@link #send(ClientRequest)} should follow a structured 3-step
   * pipeline to ensure consistency and maintainability. It is highly recommended to split the logic
   * into three distinct protected methods:
   *
   * <ol>
   *   <li><b>Payload Conversion</b>: {@code convertPayload(Actor, UPayload) -> TI} - Convert the
   *       use case payload into a 3rd party API request format.
   *   <li><b>Processing</b>: {@code execute(TI) -> TO} - Execute the actual API call and receive a
   *       response.
   *   <li><b>Body Conversion</b>: {@code convertBody(TO) -> UBody} - Convert the 3rd party response
   *       back into the use case body.
   * </ol>
   *
   * <p>Example implementation:
   *
   * <pre>{@code
   * @Override
   * public ClientResponse<UBody> send(ClientRequest<UPayload> request) {
   *   TI apiRequest = convertPayload(request.actor(), request.payload());
   *   TO apiResponse = execute(apiRequest);
   *   UBody body = convertBody(apiResponse);
   *   return new ClientResponse<>(body);
   * }
   * }</pre>
   *
   * @param request the client request containing actor and payload
   * @return the client response containing the response body
   */
  ClientResponse<UBody> send(ClientRequest<UPayload> request);

  /**
   * Convenience method to send a request and directly return the response body.
   *
   * @param actor the actor performing the request
   * @param uPayload the use case payload
   * @return the response body
   */
  default UBody send(Actor actor, UPayload uPayload) {
    return send(new ClientRequest<UPayload>(actor, uPayload)).body();
  }
}
