package net.henryco.injector.general;

import net.henryco.injector.GrInjector;
import net.henryco.injector.general.module.ModuleGA;
import org.junit.Test;

/**
 * Created 12/18/2017
 *
 * @author Henry
 */
public class InjectionGeneralTest {


	@Test
	public void testGeneral() {

		GrInjector.addModules(ModuleGA.class);
		SomeControllerA controllerA = new SomeControllerA();
		controllerA.initialize(null, null);
	}

	@Test
	public void testGeneralCustomInjects() {

		GrInjector.addModules(ModuleGA.class);
		SomeControllerB cb1 = new SomeControllerB();
		cb1.initAll();

		SomeControllerB cb2 = new SomeControllerB();
		cb2.initOnceClass();

		SomeControllerB cb3 = new SomeControllerB();
		cb3.initOnceString();
	}
}