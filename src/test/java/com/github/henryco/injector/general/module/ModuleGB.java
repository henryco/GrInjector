package com.github.henryco.injector.general.module;

import com.github.henryco.injector.meta.annotations.Module;
import com.github.henryco.injector.meta.annotations.Provide;

/**
 * Created 12/18/2017
 *
 * @author Henry
 */
@Module(include = ModuleGD.class)
public class ModuleGB {


	@Provide("float_val")
	public Float provideFloat() {
		return 2323223F;
	}

}