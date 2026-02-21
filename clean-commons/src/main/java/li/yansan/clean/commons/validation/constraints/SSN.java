package li.yansan.clean.commons.validation.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import li.yansan.clean.commons.validation.internal.constraintvalidators.SSNValidator;

@Documented
@Constraint(validatedBy = { SSNValidator.class })
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface SSN {

	String message() default "Not valid SSN.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	String pattern() default "^(?!000|666|9\\d{2})\\d{3}-(?!00)\\d{2}-(?!0{4})\\d{4}$";

}
