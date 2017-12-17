package net.henryco.injector.injector.module;

import net.henryco.injector.meta.annotations.Inject;
import net.henryco.injector.meta.annotations.Module;
import net.henryco.injector.meta.annotations.Provide;
import net.henryco.injector.injector.component.ComponentA;
import net.henryco.injector.injector.component.ComponentD;

/**
 * @author Henry on 17/12/17.
 */
@Module(include = {ModuleB.class, ModuleC.class})
public class ModuleA {


	@Provide("provide_D")
	public ComponentD provideD(ComponentA componentA, @Inject("number2") int value) {
		System.out.println("provideD(" + componentA + ", " + value +")");
		return new ComponentD(componentA.toString(), value + "");
	}

}