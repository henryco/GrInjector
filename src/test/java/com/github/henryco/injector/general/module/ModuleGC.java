package com.github.henryco.injector.general.module;

import com.github.henryco.injector.general.component.NotAnnotatedComponentA;
import com.github.henryco.injector.meta.annotations.Module;
import com.github.henryco.injector.meta.annotations.Provide;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created 12/18/2017
 * @author Henry
 */
@Module(include = ModuleGE.class)
public class ModuleGC {


	@Provide("prv_not_com_a")
	public NotAnnotatedComponentA prvCompA(@Named("strArr") String[] arr,
										   List<Integer> list) {
		return new NotAnnotatedComponentA(arr, (ArrayList<Integer>) list);
	}


	@Provide("strArr")
	public String[] provideStringArray() {
		return new String[]{"1", "2", "3", "4"};
	}

	@Provide
	public List<Integer> provideIntList(@Named("number_array") String[] arr) {
		ArrayList<Integer> list = new ArrayList<>();
		for (String s : arr) list.add(Integer.parseInt(s));
		return list;
	}

	@Provide @Singleton
	public float provideFloatB(Scanner scanner) {
		return 23;
	}

}