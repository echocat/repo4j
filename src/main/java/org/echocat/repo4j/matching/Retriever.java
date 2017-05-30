package org.echocat.repo4j.matching;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.stream.Stream;

@FunctionalInterface
public interface Retriever<T, Q extends Query<T>> {

    /**
     * Find all entities that matches given query.
     */
    @Nonnull
    Stream<T> findBy(@Nonnull Q query);

    /**
     * Find one entity that matches given query.
     */
    @Nonnull
    default Optional<T> findOneBy(@Nonnull Q query) {
        try (final Stream<T> stream = findBy(query)) {
            return stream.findAny();
        }
    }

    /**
     * Count all entities that matches the given query.
     */
    @Nonnegative
    default long countBy(@Nonnull Q query) {
        try (final Stream<T> stream = findBy(query)) {
            return stream.count();
        }
    }

}
