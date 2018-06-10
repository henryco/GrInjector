package com.github.henryco.injector.general.component;


import com.github.henryco.injector.meta.annotations.Provide;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created 12/18/2017
 *
 * @author Henry
 */
@Provide("ComponentGA")
public class ComponentGA implements IComponent {

	@Inject
	@Named("component_gb")
	private IComponent component;

	private NotAnnotatedComponentB componentB;

	@Override
	public String hello() {

		String val = "";

		if (component != null)
			val += component.hello() + ", ";

		if (componentB != null)
			val += componentB.toString();

		return val;
	}

	@Inject
	public void setComponentB(NotAnnotatedComponentB componentB) {
		this.componentB = componentB;
	}

	@Override
	public String toString() {
		return "ComponentGA{" +
				"component=" + component +
				", componentB=" + componentB +
				'}';
	}
}