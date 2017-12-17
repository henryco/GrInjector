package net.henryco.injector.meta.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created 12/15/2017
 *
 * @author Henry
 */
@Documented
@Retention(RUNTIME)
@Target({FIELD, METHOD, CONSTRUCTOR, PARAMETER})
public @interface Inject {

	/**
	 * Name of injectable component
	 */
	String value() default "";
}