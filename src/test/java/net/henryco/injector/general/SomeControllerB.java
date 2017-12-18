package net.henryco.injector.general;

import net.henryco.injector.GrInjector;
import net.henryco.injector.general.component.ComponentGA;
import net.henryco.injector.general.component.NotAnnotatedComponentB;
import net.henryco.injector.meta.annotations.Inject;

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

	@Inject("prv_com_b")
	public void setComponentB(NotAnnotatedComponentB componentB) {
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
}