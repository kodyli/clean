package li.yansan.clean.usecase.handler;

import java.util.List;
import java.util.stream.Collectors;

public class UnsupportedHandlerException extends UnsupportedOperationException {

	private static final String DEFAULT_MSG = "No suitable handler found to process the given request.";

	public UnsupportedHandlerException() {
		super(DEFAULT_MSG);
	}

	public UnsupportedHandlerException(Object request, List<?> registeredHandlers) {
		super(buildMessage(request, registeredHandlers));
	}

	private static String buildMessage(Object request, List<?> handlers) {
		String requestType = (request != null) ? request.getClass().getSimpleName() : "null";

		// List the names of all handlers currently in the dispatcher
		String handlerNames = handlers.isEmpty() ? "None"
				: handlers.stream().map(h -> h.getClass().getSimpleName()).collect(Collectors.joining(", "));

		return String.format("No compatible handler found for Request Type [%s]. %d handlers were checked: [%s]",
				requestType, handlers.size(), handlerNames);
	}

}
