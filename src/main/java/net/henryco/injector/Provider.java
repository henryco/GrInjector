package net.henryco.injector;

import java.util.HashMap;
import java.util.Map;

public final class Provider {
	private static Provider ourInstance = new Provider();
	public static Provider getInstance() {
		return ourInstance;
	}

	private final Map<Class<?>, Map<String, Object>> singletonsMap;


	private Provider() {
		singletonsMap = new HashMap<>();
	}


	public void inject() {

	}

	public void inject(Object dest) {

	}

	public void inject(Class<?> dest) {

	}

	public void addReceivers(Class<?> ... receivers) {
		for (Class<?> receiver : receivers) {
			// TODO
		}
	}

	public void addModules(Class<?> ... modules) {
		for (Class<?> module : modules) {
			// TODO
		}
	}

}