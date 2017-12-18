package net.henryco.injector.general.component;

import net.henryco.injector.meta.annotations.Component;
import net.henryco.injector.meta.annotations.Inject;

/**
 * Created 12/18/2017
 *
 * @author Henry
 */
@Component("ComponentGA")
public class ComponentGA implements IComponent {

	@Inject("component_gb")
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
}