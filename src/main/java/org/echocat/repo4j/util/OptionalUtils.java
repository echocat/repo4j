package org.echocat.repo4j.util;

import javax.annotation.Nonnull;
import java.util.Optional;

public class OptionalUtils {

    @Nonnull
    public static <T> T valueOf(@Nonnull Optional<T> of, @Nonnull String name) throws IllegalArgumentException {
        return of.orElseThrow(() ->
            new ArgumentMissingException(name + " not provided.")
        );
    }

    @Nonnull
    public static <T> Optional<T> ofNonnull(T of, @Nonnull String name) throws IllegalArgumentException {
        if (of == null) {
            throw new ArgumentMissingException(name + " not provided.");
        }
        return Optional.of(of);
    }

}
