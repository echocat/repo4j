package org.echocat.repo4j.create;

import org.echocat.repo4j.create.Requirement.RangeRequirement;
import org.echocat.repo4j.range.Range;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.util.Objects;
import java.util.Random;
import java.util.function.Supplier;

import static org.echocat.repo4j.util.RandomUtils.*;
import static org.echocat.repo4j.util.UniqueValueCreator.serialOf;

@Immutable
@ThreadSafe
public class NumberCreator<N extends Number> extends NumberCreatorSupport<N, N, Requirement<N>> {

    @Nonnull
    public static <N extends Number> Builder<N> numberCreatorFor(@Nonnull Class<N> type) {
        return new Builder<>(type);
    }

    protected NumberCreator(
        @Nonnull Class<N> type,
        @Nonnull Random random,
        @Nonnull Supplier<N> uniqueValueSupplier
    ) {
        super(type, random, uniqueValueSupplier);
    }

    @Nonnull
    @Override
    protected N map(@Nonnull N input) {
        return input;
    }

    @Override
    @Nonnull
    protected N createBy(@Nonnull RangeRequirement<N> requirement) {
        final Range<N> range = requirement.range();
        if (Objects.equals(type(), Short.class)) {
            //noinspection unchecked
            return (N) (Object) nextShortFor(random(), (Range<Short>) range);
        }
        if (Objects.equals(type(), Integer.class)) {
            //noinspection unchecked
            return (N) (Object) nextIntegerFor(random(), (Range<Integer>) range);
        }
        if (Objects.equals(type(), Long.class)) {
            //noinspection unchecked
            return (N) (Object) nextLongFor(random(), (Range<Long>) range);
        }
        if (Objects.equals(type(), Float.class)) {
            //noinspection unchecked
            return (N) (Object) nextFloatFor(random(), (Range<Float>) range);
        }
        if (Objects.equals(type(), Double.class)) {
            //noinspection unchecked
            return (N) (Object) nextDoubleFor(random(), (Range<Double>) range);
        }
        throw createUnsupportedRequirement(requirement);
    }

    public static class Builder<N extends Number> extends NumberCreatorSupport.Builder<N, Builder<N>> {

        @Nonnull
        private final Class<N> type;

        protected Builder(
            @Nonnull Class<N> type
        ) {

            this.type = type;
        }

        @Nonnull
        protected Class<N> type() {
            return type;
        }

        @Nonnull
        public NumberCreator<N> build() {
            return new NumberCreator<>(
                type(),
                random().orElseGet(Random::new),
                uniqueValueSupplier().orElseGet(() -> serialOf(type())
                    .build()
                )
            );
        }

    }

}
