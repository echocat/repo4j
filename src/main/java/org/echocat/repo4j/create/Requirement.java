package org.echocat.repo4j.create;

import org.echocat.repo4j.range.Range;

import javax.annotation.Nonnull;
import java.util.Objects;

public interface Requirement<T> {

    @Nonnull
    static <T> FixedRequirement<T> fixedRequirement(@Nonnull T value) {
        return new FixedRequirement.Base<>(value);
    }

    @Nonnull
    static <T> UniqueRequirement<T> uniqueRequirement() {
        return new UniqueRequirement.Base<>();
    }

    @Nonnull
    static <T, P, V, VR extends Requirement<V>> ContainingRequirement<T, P, V, VR> containingRequirement(@Nonnull P pattern, @Nonnull VR valueRequirement) {
        return new ContainingRequirement.Base<>(pattern, valueRequirement);
    }

    @Nonnull
    static <T, P> RangeRequirement<T> rangeRequirement(@Nonnull Range<T> range) {
        return new RangeRequirement.Base<>(range);
    }

    @FunctionalInterface
    interface FixedRequirement<V> extends Requirement<V> {

        @Nonnull
        V value();

        class Base<T> implements FixedRequirement<T> {

            @Nonnull
            private final T value;

            protected Base(@Nonnull T value) {
                this.value = value;
            }

            @Nonnull
            @Override
            public T value() {
                return value;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (!(o instanceof FixedRequirement)) {
                    return false;
                }
                final FixedRequirement<?> that = (FixedRequirement<?>) o;
                return Objects.equals(value(), that.value());
            }

            @Override
            public int hashCode() {
                return Objects.hash(value());
            }

            @Override
            public String toString() {
                return FixedRequirement.class.getSimpleName() + "{" + value() + "}";
            }

        }

    }

    interface UniqueRequirement<T> extends Requirement<T> {

        class Base<T, P> implements UniqueRequirement<T> {

            protected Base() {}

            @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (!(o instanceof UniqueRequirement)) {
                    return false;
                }
                return true;
            }

            @Override
            public int hashCode() {
                return 0;
            }

            @Override
            public String toString() {
                return UniqueRequirement.class.getSimpleName() + "{" + "}";
            }

        }

    }

    interface ContainingRequirement<T, P, V, VR extends Requirement<V>> extends UniqueRequirement<T> {
        @Nonnull
        P pattern();

        @Nonnull
        VR valueRequirement();

        class Base<T, P, V, VR extends Requirement<V>> implements ContainingRequirement<T, P, V, VR> {

            @Nonnull
            private final P pattern;
            @Nonnull
            private final VR valueRequirement;

            protected Base(
                @Nonnull P pattern,
                @Nonnull VR valueRequirement
            ) {
                this.pattern = pattern;
                this.valueRequirement = valueRequirement;
            }

            @Override
            @Nonnull
            public P pattern() {
                return pattern;
            }

            @Override
            @Nonnull
            public VR valueRequirement() {
                return valueRequirement;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (!(o instanceof Requirement.ContainingRequirement)) {
                    return false;
                }
                final ContainingRequirement<?, ?, ?, ?> that = (ContainingRequirement<?, ?, ?, ?>) o;
                return Objects.equals(pattern(), that.pattern())
                    && Objects.equals(valueRequirement(), that.valueRequirement());
            }

            @Override
            public int hashCode() {
                return Objects.hash(pattern(), valueRequirement());
            }

            @Override
            public String toString() {
                return ContainingRequirement.class.getSimpleName() + "{pattern: " + pattern() + ", valueRequirement: " + valueRequirement() + "}";
            }

        }
    }

    @FunctionalInterface
    interface RangeRequirement<T> extends Requirement<T> {
        @Nonnull
        Range<T> range();

        class Base<T> implements RangeRequirement<T> {

            @Nonnull
            private final Range<T> range;

            protected Base(@Nonnull Range<T> range) {
                this.range = range;
            }

            @Nonnull
            @Override
            public Range<T> range() {
                return range;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (!(o instanceof RangeRequirement)) {
                    return false;
                }
                final RangeRequirement<?> that = (RangeRequirement<?>) o;
                return Objects.equals(range(), that.range());
            }

            @Override
            public int hashCode() {
                return Objects.hash(range());
            }

            @Override
            public String toString() {
                return RangeRequirement.class.getSimpleName() + "{" + range() + "}";
            }

        }

    }

}
