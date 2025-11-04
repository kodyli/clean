package li.yansan.clean.commons.validation.internal.constraintvalidators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;
import li.yansan.clean.commons.validation.constraints.SSN;

public class SSNValidator implements ConstraintValidator<SSN, CharSequence> {
  private Pattern pattern;

  public void initialize(SSN ssn) {
    this.pattern = Pattern.compile(ssn.pattern());
  }

  @Override
  public boolean isValid(
      CharSequence value, ConstraintValidatorContext constraintValidatorContext) {
    if (value == null || value.isEmpty() || value.toString().isBlank()) {
      return true;
    }
    return pattern.matcher(value).matches();
  }
}
