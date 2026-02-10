package li.yansan.clean.application.exception;

/**
 * Base exception for all business rule violations in the Application Layer.
 * <p>
 * This is a {@link RuntimeException} because business rule violations act as domain-level
 * faults that should be handled centrally by the delivery mechanism (e.g., converting to
 * a 400 Bad Request in REST).
 */
public abstract class BusinessException extends RuntimeException {

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

}
