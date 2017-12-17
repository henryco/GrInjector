package net.henryco.injector.injector.component;

/**
 * @author Henry on 17/12/17.
 */
public class ComponentD {

	private final String text;
	private final String numb;

	public ComponentD(String text, String numb) {
		this.text = text;
		this.numb = numb;
	}

	@Override
	public String toString() {
		return "ComponentD{" +
				"text='" + text + '\'' +
				", numb='" + numb + '\'' +
				'}';
	}
}