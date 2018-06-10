package com.github.henryco.injector;

import com.github.henryco.injector.meta.ModuleStruct;
import com.github.henryco.injector.meta.resolver.IClassFinder;

import java.util.Arrays;

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

	@SuppressWarnings("Duplicates")
	private void injectToTarget(Object dest, String... components) {

		for (ModuleStruct module : container.modules) {
			boolean inject = module.inject(dest, components);
			if (inject) return;
		}
		throw new RuntimeException("There are no dependencies to inject " + dest.getClass());
	}

	@SuppressWarnings("Duplicates")
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

	private void addRootModules(IClassFinder classFinder, Class<?>... rootModules) {
		container.addModules(classFinder, rootModules);
	}

	private void resetModules() {
		container.reset();
	}



	/**
	 * @param dest instance inject to
	 */
	public static void inject(Object dest) {
		ourInstance.injectToTarget(dest);
	}

	/**
	 * @param dest instance inject to
	 * @param components desired components names
	 */
	public static void inject(Object dest, String ... components) {
		ourInstance.injectToTarget(dest, components);
	}

	/**
	 * @param dest instance inject to
	 * @param components desired components types
	 */
	public static void inject(Object dest, Class<?> ... components) {
		ourInstance.injectToTarget(dest, components);
	}


	public static void addModules(Class<?> ... rootModules) {
		addModules(null, rootModules);
	}

	public static void addModules(IClassFinder classFinder, Class<?> ... rootModules) {
		ourInstance.addRootModules(classFinder, rootModules);
	}


	public static <T> T getComponent(String name) {
		return ourInstance.getComponentByName(name);
	}

	public static <T> T getComponent(Class<T> cmpClass) {
		return ourInstance.getComponentByType(cmpClass);
	}

	/**
	 * Reset all modules with dependencies
	 */
	public static void reset() {
		ourInstance.resetModules();
	}
}