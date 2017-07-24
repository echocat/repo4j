package org.echocat.repo4j.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.lang.Character.isUpperCase;
import static java.lang.Character.toLowerCase;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static org.echocat.repo4j.util.Tuple.tupleOf;

public class ReflectionUtils {

    protected static final Collection<String> GET_PREFIXES = unmodifiableList(asList(
        "get",
        "is",
        "has"
    ));
    private static final Map<Class<?>, Map<String, Method>> TYPE_TO_PROPERTY_METHOD_MAP = synchronizedMap(new WeakHashMap<>());

    @Nonnull
    public static String toString(@Nullable Object input, @Nullable Predicate<Tuple<String, Object>> shouldUse) {
        final StringBuilder sb = new StringBuilder();
        toString(input, sb, shouldUse);
        return sb.toString();
    }

    public static void toString(@Nullable Object input, @Nonnull StringBuilder target, @Nullable Predicate<Tuple<String, Object>> shouldUse) {
        final AtomicBoolean first = new AtomicBoolean(true);
        retrievePropertiesOf(input)
            .filter(candidate -> shouldUse == null || shouldUse.test(candidate))
            .forEach(tuple -> {
                if (!first.getAndSet(false)) {
                    target.append(", ");
                }
                target.append(tuple.left()).append("=");
                final Object value = tuple.right();
                if (value == null) {
                    target.append("null");
                } else if (value instanceof CharSequence) {
                    target.append('\'').append(value).append('\'');
                } else {
                    target.append(value);
                }
            });
    }

    @Nonnull
    public static Stream<Tuple<String, Object>> retrievePropertiesOf(@Nullable Object input) {
        if (input == null) {
            return Stream.empty();
        }
        return retrievePropertyNameToMethodFor(input.getClass()).entrySet().stream()
            .map(entry -> tupleOf(entry.getKey(), retrieveValueOf(entry.getValue(), input)))
            ;
    }

    @Nonnull
    protected static Object retrieveValueOf(@Nonnull Method method, @Nonnull Object target) {
        try {
            return method.invoke(target);
        } catch (final IllegalAccessException e) {
            throw new UndeclaredThrowableException(e);
        } catch (final InvocationTargetException e) {
            final Throwable throwable = e.getTargetException();
            if (throwable instanceof RuntimeException) {
                //noinspection ThrowInsideCatchBlockWhichIgnoresCaughtException
                throw (RuntimeException) throwable;
            }
            if (throwable instanceof Error) {
                //noinspection ThrowInsideCatchBlockWhichIgnoresCaughtException
                throw (Error) throwable;
            }
            //noinspection ThrowInsideCatchBlockWhichIgnoresCaughtException
            throw new UndeclaredThrowableException(throwable);
        }
    }

    @Nonnull
    public static Map<String, Method> retrievePropertyNameToMethodFor(@Nonnull Class<?> type) {
        return TYPE_TO_PROPERTY_METHOD_MAP.computeIfAbsent(type, ReflectionUtils::retrievePropertyNameToMethodInternalFor);
    }

    @Nonnull
    protected static Map<String, Method> retrievePropertyNameToMethodInternalFor(@Nonnull Class<?> type) {
        final Map<String, Method> result = new TreeMap<>();
        for (final Method candidate : type.getMethods()) {
            if (candidate.getParameterCount() == 0) {
                final Class<?> returnType = candidate.getReturnType();
                if (returnType != null && !Void.class.equals(returnType)) {
                    if (!isStatic(candidate.getModifiers())) {
                        if (!Object.class.equals(candidate.getDeclaringClass())) {
                            propertyNameOf(candidate).ifPresent(propertyName -> result.put(propertyName, candidate));
                        }
                    }
                }
            }
        }
        return unmodifiableMap(result);
    }

    @Nonnull
    protected static Optional<String> propertyNameOf(@Nonnull Method method) {
        return toPropertyName(method.getName());
    }

    @Nonnull
    public static Optional<String> toPropertyName(@Nonnull String input) {
        if ("toString".equals(input) || "hashCode".equals(input)) {
            return Optional.empty();
        }
        for (final String prefix : GET_PREFIXES) {
            final Optional<String> candidate = toPropertyName(input, prefix);
            if (candidate.isPresent()) {
                return candidate;
            }
        }
        return Optional.of(input);
    }

    @Nonnull
    protected static Optional<String> toPropertyName(@Nonnull String input, @Nonnull String ifPrefix) {
        final int prefixLength = ifPrefix.length();
        if (input.startsWith(ifPrefix) && input.length() > prefixLength) {
            final char[] chars = input.toCharArray();
            if (isUpperCase(chars[prefixLength])) {
                chars[prefixLength] = toLowerCase(chars[prefixLength]);
                return Optional.of(new String(chars, prefixLength, chars.length - prefixLength));
            }
        }
        return Optional.empty();
    }

}
