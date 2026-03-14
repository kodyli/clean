package li.yansan.clean.test.internal.rules;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

public class EncapsulationRule implements Rule {

  @Override
  public void check(JavaClasses javaClasses) {
    getRule().check(javaClasses);
  }

  protected ArchRule getRule() {
    return ArchRuleDefinition.classes()
        .that()
        .resideInAPackage("..adapter..")
        .and()
        .areTopLevelClasses()
        .and()
        .areNotInterfaces()
        .and()
        .areNotEnums()
        .and()
        .areNotRecords()
        .should()
        .bePackagePrivate()
        .as(
            "Concrete classes in the 'adapter' layer should be package-private to enforce encapsulation.");
  }
}
