package com.github.henryco.injector.injector.component;


import com.github.henryco.injector.meta.annotations.Provide;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Henry on 17/12/17.
 */
@Provide("SomeComponent")
public class ComponentB {

	private final Long someLong;
	private final Float someFloat;

	private String someString;

	@Inject
	private String otherString;

	@Inject
	public ComponentB(Long someLong, Float someFloat) {
		this.someLong = someLong;
		this.someFloat = someFloat;
	}

	@Inject
	public void setSomeString(@Named("strange") String someString) {
		this.someString = someString;
	}

	@Override
	public String toString() {
		return "ComponentB{" +
				"someLong=" + someLong +
				", someFloat=" + someFloat +
				", someString='" + someString + '\'' +
				", otherString='" + otherString + '\'' +
				'}';
	}
}