package com.github.henryco.injector.general.component;


import com.github.henryco.injector.meta.annotations.Provide;

import javax.inject.Inject;

/**
 * Created 12/18/2017
 *
 * @author Henry
 */
@Provide("component_gb")
public class ComponentGB implements IComponent {

	private final String textToSay;
	private final int value;

	@Inject
	public ComponentGB(String textToSay, int value) {
		this.textToSay = textToSay;
		this.value = value;
	}

	@Override
	public String hello() {
		return "HELLO ComponentGB: " + textToSay + " , " + value;
	}

}