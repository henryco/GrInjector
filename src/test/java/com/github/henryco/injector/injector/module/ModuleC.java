package com.github.henryco.injector.injector.module;

import com.github.henryco.injector.meta.annotations.Module;
import com.github.henryco.injector.meta.annotations.Provide;

/**
 * @author Henry on 17/12/17.
 */
@Module(include = ModuleD.class)
public class ModuleC {

	@Provide("number1")
	public int provideNumber1() {
		System.out.println("provideNumber1()");
		return 123;
	}


	@Provide("number2")
	public int provideNumber2() {
		System.out.println("provideNumber2()");
		return 4;
	}


}