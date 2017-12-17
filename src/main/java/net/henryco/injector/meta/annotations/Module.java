package net.henryco.injector.meta.annotations;

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
	Class<?>[] components() default {};
}