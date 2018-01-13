package com.github.henryco.injector.injector.module;

import com.github.henryco.injector.injector.component.ComponentA;
import com.github.henryco.injector.injector.component.ComponentD;
import com.github.henryco.injector.meta.annotations.Inject;
import com.github.henryco.injector.meta.annotations.Module;
import com.github.henryco.injector.meta.annotations.Provide;

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