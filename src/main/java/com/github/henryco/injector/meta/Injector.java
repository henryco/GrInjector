package com.github.henryco.injector.meta;

import com.github.henryco.injector.meta.annotations.*;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.lang.reflect.*;
import java.util.Collection;
import java.util.logging.Logger;

/**
 * @author Henry on 17/12/17.
 */
public final class Injector {

	private static final Logger logger = Logger.getLogger(Inject.class.getName());

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

				Provide ca = component.getDeclaredAnnotation(Provide.class);

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
		Object[] arguments = instanceDependencyArguments(parameters, struct, component);

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



	/* package */ static <T> T injectDependenciesToInstance(T instance,
													 ModuleStruct struct,
													 Object ... componentsToInject) {
		Field[] fields = instance.getClass().getDeclaredFields();
		Object[] fieldValues = instanceDependencyFields(fields, instance.getClass(), struct, componentsToInject);
		for (int i = 0; i < fields.length; i++) {
			if (fieldValues[i] == NULL) continue;

			if (fieldValues[i] != null)
				Helper.setValue(fields[i], instance, fieldValues[i]);
		}
		return instanceDependencyMethods(instance, struct, componentsToInject);
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
		Object[] args = instanceDependencyArguments(parameters, struct, method);

		try {
			return (T) method.invoke(moduleInstance, args);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private static <T> T instanceDependencyMethods(T instance,
												   ModuleStruct struct,
												   Object ... componentsToInject) {
		for (Method method : instance.getClass().getDeclaredMethods()) {

			Inject inject = method.getDeclaredAnnotation(Inject.class);

			if (inject == null) continue;

			method.setAccessible(true);

			Parameter[] parameters = method.getParameters();
			Object[] args = instanceDependencyArguments(parameters, struct, componentsToInject, componentsToInject);
			if (args == null) continue;

			try {
				method.invoke(instance, args);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
				throw new RuntimeException("Injection to method: " + method.getName() + " FAIL;"
						+ " " + instance + ", " + struct);
			}
		}
		return instance;
	}

	private static Object[] instanceDependencyFields(Field[] fields,
													 Class<?> instance,
													 ModuleStruct struct,
													 Object ... componentsToInject) {
		Object[] fieldValues = new Object[fields.length];
		for (int i = 0; i < fields.length; i++) {

			Inject inject = fields[i].getDeclaredAnnotation(Inject.class);
			if (inject == null) {

				fieldValues[i] = NULL;
				continue;
			}

			Named named = fields[i].getDeclaredAnnotation(Named.class);
			if (!containsComponent(named, fields[i], componentsToInject)) {
				fieldValues[i] = NULL;
				continue;
			}

			if (named != null && !named.value().trim().isEmpty()) {
				fieldValues[i] = findOrInstanceByName(named.value(), struct);
			}
			else {
				fieldValues[i] = findOrInstanceByType(fields[i].getType(), struct);
			}

			if (fieldValues[i] == null) {
				logger.warning("Cannot resolve dependency or dependency == null, "
						+ instance + ", " + fields[i].getName() + ", " + fields[i].getType());
			}
		}

		return fieldValues;
	}


	private static Object[] instanceDependencyArguments(Parameter[] parameters,
														ModuleStruct struct,
														Object optionalInfo,
														Object ... componentsToInject) {

		Object[] args = new Object[parameters.length];
		for (int i = 0; i < args.length; i++) {
			Parameter param = parameters[i];

			Named named = param.getDeclaredAnnotation(Named.class);
			if (componentsToInject != null && componentsToInject.length != 0) {
				if (!containsComponent(named, param, componentsToInject))
					return null;
			}

			if (named != null && !named.value().trim().isEmpty())
				args[i] = findOrInstanceByName(named.value(), struct);
			else
				args[i] = findOrInstanceByType(param.getType(), struct);

			if (args[i] == null) {
				logger.warning("Cannot resolve dependency or dependency == null, "
						+ optionalInfo + ", " + param.getName() + ", " + param.getType());
			}
		}

		return args;
	}


	private static void assertNameAndType(String name, Class<?> type) {
		if ((name != null && type != null))
			throw new RuntimeException("Cannot find by NAME and TYPE in the same time!");
		if (name == null && type == null)
			throw new RuntimeException("NAME and TYPE cannot be NULL in the same time!");
	}

	private static boolean containsComponent(Named named, Parameter parameter, Object[] componentsToInject) {

		if (componentsToInject.length == 0) return true;

		for (Object cti : componentsToInject) {
			if (cti instanceof String) {
				boolean b = named != null && cti.equals(named.value());
				if (b) return true;
			} else if (cti instanceof Class<?>) {
				boolean b = Helper.checkType((Class<?>) cti, parameter.getType());
				if (b) return true;
			}
		}
		return false;
	}

	private static boolean containsComponent(Named named, Field field, Object[] componentsToInject) {

		if (componentsToInject.length == 0) return true;

		for (Object cti : componentsToInject) {
			if (cti instanceof String) {
				boolean b = (named != null && cti.equals(named.value()) ||
						cti.equals(field.getName())
				);
				if (b) return true;
			} else if (cti instanceof Class<?>) {
				boolean b = Helper.checkType((Class<?>) cti, field.getType());
				if (b) return true;
			}
		}
		return false;
	}

}