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
	private static <T> T findOrInstance(String name, ModuleStruct struct) {

//		System.out.println("\n\n\n------");
//		System.out.println("findOrInstance(name: " + name +", parent: "+struct+")");

//		System.out.println("\nSearch in singletons ");
		for (ModuleStruct moduleStruct : struct.included) {

//			System.out.println("Module: " + moduleStruct.module.getSimpleName() + ".class");
			Object o = moduleStruct.singletons.get(name);
			if (o != null) {
//				System.out.println("Return\n");
				return (T) o;
			}
		}

//		System.out.println("\nSearch in methods: ");
		for (ModuleStruct moduleStruct : struct.included) {

//			System.out.println("Module: " + moduleStruct.module.getSimpleName() + ".class");

			Method[] methods = moduleStruct.module.getDeclaredMethods();
			for (Method method : methods) {
//				System.out.println("Method: " + method.getName());

				Provide provide = method.getDeclaredAnnotation(Provide.class);
				if (provide == null)
					continue;


//				System.out.println("Provide(name: "+provide.value()+")");


				String compName = provide.value();
				if (compName.trim().isEmpty() || !compName.trim().equals(name))
					continue;

//				System.out.println("CompName: " + compName);

				if (method.getParameterCount() == 0) {
//					System.out.println("Param count: 0");
					try {
//						System.out.println("Return\n");
						return (T) method.invoke(moduleStruct.module.newInstance());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}


				Parameter[] parameters = method.getParameters();
				Object[] args = new Object[parameters.length];

//				System.out.println("\nProcess method parameters");
				for (int i = 0; i < args.length; i++) {
					Parameter param = parameters[i];

//					System.out.println("Param: " + param);

					Inject inject = param.getAnnotation(Inject.class);
					if (inject != null && !inject.value().trim().isEmpty()) {
//						System.out.println("Param Inject(" + inject.value() + ")");
						args[i] = findOrInstance(inject.value(), struct);
						continue;
					}

//					System.out.println("Param name: " + param.getName());
					args[i] = findOrInstance(param.getName(), struct);
				}

				try {
//					System.out.println("\nDependency instance(name: " + name + ")");
//					for (int k = 0; k < args.length; k++) System.out.println("Param["+k+"]: " + args[k]);
//					System.out.println("Return\n");
					return (T) method.invoke(moduleStruct.module.newInstance(), args);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}

//		System.out.println("\nSearch nested");
		for (ModuleStruct moduleStruct : struct.included) {

//			System.out.println("Module: " + moduleStruct.module.getSimpleName() + ".class");
			Object o1 = findOrInstance(name, moduleStruct);
			if (o1 != null) {
//				System.out.println("Return\n");
				return (T) o1;
			}
		}

//		System.out.println("Return\n");
//		System.out.println("------");
		return null;
	}



}