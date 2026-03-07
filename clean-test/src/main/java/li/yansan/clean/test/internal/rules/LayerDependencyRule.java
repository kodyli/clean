package li.yansan.clean.test.internal.rules;

import com.tngtech.archunit.library.Architectures;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.lang.ArchRule;

public class LayerDependencyRule implements Rule {

	public LayerDependencyRule() {
	}

	@Override
	public void check(JavaClasses javaClasses) {
		getRule().check(javaClasses);
	}

	protected ArchRule getRule() {
		return Architectures.layeredArchitecture()
			.consideringOnlyDependenciesInAnyPackage("..")

			// Defines the structure based on package segment names
			.layer("UseCase")
			.definedBy("..usecase..")
			.layer("Platform")
			.definedBy("..adapter..")

			// Specifies allowed dependencies
			.whereLayer("Platform")
			.mayOnlyAccessLayers("UseCase")
			.as("The Dependency Rule must be enforced: UseCase must not depend on Platform.");
	}

}
