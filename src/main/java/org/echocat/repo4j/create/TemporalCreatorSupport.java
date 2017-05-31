package org.echocat.repo4j.create;

import org.echocat.repo4j.create.Requirement.RangeRequirement;
import org.echocat.repo4j.create.Requirement.UniqueRequirement;
import org.echocat.repo4j.range.Range;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.time.Instant;
import java.time.temporal.Temporal;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.echocat.repo4j.create.Requirement.rangeRequirementOf;
import static org.echocat.repo4j.create.Requirement.uniqueRequirement;
import static org.echocat.repo4j.range.Range.rangeOf;

@Immutable
@ThreadSafe
public abstract class TemporalCreatorSupport<T extends Temporal & Comparable<?>, R extends Requirement<T>> extends Creator.Base<T, R> {

    @Nonnull
    private final Creator<Instant, Requirement<Instant>> instantCreator;

    protected TemporalCreatorSupport(
        @Nonnull Class<T> type,
        @Nonnull Creator<Instant, Requirement<Instant>> instantCreator
    ) {
        super(type);
        this.instantCreator = instantCreator;
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
    protected abstract T map(@Nonnull Instant input);

    @Nonnull
    protected abstract Instant map(@Nonnull T input);

    @Nonnull
    protected T createBy(@SuppressWarnings("unused") @Nonnull UniqueRequirement<T> requirement) {
        final Instant instant = instantCreator().createBy(uniqueRequirement());
        return map(instant);
    }

    @Nonnull
    protected abstract T createBy(@Nonnull RangeRequirement<T> requirement);

    @Nonnull
    protected Instant createInstantBy(@Nonnull RangeRequirement<T> requirement) {
        final Range<T> range = requirement.range();
        final Range<Instant> instantRange = rangeOf(
            range.from().map(this::map).orElse(null),
            range.to().map(this::map).orElse(null)
        );
        return instantCreator().createBy(rangeRequirementOf(instantRange));
    }

    @Nonnull
    public Creator<Instant, Requirement<Instant>> instantCreator() {
        return instantCreator;
    }

    public abstract static class Builder<B extends Builder<B>> {

        @Nonnull
        private Optional<Creator<Instant, Requirement<Instant>>> instantCreator = empty();

        protected Builder() {}

        @Nonnull
        public B setInstantCreator(@Nullable Creator<Instant, Requirement<Instant>> instantCreator) {
            this.instantCreator = ofNullable(instantCreator);
            //noinspection unchecked
            return (B) this;
        }

        @Nonnull
        protected Optional<Creator<Instant, Requirement<Instant>>> instantCreator() {
            return instantCreator;
        }

        @Nonnull
        protected Creator<Instant, Requirement<Instant>> getInstantCreator() {
            return instantCreator()
                .orElseGet(() -> InstantCreator.instantCreator()
                    .build()
                );
        }

    }

}
