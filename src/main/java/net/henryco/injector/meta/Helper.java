package net.henryco.injector.meta;

/**
 * @author Henry on 17/12/17.
 */
public final class Helper {

	private Helper() {}


	/**
	 * Check if class equals or extends / implements other class
	 */
	public static boolean checkType(Class<?> toCheck, Class<?> type) {

		Class<?> superClass = toCheck;
		while (superClass != null) {

			if (superClass.getTypeName().equals(type.getTypeName()))
				return true;

			if (checkInterfaces(superClass, type))
				return true;

			superClass = superClass.getSuperclass();
		}

		return false;
	}

	public static boolean checkInterfaces(Class<?> inter, Class<?> type) {

		Class<?>[] interfaces = inter.getInterfaces();
		for (Class<?> anInterface : interfaces) {

			if (anInterface.getTypeName().equals(type.getTypeName()))
				return true;

			if (checkInterfaces(anInterface, type))
				return true;
		}
		return false;
	}
}