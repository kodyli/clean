package li.yansan.clean.commons.validation;

import java.util.Set;

import jakarta.validation.Validation;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolationException;

public abstract class Validator {

	private static final ValidatorFactory VALIDATOR_FACTORY;

	private static final jakarta.validation.Validator VALIDATOR;

	static {
		// Create factory once and keep it alive for application lifetime
		// Per Jakarta Validation spec, the factory should not be closed immediately
		VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();
		VALIDATOR = VALIDATOR_FACTORY.getValidator();

		// Register shutdown hook to properly close factory on JVM shutdown
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			if (VALIDATOR_FACTORY != null) {
				VALIDATOR_FACTORY.close();
			}
		}, "ValidatorFactory-Shutdown-Hook"));
	}

	public static <T> void validate(T data) {
		Set<ConstraintViolation<T>> result = VALIDATOR.validate(data);
		if (!result.isEmpty()) {
			throw new ConstraintViolationException(result);
		}
	}

}
