package net.henryco.injector.general.module;

import net.henryco.injector.general.SomeControllerA;
import net.henryco.injector.general.SomeControllerB;
import net.henryco.injector.meta.annotations.Inject;
import net.henryco.injector.meta.annotations.TargetInterface;

@TargetInterface
public interface ExampleTargetInterface {

	@Inject void inject(
			SomeControllerA target1,
			SomeControllerB target2
			// .
			// .
			// .
			// Other targets
	);
}