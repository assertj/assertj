package org.assertj.core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for an operation that is deemed "unsafe".
 *
 * <p>Any methods with this annotation should be handled with additional care to prevent unwanted
 * results materializing at runtime.
 *
 * @author Ashley Scopes
 */
@Beta
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Unsafe {
}
