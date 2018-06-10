package com.github.henryco.injector.injector.module;

import com.github.henryco.injector.injector.component.ComponentB;
import com.github.henryco.injector.injector.component.ComponentC;
import com.github.henryco.injector.meta.annotations.Module;
import com.github.henryco.injector.meta.annotations.Provide;

import javax.inject.Singleton;

/**
 * @author Henry on 17/12/17.
 */
@Module(components = {
		ComponentC.class,
		ComponentB.class
}, include = ModuleB.NestedModule.class)
public class ModuleB {

	@Provide
	public Float provideFloat() {
		return 100F;
	}

	@Provide
	public Long provideLong() {
		return -20L;
	}

	@Provide
	public String provideString() {
		return "BANG";
	}


	@Module
	public static final class NestedModule {

		@Provide("strange") @Singleton
		public String nestedString() {
			return "Strange string";
		}
	}

}