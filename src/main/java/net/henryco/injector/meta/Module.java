package net.henryco.injector.meta;

/**
 * Created 12/15/2017
 *
 * @author Henry
 */
public @interface Module {

	Class<?>[] reveiver() default {};

}