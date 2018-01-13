package com.github.henryco.injector.meta;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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


	public static String createSetterName(String name) {
		char[] chars = name.toCharArray();
		chars[0] = new String(new char[]{chars[0]}).toUpperCase().charAt(0);
		return "set" + new String(chars);
	}


	public static void setValues(Object instance, Field[] fields, Object[] values) {
		for (int i = 0; i < fields.length; i++)
			setValue(fields[i], instance, values[i]);
	}

	public static void setValue(Field field, Object instance, Object value) {


		String name = createSetterName(field.getName());
		try {
			Method setter = instance.getClass().getDeclaredMethod(name, value.getClass());
			setter.invoke(instance, value);
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			try {
				field.set(instance, value);
			} catch (IllegalAccessException e1) {
				field.setAccessible(true);
				try {
					field.set(instance, value);
				} catch (IllegalAccessException e2) {
					e2.printStackTrace();
					throw new RuntimeException("Cannot inject value: "
							+ instance.getClass() + " : "
							+ field.getName() + " : "
							+ value
					);
				}
			}
		}
	}



}