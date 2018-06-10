package com.github.henryco.injector.general.component;

/**
 * Created 12/18/2017
 *
 * @author Henry
 */
public class NotAnnotatedComponentB {

	public final String val_s;
	public final int val_i;
	public final float val_f;

	public NotAnnotatedComponentB(String val_s, int val_i, float val_f) {
		this.val_s = val_s;
		this.val_i = val_i;
		this.val_f = val_f;
	}

	@Override
	public String toString() {
		return "NotAnnotatedComponentB{" +
					   "val_s='" + val_s + '\'' +
					   ", val_i=" + val_i +
					   ", val_f=" + val_f +
					   '}';
	}

}