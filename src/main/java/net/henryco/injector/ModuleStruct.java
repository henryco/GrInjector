package net.henryco.injector;

import net.henryco.injector.meta.Inject;
import net.henryco.injector.meta.Module;
import net.henryco.injector.meta.Provide;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * Created 12/15/2017
 *
 * @author Henry
 */
public final class ModuleStruct {

	private final Class<?> module;
	private final Set<ModuleStruct> included;
	private final Map<String, Object> singletons;

	public ModuleStruct(Class<?> module) {

		Module ma = module.getAnnotation(Module.class);
		if (ma == null)
			throw new RuntimeException("Module must be annotated as @Module");

		this.module = module;
		this.included = new HashSet<>();
		this.singletons = new HashMap<>();

		processIncluded(ma);
		processSingletons();
	}

	private ModuleStruct(ModuleStruct other) {
		this.module = null;
		this.singletons = new HashMap<>();
		this.included = new HashSet<ModuleStruct>() {{
			add(other);
		}};
	}

	private void processIncluded(Module m) {
		for (Class<?> icl : m.include())
			included.add(new ModuleStruct(icl));
	}

	private void processSingletons() {
		// TODO
	}



	@Override
	public String toString() {
		return module == null ? null : (module.getSimpleName() + ".class");
	}



	@SuppressWarnings("unchecked")
	public <T> T findOrInstance(String name, Class<?> type) {

		Object o = findOrInstanceByName(name, new ModuleStruct(this));
		if (o != null) return (T) o;

		return findInSingletonsByType(type, new ModuleStruct(this));
	}







	@SuppressWarnings("unchecked")
	private static <T> T findOrInstanceByType(Class<?> type, ModuleStruct struct) {

		T o = findInSingletonsByType(type, struct);
		if (o != null) return o;

		T byMethod = findInMethodsByType(type, struct);
		if (byMethod != null) return byMethod;

		T nested = findInNestedByType(type, struct);
		if (nested != null) return nested;

		return null;
	}


	@SuppressWarnings("unchecked")
	private static <T> T findOrInstanceByName(String name, ModuleStruct struct) {

		T o = findInSingletonsByName(name, struct);
		if (o != null) return o;

		T byMethod = findInMethodsByName(name, struct);
		if (byMethod != null) return byMethod;

		T nested = findInNestedByName(name, struct);
		if (nested != null) return nested;

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
				if (type.getClass().isInstance(value))
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

		if ((name != null && type != null))
			throw new RuntimeException("Cannot find by NAME and TYPE in the same time!");
		if (name == null && type == null)
			throw new RuntimeException("NAME and TYPE cannot be NULL in the same time!");


		for (ModuleStruct moduleStruct : struct.included) {

			Method[] methods = moduleStruct.module.getDeclaredMethods();
			for (Method method : methods) {

				Provide provide = method.getDeclaredAnnotation(Provide.class);
				if (provide == null)
					continue;

				if (name == null) {
					Class<?> returnType = method.getReturnType();
					if (!returnType.equals(type))
						continue;
				}

				if (type == null) {
					String compName = provide.value();
					if (compName.trim().isEmpty() || !compName.trim().equals(name))
						continue;
				}

				try {
					T dependency = instanceDependencyFromMethod(moduleStruct.module.newInstance(), struct, method);
					if (dependency != null)
						return dependency;
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}



	@SuppressWarnings("unchecked")
	private static <T> T instanceDependencyFromMethod
			(Object moduleInstance, ModuleStruct struct, Method method) {

		if (method.getParameterCount() == 0) try {
			return (T) method.invoke(moduleInstance);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Parameter[] parameters = method.getParameters();
		Object[] args = new Object[parameters.length];

		for (int i = 0; i < args.length; i++) {
			Parameter param = parameters[i];

			Inject inject = param.getAnnotation(Inject.class);
			if (inject != null && !inject.value().trim().isEmpty())
				args[i] = findOrInstanceByName(inject.value(), struct);
			else
				args[i] = findOrInstanceByType(param.getType(), struct);
		}

		try {
			return (T) method.invoke(moduleInstance, args);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}


}