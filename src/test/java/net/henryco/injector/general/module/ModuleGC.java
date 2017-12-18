package net.henryco.injector.general.module;

import net.henryco.injector.general.component.NotAnnotatedComponentA;
import net.henryco.injector.meta.annotations.Inject;
import net.henryco.injector.meta.annotations.Module;
import net.henryco.injector.meta.annotations.Provide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 12/18/2017
 *
 * @author Henry
 */
@Module(include = ModuleGE.class)
public class ModuleGC {


	@Provide("prv_not_com_a")
	public NotAnnotatedComponentA prvCompA(@Inject("strArr") String[] arr,
										   List<Integer> list) {
		return new NotAnnotatedComponentA(arr, (ArrayList<Integer>) list);
	}


	@Provide("strArr")
	public String[] provideStringArray() {
		return new String[]{"1", "2", "3", "4"};
	}

	@Provide
	public List<Integer> provideIntList(@Inject("number_array") String[] arr) {
		ArrayList<Integer> list = new ArrayList<>();
		for (String s : arr) list.add(Integer.parseInt(s));
		return list;
	}

}