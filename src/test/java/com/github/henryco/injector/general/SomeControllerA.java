package com.github.henryco.injector.general;

import com.github.henryco.injector.GrInjector;
import com.github.henryco.injector.general.component.IComponent;
import com.github.henryco.injector.general.component.NotAnnotatedComponentA;
import com.github.henryco.injector.meta.annotations.Inject;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created 12/18/2017
 *
 * @author Henry
 */
public class SomeControllerA implements Initializable{

	private static final String VALID_I_COMPONENT_STRING =
			"HELLO ComponentGB: Text value from ModuleGA , 42, NotAnnotatedComponentB{val_s='Text value from ModuleGA', val_i=2323223, val_f=2323223.0}";
	private static final String VALID_NOT_ANN_COMPONENT_A_STRING =
			"NotAnnotatedComponentA{array=[1, 2, 3, 4], intList=[7, 8, 9, 10]}";

	@Inject("ComponentGA")
	private IComponent component;

	@Inject
	private NotAnnotatedComponentA componentA;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		GrInjector.inject(this);

		assert component.hello().equals(VALID_I_COMPONENT_STRING);
		assert componentA.toString().equals(VALID_NOT_ANN_COMPONENT_A_STRING);
	}
}