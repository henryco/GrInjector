package net.henryco.injector.injector.component;

import net.henryco.injector.meta.annotations.Component;
import net.henryco.injector.meta.annotations.Inject;

/**
 * @author Henry on 17/12/17.
 */
@Component("SomeComponent")
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

	@Inject("strange")
	public void setSomeString(String someString) {
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