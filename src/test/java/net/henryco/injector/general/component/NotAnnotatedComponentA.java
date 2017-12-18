package net.henryco.injector.general.component;

import net.henryco.injector.meta.annotations.Inject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created 12/18/2017
 *
 * @author Henry
 */
public class NotAnnotatedComponentA {

	private String[] array;
	private ArrayList<Integer> intList;

	@Inject
	public NotAnnotatedComponentA(String[] array, ArrayList<Integer> intList) {
		this.array = array;
		this.intList = intList;
	}

	@Override
	public String toString() {
		return "NotAnnotatedComponentA{" +
					   "array=" + Arrays.toString(array) +
					   ", intList=" + intList +
					   '}';
	}
}