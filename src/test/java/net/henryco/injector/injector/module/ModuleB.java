package net.henryco.injector.injector.module;

import net.henryco.injector.injector.component.ComponentB;
import net.henryco.injector.injector.component.ComponentC;
import net.henryco.injector.meta.annotations.Module;
import net.henryco.injector.meta.annotations.Provide;
import net.henryco.injector.meta.annotations.Singleton;

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