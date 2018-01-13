package com.github.henryco.injector.helper;

import com.github.henryco.injector.injector.component.ComponentA;
import com.github.henryco.injector.injector.component.ComponentB;
import com.github.henryco.injector.injector.component.ComponentC;
import com.github.henryco.injector.injector.component.ComponentD;
import com.github.henryco.injector.meta.ClassFinder;
import com.github.henryco.injector.meta.Helper;
import org.junit.Test;

import java.util.ArrayList;

/**
 * @author Henry on 17/12/17.
 */
public class HelperTest {

	interface InterfaceA { }
	interface InterfaceB extends InterfaceA { }
	static class ClassA {
		private String text;
		private String numb;

		public void setNumb(String numb) {
			this.numb = numb;
		}
	}
	static class ClassB extends ClassA implements InterfaceB { }
	private static class ClassC extends ClassB { }

	@Test
	public void superClassTest() {

		assert Helper.checkType(ClassC.class, ClassB.class);
		assert Helper.checkType(ClassC.class, InterfaceA.class);
		assert Helper.checkType(ClassC.class, InterfaceB.class);
		assert Helper.checkType(ClassB.class, InterfaceB.class);
		assert !Helper.checkType(ClassA.class, InterfaceA.class);
		assert Helper.checkType(ClassB.class, InterfaceA.class);
		assert Helper.checkType(ClassB.class, ClassA.class);
		assert Helper.checkType(ClassC.class, ClassA.class);
		assert Helper.checkType(InterfaceB.class, InterfaceA.class);
		assert Helper.checkType(InterfaceA.class, InterfaceA.class);
		assert !Helper.checkType(InterfaceA.class, InterfaceB.class);
		assert !Helper.checkType(InterfaceA.class, ClassA.class);
	}


	@Test
	public void setterNameTest() {

		assert Helper.createSetterName("someValue").equals("setSomeValue");
		assert Helper.createSetterName("abc").equals("setAbc");
		assert Helper.createSetterName("tetris").equals("setTetris");
	}




	@Test
	public void setValueTest() throws NoSuchFieldException {

		ClassA a = new ClassA();
		Helper.setValue(ClassA.class.getDeclaredField("text"), a, "Some Text");
		Helper.setValue(ClassA.class.getDeclaredField("numb"), a, "NUMBER");
		assert a.text.equals("Some Text");
		assert a.numb.equals("NUMBER");
	}


	@Test
	public void packageClassFinderTest() throws ClassNotFoundException {
		String name = ComponentA.class.getPackage().getName();
		ArrayList<Class<?>> list = ClassFinder.getClassesForPackage(name);
		assert list.contains(ComponentA.class);
		assert list.contains(ComponentB.class);
		assert list.contains(ComponentC.class);
		assert list.contains(ComponentD.class);
	}
}