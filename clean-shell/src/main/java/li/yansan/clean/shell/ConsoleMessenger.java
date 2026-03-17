package li.yansan.clean.shell;

import java.util.HashMap;
import java.util.Map;
import li.yansan.clean.usecase.messaging.Messenger;
import li.yansan.clean.usecase.messaging.MessengerRequest;
import li.yansan.clean.usecase.messaging.MessengerResponse;

public class ConsoleMessenger<UPayload, UBody> implements Messenger<UPayload, UBody> {

  private final Map<MessengerRequest<UPayload>, MessengerResponse<UBody>> store;

  public ConsoleMessenger() {
    this.store = new HashMap<>();
  }

  @Override
  public MessengerResponse<UBody> send(MessengerRequest<UPayload> request) {
    System.out.println("Sending Messenger Request");
    System.out.println(request.toString());
    System.out.println("Messenger Request Sent");
    return this.store.get(request);
  }

  public void put(MessengerRequest<UPayload> request, MessengerResponse<UBody> response) {
    this.store.put(request, response);
  }
}
