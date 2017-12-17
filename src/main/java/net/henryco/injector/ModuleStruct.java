package net.henryco.injector;

import net.henryco.injector.meta.Inject;
import net.henryco.injector.meta.Module;
import net.henryco.injector.meta.Provide;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

	public <T> T findOrInstance(String name) {
		return findOrInstance(name, new ModuleStruct(this));
	}


	@Override
	public String toString() {
		return module == null ? null : (module.getSimpleName() + ".class");
	}



	@SuppressWarnings("unchecked")
	private static <T> T findInSingletons(String name, ModuleStruct struct) {

		System.out.println("\nSearch in singletons ");
		for (ModuleStruct moduleStruct : struct.included) {

			System.out.println("Module: " + moduleStruct.module.getSimpleName() + ".class");
			Object o = moduleStruct.singletons.get(name);
			if (o != null) {
				System.out.println("Return\n");
				return (T) moduleStruct.singletons.get(name);
			}
		}
		return null;
	}



	@SuppressWarnings("unchecked")
	private static <T> T findOrInstance(String name, ModuleStruct struct) {


		Object o = findInSingletons(name, struct);
		if (o != null) return (T) o;

		for (ModuleStruct moduleStruct : struct.included) {

			Method[] methods = moduleStruct.module.getDeclaredMethods();
			for (Method method : methods) {

				Provide provide = method.getDeclaredAnnotation(Provide.class);
				if (provide == null)
					continue;



				String compName = provide.value();
				if (compName.trim().isEmpty() || !compName.trim().equals(name))
					continue;


				if (method.getParameterCount() == 0) {
					try {
						return (T) method.invoke(moduleStruct.module.newInstance());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}


				Parameter[] parameters = method.getParameters();
				Object[] args = new Object[parameters.length];

				for (int i = 0; i < args.length; i++) {
					Parameter param = parameters[i];

					Inject inject = param.getAnnotation(Inject.class);
					if (inject != null && !inject.value().trim().isEmpty()) {
						args[i] = findOrInstance(inject.value(), struct);
					}

				}

				try {
					return (T) method.invoke(moduleStruct.module.newInstance(), args);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}

		for (ModuleStruct moduleStruct : struct.included) {

			Object o1 = findOrInstance(name, moduleStruct);
			if (o1 != null) {
				return (T) o1;
			}
		}

		return null;
	}



}