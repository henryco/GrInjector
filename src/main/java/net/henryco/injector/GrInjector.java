package net.henryco.injector;

import net.henryco.injector.meta.ModuleStruct;

public final class GrInjector {

	private static GrInjector ourInstance = new GrInjector();

	private final Container container;

	private GrInjector() {
		container = new Container();
	}


	private void injectToTarget(Object dest) {
		for (ModuleStruct module : container.modules) {
			boolean inject = module.inject(dest);
			if (inject) return;
		}
		throw new RuntimeException("There are no dependencies to inject " + dest.getClass());
	}

	private void injectToTarget(Object dest, String... components) {

		for (ModuleStruct module : container.modules) {
			boolean inject = module.inject(dest, components);
			if (inject) return;
		}
		throw new RuntimeException("There are no dependencies to inject " + dest.getClass());
	}

	private void injectToTarget(Object dest, Class<?>... components) {
		for (ModuleStruct module : container.modules) {
			boolean inject = module.inject(dest, components);
			if (inject) return;
		}
		throw new RuntimeException("There are no dependencies to inject " + dest.getClass());
	}

	private <T> T getComponentByName(String name) {
		for (ModuleStruct module : container.modules) {
			T t = module.findOrInstanceByName(name);
			if (t != null) return t;
		}
		return null;
	}

	private <T> T getComponentByType(Class<T> type) {
		for (ModuleStruct module : container.modules) {
			T t = module.findOrInstanceByType(type);
			if (t != null) return t;
		}
		return null;
	}

	private void addRootModules(Class<?>... rootModules) {
		container.addModules(rootModules);
	}

	private void resetModules() {
		container.reset();
	}


	public static void inject(Object dest) {
		ourInstance.injectToTarget(dest);
	}

	public static void inject(Object dest, String ... components) {
		ourInstance.injectToTarget(dest, components);
	}

	public static void inject(Object dest, Class<?> ... components) {
		ourInstance.injectToTarget(dest, components);
	}

	public static void addModules(Class<?> ... rootModules) {
		ourInstance.addRootModules(rootModules);
	}

	public static <T> T getComponent(String name) {
		return ourInstance.getComponentByName(name);
	}

	public static <T> T getComponent(Class<T> cmpClass) {
		return ourInstance.getComponentByType(cmpClass);
	}

	public static void reset() {
		ourInstance.resetModules();
	}
}