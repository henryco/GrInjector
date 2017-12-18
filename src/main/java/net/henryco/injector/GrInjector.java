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
	}

	private void injectToTarget(Object dest, String... components) {

		for (ModuleStruct module : container.modules) {
			boolean inject = module.inject(dest, components);
			if (inject) return;
		}
	}

	private void injectToTarget(Object dest, Class<?>... components) {
		for (ModuleStruct module : container.modules) {
			boolean inject = module.inject(dest, components);
			if (inject) return;
		}
	}

	private void addRootModules(Class<?>... rootModules) {
		container.addModules(rootModules);
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
}