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

	}

	public void inject(Object dest) {

	}

	public void inject(Class<?> dest) {

	}

	public void addModules(Class<?> ... modules) {
		container.addModules(modules);
	}

}