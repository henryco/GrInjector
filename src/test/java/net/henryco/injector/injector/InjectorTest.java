package net.henryco.injector.injector;

import net.henryco.injector.GrInjector;
import net.henryco.injector.injector.component.ComponentB;
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
		ComponentD provide_d = Injector.findOrInstanceByName("provide_D", new ModuleStruct(moduleStruct));
		assert provide_d != null;
		assert provide_d.toString().equals("ComponentD{text='ComponentA: MODULE_D_TEXT', numb='4'}");
	}


	@Test
	public void provideComponentTest() {

		String valid = "ComponentB{someLong=-20, someFloat=100.0, someString='Strange string', otherString='BANG'}";

		ModuleStruct moduleStruct = new ModuleStruct(ModuleA.class);

		ComponentB componentB = Injector.findOrInstanceByType(ComponentB.class, new ModuleStruct(moduleStruct));
		assert componentB != null;
		assert componentB.toString().equals(valid);

		ComponentB componentB2 = Injector.findOrInstanceByName("SomeComponent", new ModuleStruct(moduleStruct));
		assert componentB2 != null;
		assert componentB2.toString().equals(valid);
	}


	@Test
	public void getComponentHiLvlTest() {

		GrInjector.addModules(ModuleA.class);
		String valid = "ComponentB{someLong=-20, someFloat=100.0, someString='Strange string', otherString='BANG'}";
		ComponentB component = GrInjector.getComponent("SomeComponent");

		assert component != null;
		assert component.toString().equals(valid);

		GrInjector.reset();
		GrInjector.addModules(ModuleA.class);

		ComponentB component2 = GrInjector.getComponent(ComponentB.class);
		assert component2 != null;
		assert component2.toString().equals(valid);
	}
}