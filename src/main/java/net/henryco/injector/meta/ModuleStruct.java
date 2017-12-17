package net.henryco.injector.meta;

import net.henryco.injector.meta.annotations.*;

import java.lang.reflect.*;
import java.util.*;

/**
 * Created 12/15/2017
 *
 * @author Henry
 */
public final class ModuleStruct {

	/*package*/ final Class<?> module;
	/*package*/ final Map<String, Object> singletons;
	/*package*/ final Set<ModuleStruct> included;
	/*package*/ final Set<Class<?>> components;

	public ModuleStruct(Class<?> module) {

		Module ma = module.getDeclaredAnnotation(Module.class);
		if (ma == null)
			throw new RuntimeException("Module " + module.getName() + " must be annotated as @Module");

		this.module = module;
		this.included = new HashSet<>();
		this.components = new HashSet<>();
		this.singletons = new HashMap<>();

		addIncluded(ma);
		addComponents(ma);
		processSingletons();
	}


	public ModuleStruct(ModuleStruct other) {
		this.module = null;
		this.singletons = new HashMap<>();
		this.components = new HashSet<>();
		this.included = new HashSet<ModuleStruct>() {{
			add(other);
		}};
	}


	private void addIncluded(Module m) {
		for (Class<?> icl : m.include())
			included.add(new ModuleStruct(icl));
	}


	private void addComponents(Module m) {

		for (Class<?> component : m.components()) {
			Component c = component.getDeclaredAnnotation(Component.class);
			if (c == null) throw new RuntimeException("Component must be annotated as @Component");
			components.add(component);
		}

	}


	private void processSingletons() {

		Method[] methods = module.getDeclaredMethods();

		for (Method method : methods) {
			if (method.getDeclaredAnnotation(Singleton.class) == null) continue;

			Provide provide = method.getDeclaredAnnotation(Provide.class);
			if (provide == null) continue;
			String name = provide.value().isEmpty() ? method.getName() : provide.value();

			Object instance = findOrInstance(name, method.getReturnType());
			singletons.put(name, instance);
		}

		for (Class<?> component : components) {
			if (component.getDeclaredAnnotation(Singleton.class) == null) continue;

			Component c = component.getDeclaredAnnotation(Component.class);
			if (c == null) continue;
			String name = c.value().isEmpty() ? component.getSimpleName() : c.value();

			Object instance = findOrInstance(name, component);
			singletons.put(name, instance);
		}
	}



	@Override
	public String toString() {
		return module == null ? "ROOT_MODULE" : (module.getSimpleName() + ".class");
	}



	@SuppressWarnings("unchecked")
	private <T> T findOrInstance(String name, Class<?> type) {

		Object o = Injector.findOrInstanceByName(name, new ModuleStruct(this));
		if (o != null) return (T) o;

		return Injector.findOrInstanceByType(type, new ModuleStruct(this));
	}


}