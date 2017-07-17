package org.echocat.repo4j.matching;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

@FunctionalInterface
public interface Retriever<T, Q extends Query<T>> {

    /**
     * Find all entities that matches given query.
     */
    @Nonnull
    Stream<? extends T> findBy(@Nonnull Q query);

    /**
     * Find one entity that matches given query.
     */
    @Nonnull
    default Optional<? extends T> findOneBy(@Nonnull Q query) {
        try (final Stream<? extends T> stream = findBy(query)) {
            return stream.findAny();
        }
    }

    /**
     * Count all entities that matches the given query.
     */
    @Nonnegative
    default long countBy(@Nonnull Q query) {
        try (final Stream<? extends T> stream = findBy(query)) {
            return stream.count();
        }
    }

    /**
     * Collect everything and collect it using <code>collector</code>
     * with respecting the resource stream.
     *
     * @see Stream#collect(Collector)
     */
    @Nonnull
    default <R, A> R findBy(@Nonnull Q query, @Nonnull Collector<? super T, A, R> collector) {
        try (final Stream<? extends T> stream = findBy(query)) {
            return stream.collect(collector);
        }
    }

    /**
     * Collect everything and collect it using <code>supplier</code>, <code>accumulator</code> and <code>combiner</code>
     * with respecting the resource stream.
     *
     * @see Stream#collect(Supplier, BiConsumer, BiConsumer)
     */
    @Nonnull
    default <R> R findBy(@Nonnull Q query, Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner) {
        try (final Stream<? extends T> stream = findBy(query)) {
            return stream.collect(supplier, accumulator, combiner);
        }
    }

}
