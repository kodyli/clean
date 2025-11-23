package li.yansan.clean.test.internal.rules;

import com.tngtech.archunit.core.domain.JavaClasses;

public interface Rule {

	void check(JavaClasses javaClasses);

}
