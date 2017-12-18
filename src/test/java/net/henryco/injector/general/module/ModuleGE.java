package net.henryco.injector.general.module;

import net.henryco.injector.meta.annotations.Module;
import net.henryco.injector.meta.annotations.Provide;

/**
 * Created 12/18/2017
 *
 * @author Henry
 */
@Module
public class ModuleGE {

	@Provide("number_array")
	public String[] numbArray() {
		return new String[]{"7", "8", "9", "10"};
	}

	@Provide("not_number_array")
	public String[] notNumberArray() {
		return new String[]{"a", "b", "c", "d"};
	}
}