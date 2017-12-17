package net.henryco.injector.injector;

import net.henryco.injector.injector.component.ComponentD;
import net.henryco.injector.injector.module.ModuleA;
import net.henryco.injector.meta.Injector;
import net.henryco.injector.meta.ModuleStruct;
import org.junit.Test;

/**
 * @author Henry on 17/12/17.
 */
public class InjectorTest {

	@Test
	public void providerTest() {

		ModuleStruct moduleStruct = new ModuleStruct(ModuleA.class);
		System.out.println("---");

		ComponentD provide_d = Injector.findOrInstanceByName("provide_D", new ModuleStruct(moduleStruct));
		System.out.println("---");

		System.out.println(provide_d);
	}
}