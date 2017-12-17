package net.henryco.injector.reflect;

import net.henryco.injector.meta.Helper;
import org.junit.Test;

/**
 * @author Henry on 17/12/17.
 */
public class ReflectionTest {

	interface InterfaceA { }
	interface InterfaceB extends InterfaceA { }
	static class ClassA { }
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






}