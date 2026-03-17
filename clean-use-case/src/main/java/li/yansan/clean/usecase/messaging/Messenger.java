package li.yansan.clean.usecase.messaging;

import li.yansan.clean.usecase.Actor;

/**
 * Interface representing a messenger for notification.
 *
 * @param <UPayload> the type of the use case payload
 * @param <UBody> the type of the use case response body
 */
public interface Messenger<UPayload, UBody> {

  /**
   * Sends a messenger request and returns a messenger response.
   *
   * <p>The implementation of {@link #send(MessengerRequest)} should follow a structured 3-step
   * pipeline to ensure consistency and maintainability. It is highly recommended to split the logic
   * into three distinct protected methods:
   *
   * <ol>
   *   <li><b>Payload Conversion</b>: {@code convertPayload(Actor, UPayload) -> TI} - Convert the
   *       use case payload into a notification request format.
   *   <li><b>Processing</b>: {@code execute(TI) -> TO} - Execute the actual notification sending
   *       and receive a response.
   *   <li><b>Body Conversion</b>: {@code convertBody(TO) -> UBody} - Convert the notification
   *       response back into the use case body.
   * </ol>
   *
   * <p>Example implementation using the Adapter pattern with KafkaTemplate:
   *
   * <pre>{@code
   * public class UserMessenger implements Messenger<UserPayload, UserBody> {
   *   private final KafkaTemplate<String, Object> kafkaTemplate;
   *
   *   public UserMessenger(KafkaTemplate<String, Object> kafkaTemplate) {
   *     this.kafkaTemplate = kafkaTemplate;
   *   }
   *
   *   @Override
   *   public MessengerResponse<UserBody> send(MessengerRequest<UserPayload> request) {
   *     UserEvent event = convertPayload(request.actor(), request.payload());
   *     SendResult<String, Object> result = execute(event);
   *     UserBody body = convertBody(result);
   *     return new MessengerResponse<>(body);
   *   }
   *
   *   protected UserEvent convertPayload(Actor actor, UserPayload payload) {
   *     // Conversion logic using Actor and payload...
   *   }
   *
   *   protected SendResult<String, Object> execute(UserEvent event) {
   *     return kafkaTemplate.send("user-topic", event).get(); // Simplified for example
   *   }
   *
   *   protected UserBody convertBody(SendResult<String, Object> result) {
   *     // Conversion logic using SendResult...
   *   }
   * }
   * }</pre>
   *
   * @param request the messenger request containing actor and payload
   * @return the messenger response containing the response body
   */
  MessengerResponse<UBody> send(MessengerRequest<UPayload> request);

  /**
   * Convenience method to send a request and directly return the response body.
   *
   * @param actor the actor performing the request
   * @param payload the use case payload
   * @return the response body
   */
  default UBody send(Actor actor, UPayload payload) {
    return send(new MessengerRequest<UPayload>(actor, payload)).body();
  }
}
