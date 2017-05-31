package org.echocat.repo4j.create;

import org.echocat.repo4j.create.Requirement.RangeRequirement;
import org.echocat.repo4j.range.Range;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.time.Duration;
import java.util.Random;
import java.util.function.Supplier;

import static java.time.temporal.ChronoUnit.MILLIS;
import static org.echocat.repo4j.util.RandomUtils.nextLongFor;
import static org.echocat.repo4j.util.UniqueValueCreator.serial;

@Immutable
@ThreadSafe
public class DurationCreator extends NumberCreatorSupport<Duration, Long, Requirement<Duration>> {

    @Nonnull
    public static Builder durationCreator() {
        return new Builder();
    }

    protected DurationCreator(
        @Nonnull Random random,
        @Nonnull Supplier<Long> uniqueValueSupplier
    ) {
        super(Duration.class, random, uniqueValueSupplier);
    }

    @Nonnull
    @Override
    protected Duration map(@Nonnull Long input) {
        return Duration.ofMillis(input);
    }

    @Override
    @Nonnull
    protected Duration createBy(@Nonnull RangeRequirement<Duration> requirement) {
        final Range<Long> range = mapToLong(requirement, duration -> duration.get(MILLIS));
        final long durationInMillis = nextLongFor(random(), range);
        return Duration.ofMillis(durationInMillis);
    }

    public static class Builder extends NumberCreatorSupport.Builder<Long, Builder> {

        protected Builder() {}

        @Nonnull
        public DurationCreator build() {
            return new DurationCreator(
                random().orElseGet(Random::new),
                uniqueValueSupplier().orElseGet(() -> serial()
                    .build()
                )
            );
        }

    }

}
