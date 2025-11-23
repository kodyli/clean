package li.yansan.clean.commons.validation;

import static jakarta.validation.Validation.buildDefaultValidatorFactory;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

public abstract class Validator {

	private static final jakarta.validation.Validator VALIDATOR;

	static {
		try (ValidatorFactory factory = buildDefaultValidatorFactory()) {
			VALIDATOR = factory.getValidator();
		}
	}

	public static <T> void validate(T data) {
		Set<ConstraintViolation<T>> result = VALIDATOR.validate(data);
		if (!result.isEmpty()) {
			throw new ConstraintViolationException(result);
		}
	}

}
