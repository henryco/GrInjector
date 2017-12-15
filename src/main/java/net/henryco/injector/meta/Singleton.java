package net.henryco.injector.meta;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

/**
 * Created 12/15/2017
 *
 * @author Henry
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented @Target({METHOD, TYPE})
public @interface Singleton {

}