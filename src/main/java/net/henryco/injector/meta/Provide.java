package net.henryco.injector.meta;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;

/**
 * Created 12/15/2017
 *
 * @author Henry
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented @Target({METHOD, ANNOTATION_TYPE})
public @interface Provide {

	/**
	 * Name of provided component
	 */
	String value();

	final class Helper {

		public static Provide getProvideAnnotation(Annotation annotation) {
			return annotation.annotationType().getAnnotation(Provide.class);
		}

		public static Provide getProvideAnnotation(Class<?> aClass) {
			for (Annotation annotation : aClass.getAnnotations()) {
				Provide provide = getProvideAnnotation(annotation);
				if (provide != null) return provide;
			}
			return null;
		}
	}
}