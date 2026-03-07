package li.yansan.clean.usecase.messaging;

import java.util.HashMap;
import java.util.Map;

public class ConsoleMessenger implements Messenger<Object, Object> {

	private final Map<MessengerRequest<Object>, MessengerResponse<Object>> store;

	public ConsoleMessenger() {
		this.store = new HashMap<>();
	}

	@Override
	public MessengerResponse<Object> send(MessengerRequest<Object> request) {
		System.out.println("Sending Messenger Request");
		System.out.println(request.toString());
		System.out.println("Messenger Request Sent");
		return this.store.get(request);
	}

	public void put(MessengerRequest<Object> request, MessengerResponse<Object> response) {
		this.store.put(request, response);
	}

}
