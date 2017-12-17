package net.henryco.injector;

public final class Provider {

	private static Provider ourInstance = new Provider();
	public static Provider get() {
		return ourInstance;
	}

	private final Container container;

	private Provider() {
		container = new Container();
	}


	public void inject() {
		for (Class<?> target : container.targets) {

		}
	}

	public void inject(Object dest) {

	}

	public void inject(Class<?> dest) {

	}

	public void addModules(Class<?> ... modules) {
		container.addModules(modules);
	}

	public void addTargets(Class<?> ... targets) {
		container.addTargets(targets);
	}
}