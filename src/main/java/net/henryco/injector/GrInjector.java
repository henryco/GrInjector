package net.henryco.injector;

import net.henryco.injector.meta.ModuleStruct;

public final class GrInjector {

	private static GrInjector ourInstance = new GrInjector();
	public static GrInjector get() {
		return ourInstance;
	}

	private final Container container;

	private GrInjector() {
		container = new Container();
	}



	public void inject(Object dest, String ... components) {

		for (ModuleStruct module : container.modules) {
			boolean inject = module.inject(dest, components);
			if (inject) return;
		}
	}

	public void inject(Object dest, Class<?> ... components) {
		for (ModuleStruct module : container.modules) {
			boolean inject = module.inject(dest, components);
			if (inject) return;
		}
	}


	public void addModules(Class<?> ... modules) {
		container.addModules(modules);
	}

}