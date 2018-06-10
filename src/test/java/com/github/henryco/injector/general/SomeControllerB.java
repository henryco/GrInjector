package com.github.henryco.injector.general;

import com.github.henryco.injector.GrInjector;
import com.github.henryco.injector.general.component.ComponentGA;
import com.github.henryco.injector.general.component.NotAnnotatedComponentB;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created 12/18/2017
 *
 * @author Henry
 */
public class SomeControllerB {

	private static final String VALID_GA_STRING =
			"HELLO ComponentGB: Text value from ModuleGA , 42, NotAnnotatedComponentB{val_s='Text value from ModuleGA', val_i=2323223, val_f=2323223.0}";
	private static final String VALID_NAC_B_STRING =
			"NotAnnotatedComponentB{val_s='Text value from ModuleGA', val_i=2323223, val_f=2323223.0}";

	@Inject
	private ComponentGA componentGA;

	private NotAnnotatedComponentB componentB;

	@Inject
	public void setComponentB(@Named("prv_com_b") NotAnnotatedComponentB componentB) {
		this.componentB = componentB;
	}


	public void initAll() {
		GrInjector.inject(this);

		assert componentGA != null;
		assert componentB != null;
		assert componentGA.hello().equals(VALID_GA_STRING);
		assert componentB.toString().equals(VALID_NAC_B_STRING);
	}

	public void initOnceClass() {

		GrInjector.inject(this, ComponentGA.class);

		assert componentGA != null;
		assert componentB == null;
		assert componentGA.hello().equals(VALID_GA_STRING);
	}

	public void initOnceString() {

		GrInjector.inject(this, "prv_com_b");

		assert componentGA == null;
		assert componentB != null;
		assert componentB.toString().equals(VALID_NAC_B_STRING);
	}

	@Override
	public String toString() {
		return "SomeControllerB{" +
				"componentGA=" + componentGA +
				", componentB=" + componentB +
				'}';
	}
}