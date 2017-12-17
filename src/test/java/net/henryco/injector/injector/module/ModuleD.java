package net.henryco.injector.injector.module;

import net.henryco.injector.meta.annotations.Module;
import net.henryco.injector.meta.annotations.Provide;
import net.henryco.injector.meta.annotations.Singleton;
import net.henryco.injector.injector.component.ComponentA;

/**
 * @author Henry on 17/12/17.
 */
@Module
public class ModuleD {

	@Provide("component_a") @Singleton
	public ComponentA provideComponentA(String someValue) {
		System.out.println("provideComponentA(" + someValue + ")");
		return new ComponentA(someValue);
	}

	@Provide
	public String provideText() {
		System.out.println("provideText()");
		return "MODULE_D_TEXT";
	}

}