package org.echocat.repo4j.util;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <p>Is used to document an parameter, field, ... which was included in a range.</p>
 *
 * <p>Example: <code>public boolean isInRange({@link Including @Including} int start, {@link Excluding @Excluding} int end)</code></p>
 */
@Documented
@Retention(RUNTIME)
@Target({PARAMETER, LOCAL_VARIABLE, FIELD, METHOD, ANNOTATION_TYPE, TYPE})
public @interface Including {}
