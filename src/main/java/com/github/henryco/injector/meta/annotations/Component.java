package com.github.henryco.injector.meta.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Created 12/15/2017
 *
 * @author Henry
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented @Target({TYPE, FIELD, METHOD})
public @interface Component {

	/**
	 * Name of component
	 * */
	String value() default "";

}