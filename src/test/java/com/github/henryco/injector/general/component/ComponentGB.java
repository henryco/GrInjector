package com.github.henryco.injector.general.component;

import com.github.henryco.injector.meta.annotations.Component;
import com.github.henryco.injector.meta.annotations.Inject;

/**
 * Created 12/18/2017
 *
 * @author Henry
 */
@Component("component_gb")
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