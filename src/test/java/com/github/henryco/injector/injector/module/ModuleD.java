package com.github.henryco.injector.injector.module;

import com.github.henryco.injector.injector.component.ComponentA;
import com.github.henryco.injector.meta.annotations.Module;
import com.github.henryco.injector.meta.annotations.Provide;
import com.github.henryco.injector.meta.annotations.Singleton;

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