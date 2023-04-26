package org.echocat.repo4j.range;

import org.echocat.repo4j.util.Excluding;
import org.echocat.repo4j.util.Including;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import static java.util.Optional.ofNullable;

public interface Range<V> extends Predicate<V> {

    @Nonnull
    @Including
    Optional<V> from();

    @Nonnull
    @Excluding
    Optional<V> to();

    @Nonnull
    static Range<Boolean> rangeOf(@Nullable @Including Boolean from, @Nullable @Excluding Boolean to) {
        return new Base<>(from, to, Boolean::compare);
    }

    @Nonnull
    static Range<Character> rangeOf(@Nullable @Including Character from, @Nullable @Excluding Character to) {
        return new Base<>(from, to, Character::compare);
    }

    @Nonnull
    static Range<Short> rangeOf(@Nullable @Including Short from, @Nullable @Excluding Short to) {
        return new Base<>(from, to, Short::compare);
    }

    @Nonnull
    static Range<Integer> rangeOf(@Nullable @Including Integer from, @Nullable @Excluding Integer to) {
        return new Base<>(from, to, Integer::compare);
    }

    @Nonnull
    static Range<Long> rangeOf(@Nullable @Including Long from, @Nullable @Excluding Long to) {
        return new Base<>(from, to, Long::compare);
    }

    @Nonnull
    static Range<Float> rangeOf(@Nullable @Including Float from, @Nullable @Excluding Float to) {
        return new Base<>(from, to, Float::compare);
    }

    @Nonnull
    static Range<Double> rangeOf(@Nullable @Including Double from, @Nullable @Excluding Double to) {
        return new Base<>(from, to, Double::compare);
    }

    @SuppressWarnings("rawtypes")
    @Nonnull
    static <V extends Comparable> Range<V> rangeOf(@Nullable @Including V from, @Nullable @Excluding V to) {
        //noinspection unchecked
        return new Base<>(from, to, Comparable::compareTo);
    }

    @Nonnull
    static <V> Range<V> rangeOf(@Nullable @Including V from, @Nullable @Excluding V to, @Nonnull Comparator<V> comparator) {
        return new Base<>(from, to, comparator);
    }

    @ThreadSafe
    @Immutable
    class Base<V> implements Range<V> {

        @Nonnull
        @Including
        private final Optional<V> from;
        @Nonnull
        @Excluding
        private final Optional<V> to;
        @Nonnull
        private final Comparator<V> comparator;

        protected Base(@Nullable @Including V from, @Nullable @Excluding V to, @Nonnull Comparator<V> comparator) {
            this.from = ofNullable(from);
            this.to = ofNullable(to);
            this.comparator = comparator;
            if (from != null && to != null && comparator.compare(from, to) > 0) {
                throw new IllegalArgumentException("From value (" + from + ") is greater than to value (" + to + ")");
            }
        }

        @Override
        @Including
        @Nonnull
        public Optional<V> from() {
            return from;
        }

        @Override
        @Excluding
        @Nonnull
        public Optional<V> to() {
            return to;
        }

        @Nonnull
        public Comparator<V> comparator() {
            return comparator;
        }

        @Override
        public boolean test(@Nullable V that) {
            if (that == null) {
                return !from().isPresent() && !to().isPresent();
            }
            return matchesFrom(that) && matchesTo(that);
        }

        protected boolean matchesFrom(@Nonnull V that) {
            return from()
                .map(current -> comparator().compare(current, that) >= 0)
                .orElse(true);
        }

        protected boolean matchesTo(@Nonnull V that) {
            return to()
                .map(current -> comparator().compare(current, that) < 0)
                .orElse(true);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) { return true; }
            if (!(o instanceof Range)) { return false; }
            final Range<?> that = (Range<?>) o;
            return Objects.equals(from(), that.from()) &&
                Objects.equals(to(), that.to());
        }

        @Override
        public int hashCode() {
            return Objects.hash(from(), to());
        }

        @Override
        public String toString() {
            return ""
                + from().map(Object::toString).orElse("<open>")
                + " ... "
                + to().map(Object::toString).orElse("<open>");
        }

    }

}
