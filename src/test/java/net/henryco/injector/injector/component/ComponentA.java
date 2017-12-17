package net.henryco.injector.injector.component;


/**
 * @author Henry on 17/12/17.
 */
public class ComponentA {

	private final String text;

	public ComponentA(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "ComponentA: "+text;
	}
}