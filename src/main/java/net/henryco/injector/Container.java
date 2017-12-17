package net.henryco.injector;

import net.henryco.injector.meta.ModuleStruct;
import net.henryco.injector.meta.annotations.Component;

import java.util.*;

/**
 * Created 12/15/2017
 *
 * @author Henry
 */
public class Container {


	/*package*/ final Set<ModuleStruct> modules;


	Container() {
		modules = new HashSet<>();
	}


	void addModules(Class<?> ... modules) {
		for (Class<?> module : modules)
			this.modules.add(new ModuleStruct(module));
	}


}