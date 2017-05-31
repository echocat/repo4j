package org.echocat.repo4j.create;

import org.echocat.repo4j.create.Requirement.RangeRequirement;
import org.echocat.repo4j.create.Requirement.UniqueRequirement;
import org.echocat.repo4j.range.Range;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.echocat.repo4j.range.Range.rangeOf;

@Immutable
@ThreadSafe
public abstract class NumberCreatorSupport<T, N extends Number, R extends Requirement<T>> extends Creator.Base<T, R> {

    @Nonnull
    private final Random random;
    @Nonnull
    private final Supplier<N> uniqueValueSupplier;

    protected NumberCreatorSupport(
        @Nonnull Class<T> type,
        @Nonnull Random random,
        @Nonnull Supplier<N> uniqueValueSupplier
    ) {
        super(type);
        this.random = random;
        this.uniqueValueSupplier = uniqueValueSupplier;
    }

    @Nonnull
    @Override
    public T createBy(@Nonnull R requirement) {
        if (requirement instanceof UniqueRequirement<?>) {
            //noinspection unchecked
            return createBy((UniqueRequirement<T>) requirement);
        }
        if (requirement instanceof RangeRequirement<?>) {
            //noinspection unchecked
            return createBy((RangeRequirement<T>) requirement);
        }
        return super.createBy(requirement);
    }

    @Nonnull
    protected abstract T map(@Nonnull N input);

    @Nonnull
    protected Range<Long> mapToLong(@Nonnull RangeRequirement<T> input, @Nonnull Function<T, Long> mapper) {
        return mapToLong(input.range(), mapper);
    }

    @Nonnull
    protected Range<Long> mapToLong(@Nonnull Range<T> input, @Nonnull Function<T, Long> mapper) {
        return rangeOf(
            input.from().map(mapper).orElse(null),
            input.to().map(mapper).orElse(null)
        );
    }

    @Nonnull
    protected T createBy(@SuppressWarnings("unused") @Nonnull UniqueRequirement<T> requirement) {
        final N number = requireNonNull(uniqueValueSupplier().get());
        return map(number);
    }

    @Nonnull
    protected abstract T createBy(@Nonnull RangeRequirement<T> requirement);

    @Nonnull
    protected Random random() {
        return random;
    }

    @Nonnull
    protected Supplier<N> uniqueValueSupplier() {
        return uniqueValueSupplier;
    }

    public abstract static class Builder<N extends Number, B extends Builder<N, B>> {

        @Nonnull
        private Optional<Random> random = empty();
        @Nonnull
        private Optional<Supplier<N>> uniqueValueSupplier = empty();

        protected Builder() {}

        @Nonnull
        public B setRandom(@Nullable Random random) {
            this.random = ofNullable(random);
            //noinspection unchecked
            return (B) this;
        }

        @Nonnull
        public B setUniqueValueSupplier(@Nullable Supplier<N> uniqueValueSupplier) {
            this.uniqueValueSupplier = ofNullable(uniqueValueSupplier);
            //noinspection unchecked
            return (B) this;
        }

        @Nonnull
        protected Optional<Random> random() {
            return random;
        }

        @Nonnull
        protected Optional<Supplier<N>> uniqueValueSupplier() {
            return uniqueValueSupplier;
        }

    }

}
