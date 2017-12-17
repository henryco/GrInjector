package net.henryco.injector;

import net.henryco.injector.meta.ModuleStruct;
import net.henryco.injector.meta.annotations.Component;

import java.util.*;

/**
 * Created 12/15/2017
 *
 * @author Henry
 */
public class Container {


	/*package*/ final Set<Class<?>> componentsSet;
	/*package*/ final Set<Class<?>> receiversSet;
	/*package*/ final Set<ModuleStruct> modules;


	Container() {
		componentsSet = new HashSet<>();
		receiversSet = new HashSet<>();
		modules = new HashSet<>();
	}


	void addReceivers(Class<?> ... receivers) {
		receiversSet.addAll(Arrays.asList(receivers));
	}


	void addModules(Class<?> ... modules) {
		for (Class<?> module : modules)
			this.modules.add(new ModuleStruct(module));
	}


	void addComponents(Class<?> ... components) {
		for (Class<?> component : components) {
			if (component.getAnnotation(Component.class) == null)
				throw new RuntimeException("Component must be annotated as @Component");

			componentsSet.add(component);
		}
	}

}