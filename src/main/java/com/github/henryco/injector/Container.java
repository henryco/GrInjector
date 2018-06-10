package com.github.henryco.injector;

import com.github.henryco.injector.meta.ModuleStruct;
import com.github.henryco.injector.meta.resolver.IClassFinder;

import java.util.HashSet;
import java.util.Set;

/**
 * Created 12/15/2017
 *
 * @author Henry
 */
public final class Container {


	/*package*/ final Set<ModuleStruct> modules;

	Container() {
		modules = new HashSet<>();
	}

	void addModules(IClassFinder classFinder, Class<?> ... modules) {
		for (Class<?> module : modules) {
			if (this.modules.stream().anyMatch(m -> m.getModule().getName().equals(module.getName())))
				continue;
			this.modules.add(new ModuleStruct(module, classFinder));
		}
	}


	void reset() {
		modules.clear();
	}

}