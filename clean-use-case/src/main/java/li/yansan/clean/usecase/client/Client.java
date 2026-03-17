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
   * <p>Example implementation using the Adapter pattern with RestTemplate:
   *
   * <pre>{@code
   * public class UserClient implements Client<UserPayload, UserBody> {
   *   private final RestTemplate restTemplate;
   *
   *   public UserClient(RestTemplate restTemplate) {
   *     this.restTemplate = restTemplate;
   *   }
   *
   *   @Override
   *   public ClientResponse<UserBody> send(ClientRequest<UserPayload> request) {
   *     HttpEntity<ApiRequest> apiRequest = convertPayload(request.actor(), request.payload());
   *     ResponseEntity<ApiResponse> apiResponse = execute(apiRequest);
   *     UserBody body = convertBody(apiResponse);
   *     return new ClientResponse<>(body);
   *   }
   *
   *   protected HttpEntity<ApiRequest> convertPayload(Actor actor, UserPayload payload) {
   *     // Conversion logic using Actor and UPayload...
   *   }
   *
   *   protected ResponseEntity<ApiResponse> execute(HttpEntity<ApiRequest> request) {
   *     return restTemplate.postForEntity("/api/users", request, ApiResponse.class);
   *   }
   *
   *   protected UserBody convertBody(ResponseEntity<ApiResponse> response) {
   *     // Conversion logic using apiResponse...
   *   }
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
