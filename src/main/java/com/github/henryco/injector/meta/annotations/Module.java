package com.github.henryco.injector.meta.annotations;

import java.lang.annotation.*;

/**
 * Created 12/15/2017
 *
 * @author Henry
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Module {

	Class<?>[] include() default {};

	Class<?>[] targets() default {};
	Class<?>[] targetsRootClass() default {};
	String[] targetsRootPath() default {};

	Class<?>[] components() default {};
	Class<?>[] componentsRootClass() default {};
	String[] componentsRootPath() default {};
}