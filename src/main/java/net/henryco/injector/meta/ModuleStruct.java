package net.henryco.injector.meta;

import net.henryco.injector.meta.annotations.*;

import java.lang.reflect.*;
import java.util.*;

/**
 * Created 12/15/2017
 * @author Henry
 */
@SuppressWarnings("WeakerAccess")
public final class ModuleStruct {

	/*package*/ final Class<?> module;
	/*package*/ final Map<String, Object> singletons;
	/*package*/ final Set<ModuleStruct> included;
	/*package*/ final Set<Class<?>> components;
	/*package*/ final Set<Class<?>> targets;

	public ModuleStruct(Class<?> module) {

		Module ma = module.getDeclaredAnnotation(Module.class);
		if (ma == null)
			throw new RuntimeException("Module " + module.getName() + " must be annotated as @Module");

		this.module = module;
		this.targets = new HashSet<>();
		this.included = new HashSet<>();
		this.components = new HashSet<>();
		this.singletons = new HashMap<>();

		addComponents(ma);
		addComponentsFromPackage(ma);
		addTargets(ma);
		addTargetsFromPackage(ma);
		addIncluded(ma);

		processSingletons();
	}


	public ModuleStruct(ModuleStruct other) {
		this.module = null;
		this.targets = new HashSet<>();
		this.singletons = new HashMap<>();
		this.components = new HashSet<>();
		this.included = new HashSet<ModuleStruct>() {{
			add(other);
		}};
	}


	public boolean inject(Object target) {
		if (!targets.contains(target.getClass())) return false;

		Injector.injectDependenciesToInstance(target, new ModuleStruct(this));
		return true;
	}

	public boolean inject(Object target, String ... components) {
		if (!targets.contains(target.getClass())) return false;
		Injector.injectDependenciesToInstance(target, new ModuleStruct(this), (Object[]) components);
		return true;
	}

	public boolean inject(Object target, Class<?> ... components) {
		if (!targets.contains(target.getClass())) return false;
		Injector.injectDependenciesToInstance(target, new ModuleStruct(this), (Object[]) components);
		return true;
	}


	private void addIncluded(Module m) {
		for (Class<?> icl : m.include()) {
			ModuleStruct struct = new ModuleStruct(icl);
			included.add(struct);
		}
	}


	private void addComponents(Module m) {

		for (Class<?> component : m.components()) {
			Component c = component.getDeclaredAnnotation(Component.class);
			if (c == null) throw new RuntimeException("Component must be annotated as @Component");
			components.add(component);
		}
	}

	private void addComponentsFromPackage(Module m) {
		for (String path : m.componentsRootPath())
			processComponentPackagePath(path);
		for (Class<?> cClass : m.componentsRootClass())
			processComponentPackagePath(cClass.getPackage().getName());
	}



	private void addTarget(Class<?> target) {

		TargetInterface tia = target.getDeclaredAnnotation(TargetInterface.class);
		Module a = target.getDeclaredAnnotation(Module.class);
		Component b = target.getDeclaredAnnotation(Component.class);
		if (a != null || b != null) return;

		if (tia == null) {
			this.targets.add(target);
			return;
		}

		if (!target.isInterface())
			throw new RuntimeException("@TargetInterface annotation acceptable only for interfaces");

		for (Method method : target.getDeclaredMethods()) {
			if (method.getDeclaredAnnotation(Inject.class) == null)
				continue;
			if (method.getParameterCount() != 1)
				throw new RuntimeException("Injection target method must contain only one argument");
			this.targets.add(method.getParameters()[0].getType());
		}
	}


	private void addTargets(Module m) {
		for (Class<?> target : m.targets())
			addTarget(target);
	}

	private void addTargetsFromPackage(Module m) {
		for (String path : m.targetsRootPath())
			processTargetPackagePath(path);
		for (Class<?> aClass : m.targetsRootClass())
			processTargetPackagePath(aClass.getPackage().getName());
	}


	private void processTargetPackagePath(String path) {

		try {
			ArrayList<Class<?>> classes = ClassFinder.getClassesForPackage(path);
			for (Class<?> target : classes) {
				addTarget(target);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}


	private void processComponentPackagePath(String path) {

		try {
			ArrayList<Class<?>> classes = ClassFinder.getClassesForPackage(path);
			for (Class<?> component : classes) {
				Component c = component.getDeclaredAnnotation(Component.class);
				if (c == null) continue;
				components.add(component);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}


	private void processSingletons() {

		Method[] methods = module.getDeclaredMethods();

		for (Method method : methods) {
			if (method.getDeclaredAnnotation(Singleton.class) == null) continue;

			Provide provide = method.getDeclaredAnnotation(Provide.class);
			if (provide == null) continue;
			String name = provide.value().isEmpty() ? method.getName() : provide.value();

			if (singletons.containsKey(name)) continue;
			Object instance = findOrInstance(name, method.getReturnType());
			singletons.put(name, instance);
		}

		for (Class<?> component : components) {
			if (component.getDeclaredAnnotation(Singleton.class) == null) continue;

			Component c = component.getDeclaredAnnotation(Component.class);
			if (c == null) continue;
			String name = c.value().isEmpty() ? component.getSimpleName() : c.value();

			if (singletons.containsKey(name)) continue;
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