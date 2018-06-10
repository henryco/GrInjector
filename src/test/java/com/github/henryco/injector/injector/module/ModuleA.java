package com.github.henryco.injector.injector.module;

import com.github.henryco.injector.injector.component.ComponentA;
import com.github.henryco.injector.injector.component.ComponentD;
import com.github.henryco.injector.meta.annotations.Module;
import com.github.henryco.injector.meta.annotations.Provide;

import javax.inject.Named;

/**
 * @author Henry on 17/12/17.
 */
@Module(include = {ModuleB.class, ModuleC.class})
public class ModuleA {


	@Provide("provide_D")
	public ComponentD provideD(ComponentA componentA, @Named("number2") int value) {
		return new ComponentD(componentA.toString(), value + "");
	}

}