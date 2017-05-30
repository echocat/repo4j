package org.echocat.repo4j.create;

import org.echocat.repo4j.create.Requirement.RangeRequirement;
import org.echocat.repo4j.create.Requirement.UniqueRequirement;
import org.echocat.repo4j.range.Range;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.echocat.repo4j.util.RandomUtils.*;
import static org.echocat.repo4j.util.UniqueValueCreator.serialOf;

@Immutable
@ThreadSafe
public class NumberCreator<N extends Number> extends Creator.Base<N, Requirement<N>> {

    @Nonnull
    private static final NumberCreator<Long> DEFAULT = numberCreatorFor(Long.class)
        .build();

    @Nonnull
    public static NumberCreator<Long> defaultNumberCreator() {
        return DEFAULT;
    }

    @Nonnull
    public static <N extends Number> Builder<N> numberCreatorFor(@Nonnull Class<N> type) {
        return new Builder<>(type);
    }

    @Nonnull
    private final Random random;
    @Nonnull
    private final Supplier<N> uniqueValueSupplier;

    protected NumberCreator(
        @Nonnull Class<N> type,
        @Nonnull Random random,
        @Nonnull Supplier<N> uniqueValueSupplier
    ) {
        super(type);
        this.random = random;
        this.uniqueValueSupplier = uniqueValueSupplier;
    }

    @Nonnull
    @Override
    public N createBy(@Nonnull Requirement<N> requirement) {
        if (requirement instanceof UniqueRequirement<?>) {
            //noinspection unchecked
            return createBy((UniqueRequirement<N>) requirement);
        }
        if (requirement instanceof RangeRequirement<?>) {
            //noinspection unchecked
            return createBy((RangeRequirement<N>) requirement);
        }
        return super.createBy(requirement);
    }

    @Nonnull
    protected N createBy(@Nonnull UniqueRequirement<N> requirement) {
        return requireNonNull(uniqueValueSupplier().get());
    }

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

    @Nonnull
    protected Random random() {
        return random;
    }

    @Nonnull
    protected Supplier<N> uniqueValueSupplier() {
        return uniqueValueSupplier;
    }

    public static class Builder<N extends Number> {

        @Nonnull
        private final Class<N> type;
        @Nonnull
        private Optional<Random> random = empty();
        @Nonnull
        private Optional<Supplier<N>> uniqueValueSupplier = empty();

        protected Builder(
            @Nonnull Class<N> type
        ) {
            this.type = type;
        }

        @Nonnull
        public Builder<N> setRandom(@Nullable Random random) {
            this.random = ofNullable(random);
            return this;
        }

        @Nonnull
        public Builder<N> setUniqueValueSupplier(@Nullable Supplier<N> uniqueValueSupplier) {
            this.uniqueValueSupplier = ofNullable(uniqueValueSupplier);
            return this;
        }

        @Nonnull
        public NumberCreator<N> build() {
            return new NumberCreator<>(
                type,
                random.orElseGet(Random::new),
                uniqueValueSupplier.orElseGet(() -> serialOf(type)
                    .build()
                )
            );
        }

    }

}
