package li.yansan.clean.test.internal.rules;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.lang.ArchRule;

public class EncapsulationRule implements Rule {

	@Override
	public void check(JavaClasses javaClasses) {
		getRule().check(javaClasses);
	}

	protected ArchRule getRule() {
		return classes().that()
			.resideInAPackage("..platform..")
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
			.as("Concrete classes in the 'platform' layer should be package-private to enforce encapsulation.");
	}

}
