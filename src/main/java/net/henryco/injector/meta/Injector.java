package net.henryco.injector.meta;

import net.henryco.injector.meta.annotations.Component;
import net.henryco.injector.meta.annotations.Inject;
import net.henryco.injector.meta.annotations.Provide;
import net.henryco.injector.meta.annotations.Singleton;

import java.lang.reflect.*;
import java.util.Collection;

/**
 * @author Henry on 17/12/17.
 */
public final class Injector {

	private static final class NULL_MARKER {
		private static final NULL_MARKER instance = new NULL_MARKER();
		private NULL_MARKER() {}
	}
	private static final NULL_MARKER NULL = NULL_MARKER.instance;


	@SuppressWarnings("unchecked")
	public static <T> T findOrInstanceByType(Class<?> type, ModuleStruct struct) {

		T o = findInSingletonsByType(type, struct);
		if (o != null) return o;

		T byMethod = findInMethodsByType(type, struct);
		if (byMethod != null) return byMethod;

		T nested = findInNestedByType(type, struct);
		if (nested != null) return nested;

		T component = findInComponentsByType(type, struct);
		if (component != null) return component;

		return null;
	}


	@SuppressWarnings("unchecked")
	public static <T> T findOrInstanceByName(String name, ModuleStruct struct) {

		T o = findInSingletonsByName(name, struct);
		if (o != null) return o;

		T byMethod = findInMethodsByName(name, struct);
		if (byMethod != null) return byMethod;

		T nested = findInNestedByName(name, struct);
		if (nested != null) return nested;

		T component = findInComponentsByName(name, struct);
		if (component != null) return component;

		return null;
	}


	private static <T> T findInComponentsByName(String name, ModuleStruct struct) {
		return findInComponents(name, null, struct);
	}


	private static <T> T findInComponentsByType(Class<?> type, ModuleStruct struct) {
		return findInComponents(null, type, struct);
	}


	@SuppressWarnings("unchecked")
	private static <T> T findInComponents(String name, Class<?> type, ModuleStruct struct) {

		assertNameAndType(name, type);

		for (ModuleStruct moduleStruct : struct.included) {
			for (Class<?> component : moduleStruct.components) {

				Component ca = component.getDeclaredAnnotation(Component.class);

				if (name != null) {
					String compName = ca.value();
					if (compName.trim().isEmpty() || !compName.trim().equals(name))
						continue;
				}

				if (type != null) {
					if (!Helper.checkType(component, type))
						continue;
				}

				T dependency = instanceDependencyFromConstructor(component, struct);
				if (dependency != null) {
					if (component.getDeclaredAnnotation(Singleton.class) != null) {
						String svName = ca.value().isEmpty() ? component.getSimpleName() : ca.value();
						moduleStruct.singletons.put(svName, dependency);
					}
					return dependency;
				}

				return null;
			}
		}

		return null;
	}



	private static <T> T findInNestedByName(String name, ModuleStruct struct) {

		for (ModuleStruct moduleStruct : struct.included) {
			T o = findOrInstanceByName(name, moduleStruct);
			if (o != null) return o;
		}
		return null;
	}

	private static <T> T findInNestedByType(Class<?> type, ModuleStruct struct) {

		for (ModuleStruct moduleStruct : struct.included) {
			T o = findOrInstanceByType(type, moduleStruct);
			if (o != null) return o;
		}
		return null;
	}


	@SuppressWarnings("unchecked")
	private static <T> T findInSingletonsByName(String name, ModuleStruct struct) {

		for (ModuleStruct moduleStruct : struct.included) {
			Object o = moduleStruct.singletons.get(name);
			if (o != null) return (T) moduleStruct.singletons.get(name);
		}
		return null;
	}



	@SuppressWarnings("unchecked")
	private static <T> T findInSingletonsByType(Class<?> type, ModuleStruct struct) {

		if (type == null) return null;

		for (ModuleStruct moduleStruct : struct.included) {
			Collection<Object> values = moduleStruct.singletons.values();
			for (Object value : values) {
				if (Helper.checkType(value.getClass(), type))
					return (T) value;
			}
		}
		return null;
	}



	private static <T> T findInMethodsByName(String name, ModuleStruct struct) {
		return findInMethods(name, null, struct);
	}


	private static <T> T findInMethodsByType(Class<?> type, ModuleStruct struct) {
		return findInMethods(null, type, struct);
	}


	private static <T> T findInMethods(String name, Class<?> type, ModuleStruct struct) {

		assertNameAndType(name, type);

		for (ModuleStruct moduleStruct : struct.included) {

			if (moduleStruct.module == null) continue;

			Method[] methods = moduleStruct.module.getDeclaredMethods();
			for (Method method : methods) {

				Provide provide = method.getDeclaredAnnotation(Provide.class);
				if (provide == null)
					continue;

				if (name == null) {
					if (!Helper.checkType(method.getReturnType(), type))
						continue;
				}

				if (type == null) {
					String compName = provide.value();
					if (compName.trim().isEmpty() || !compName.trim().equals(name))
						continue;
				}

				try {
					T dependency = instanceDependencyFromMethod(moduleStruct.module.newInstance(), struct, method);
					if (dependency != null) {
						if (method.getDeclaredAnnotation(Singleton.class) != null) {
							String svName = provide.value().isEmpty() ? method.getName() : provide.value();
							moduleStruct.singletons.put(svName, dependency);
						}
						return dependency;
					}
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}



	@SuppressWarnings("unchecked")
	private static <T> T instanceDependencyFromConstructor(Class<?> component, ModuleStruct struct) {

		Constructor<?>[] constructors = component.getDeclaredConstructors();
		if (constructors.length == 0)
			throw new RuntimeException("Component must have at least one constructor");

		Constructor<?> constructor = null;

		for (Constructor<?> c : constructors) {
			Inject ia = c.getDeclaredAnnotation(Inject.class);
			if (ia != null) {
				if (constructor != null)
					throw new RuntimeException("Only once constructor can possess @Inject annotation");
				constructor = c;
			}
		}

		if (constructor == null)
			constructor = constructors[0];

		Parameter[] parameters = constructor.getParameters();
		Object[] arguments = instanceDependencyArguments(parameters, struct);

		T instance;

		try {
			instance = (T) constructor.newInstance(arguments);
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException("Cannot instance via constructor", e);
		} catch (IllegalArgumentException ex) {
			throw new RuntimeException("Cannot instance via constructor," +
											   " check providable dependencies", ex);
		}

		return injectDependenciesToInstance(instance, struct);
	}



	public static <T> T injectDependenciesToInstance(T instance,
													 ModuleStruct struct,
													 Object ... componentsToInject) {

		Field[] fields = instance.getClass().getDeclaredFields();
		Object[] fieldValues = instanceDependencyFields(fields, instance.getClass(), struct, componentsToInject);
		for (int i = 0; i < fields.length; i++) {
			if (fieldValues[i] == NULL) continue;
			Helper.setValue(fields[i], instance, fieldValues[i]);
		}

		return instance;
	}


	@SuppressWarnings("unchecked")
	private static <T> T instanceDependencyFromMethod(Object moduleInstance,
													  ModuleStruct struct,
													  Method method) {

		if (method.getParameterCount() == 0) try {
			return (T) method.invoke(moduleInstance);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Parameter[] parameters = method.getParameters();
		Object[] args = instanceDependencyArguments(parameters, struct);

		try {
			return (T) method.invoke(moduleInstance, args);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}


	private static Object[] instanceDependencyFields(Field[] fields,
													 Class<?> instance,
													 ModuleStruct struct,
													 Object ... componentsToInject) {

		Object[] fieldValues = new Object[fields.length];
		for (int i = 0; i < fields.length; i++) {

			Inject inject = fields[i].getDeclaredAnnotation(Inject.class);
			if (inject == null) {

				try {
					String setterName = Helper.createSetterName(fields[i].getName());
					Method declaredMethod = instance.getDeclaredMethod(setterName, fields[i].getType());
					inject = declaredMethod.getDeclaredAnnotation(Inject.class);
				} catch (NoSuchMethodException e) {
					inject = null;
				}

				if (inject == null) {
					fieldValues[i] = NULL;
					continue;
				}
			}

			if (!containsComponent(inject, fields[i], componentsToInject)) {
				fieldValues[i] = NULL;
				continue;
			}

			if (inject.value().trim().isEmpty()) {
				fieldValues[i] = findOrInstanceByType(fields[i].getType(), struct);
			}
			else {
				fieldValues[i] = findOrInstanceByName(inject.value(), struct);
			}
		}

		return fieldValues;
	}


	private static Object[] instanceDependencyArguments(Parameter[] parameters, ModuleStruct struct) {

		Object[] args = new Object[parameters.length];
		for (int i = 0; i < args.length; i++) {
			Parameter param = parameters[i];

			Inject inject = param.getDeclaredAnnotation(Inject.class);
			if (inject != null && !inject.value().trim().isEmpty())
				args[i] = findOrInstanceByName(inject.value(), struct);
			else
				args[i] = findOrInstanceByType(param.getType(), struct);
		}

		return args;
	}


	private static void assertNameAndType(String name, Class<?> type) {
		if ((name != null && type != null))
			throw new RuntimeException("Cannot find by NAME and TYPE in the same time!");
		if (name == null && type == null)
			throw new RuntimeException("NAME and TYPE cannot be NULL in the same time!");
	}


	private static boolean containsComponent(Inject inject, Field field, Object[] componentsToInject) {

		if (componentsToInject.length == 0) return true;

		for (Object cti : componentsToInject) {
			if (cti instanceof String) {
				boolean b = cti.equals(inject.value()) || cti.equals(field.getName());
				if (b) return true;

			} else if (cti instanceof Class<?>) {
				boolean b = Helper.checkType((Class<?>) cti, field.getType());
				if (b) return true;
			}
		}
		return false;
	}

}