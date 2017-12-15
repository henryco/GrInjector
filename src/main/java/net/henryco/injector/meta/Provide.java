package net.henryco.injector.meta;

import java.lang.annotation.*;

/**
 * Created 12/15/2017
 *
 * @author Henry
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented @Target(ElementType.METHOD)
public @interface Provide {

	/**
	 * Name of provided component
	 */
	String value();
}