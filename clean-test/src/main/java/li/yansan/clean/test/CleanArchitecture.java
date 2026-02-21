package li.yansan.clean.test;

import java.util.List;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;

import li.yansan.clean.test.internal.rules.EncapsulationRule;
import li.yansan.clean.test.internal.rules.LayerDependencyRule;
import li.yansan.clean.test.internal.rules.Rule;

/**
 * This class contains ArchUnit tests to enforce Clean Architecture rules.
 *
 * <p>
 * It validates two primary things: 1. Layer Dependencies: Dependencies only flow inwards
 * (e.g., Platform -> UseCase). 2. Encapsulation: Concrete adapter implementations are
 * package-private.
 */
class CleanArchitecture {

	private final List<Rule> rules;

	private final JavaClasses classes;

	CleanArchitecture(String rootPackage) {
		// 1. Import all classes from the current project for scanning
		this.classes = new ClassFileImporter().withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
			.importPackages(rootPackage);
		this.rules = List.of(new LayerDependencyRule(), new EncapsulationRule());
	}

	public boolean check() {
		for (Rule rule : this.rules) {
			rule.check(this.classes);
		}
		return true;
	}

}
