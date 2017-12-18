package net.henryco.injector.general.module;

import net.henryco.injector.meta.annotations.Module;
import net.henryco.injector.meta.annotations.Provide;


/**
 * Created 12/18/2017
 *
 * @author Henry
 */
@Module
public class ModuleGD {

	@Provide
	public int provideRandomInt() {
		return 42;
	}
}