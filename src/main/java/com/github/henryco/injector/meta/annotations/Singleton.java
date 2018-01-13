package com.github.henryco.injector.meta.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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