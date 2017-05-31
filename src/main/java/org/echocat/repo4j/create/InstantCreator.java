package org.echocat.repo4j.create;

import org.echocat.repo4j.create.Requirement.RangeRequirement;
import org.echocat.repo4j.range.Range;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.time.Instant;
import java.util.Random;
import java.util.function.Supplier;

import static java.time.Instant.ofEpochMilli;
import static org.echocat.repo4j.util.RandomUtils.nextLongFor;
import static org.echocat.repo4j.util.UniqueValueCreator.serial;

@Immutable
@ThreadSafe
public class InstantCreator extends NumberCreatorSupport<Instant, Long, Requirement<Instant>> {

    @Nonnull
    public static Builder instantCreator() {
        return new Builder();
    }

    protected InstantCreator(
        @Nonnull Random random,
        @Nonnull Supplier<Long> uniqueValueSupplier
    ) {
        super(Instant.class, random, uniqueValueSupplier);
    }

    @Nonnull
    @Override
    protected Instant map(@Nonnull Long input) {
        return ofEpochMilli(input);
    }

    @Override
    @Nonnull
    protected Instant createBy(@Nonnull RangeRequirement<Instant> requirement) {
        final Range<Long> range = mapToLong(requirement, Instant::toEpochMilli);
        final long millis = nextLongFor(random(), range);
        return ofEpochMilli(millis);
    }

    public static class Builder extends NumberCreatorSupport.Builder<Long, Builder> {

        protected Builder() {}

        @Nonnull
        public InstantCreator build() {
            return new InstantCreator(
                random().orElseGet(Random::new),
                uniqueValueSupplier().orElseGet(() -> serial()
                    .build()
                )
            );
        }

    }

}
