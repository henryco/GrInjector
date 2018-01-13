package com.github.henryco.injector.general.module;

import com.github.henryco.injector.general.SomeControllerA;
import com.github.henryco.injector.general.component.ComponentGA;
import com.github.henryco.injector.general.component.NotAnnotatedComponentB;
import com.github.henryco.injector.meta.annotations.Inject;
import com.github.henryco.injector.meta.annotations.Module;
import com.github.henryco.injector.meta.annotations.Provide;
import com.github.henryco.injector.meta.annotations.Singleton;

/**
 * Created 12/18/2017
 *
 * @author Henry
 */
@Module(include = {
		ModuleGB.class,
		ModuleGC.class
}, componentsRootClass = {
		ComponentGA.class
}, targetsRootClass = {
		SomeControllerA.class
})
public class ModuleGA {


	@Provide("some_text")
	public String someTextValue() {
		return "Text value from ModuleGA";
	}


	@Provide
	public Float someFloatValue() {
		return 123123F;
	}


	@Provide("prv_com_b") @Singleton
	public NotAnnotatedComponentB provide(String someText, @Inject("float_val") Float someFloat) {
		return new NotAnnotatedComponentB(someText, someFloat.intValue(), someFloat);
	}

}