package org.echocat.repo4j.create;

import org.echocat.repo4j.create.Requirement.RangeRequirement;
import org.echocat.repo4j.range.Range;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.util.Date;
import java.util.Random;
import java.util.function.Supplier;

import static org.echocat.repo4j.util.RandomUtils.nextLongFor;
import static org.echocat.repo4j.util.UniqueValueCreator.serial;

@Immutable
@ThreadSafe
public class DateCreator extends NumberCreatorSupport<Date, Long, Requirement<Date>> {

    @Nonnull
    public static Builder dateCreator() {
        return new Builder();
    }

    protected DateCreator(
        @Nonnull Random random,
        @Nonnull Supplier<Long> uniqueValueSupplier
    ) {
        super(Date.class, random, uniqueValueSupplier);
    }

    @Nonnull
    @Override
    protected Date map(@Nonnull Long input) {
        return new Date(input);
    }

    @Override
    @Nonnull
    protected Date createBy(@Nonnull RangeRequirement<Date> requirement) {
        final Range<Long> range = mapToLong(requirement, Date::getTime);
        final long dateInMillis = nextLongFor(random(), range);
        return new Date(dateInMillis);
    }

    public static class Builder extends NumberCreatorSupport.Builder<Long, Builder> {

        protected Builder() {}

        @Nonnull
        public DateCreator build() {
            return new DateCreator(
                random().orElseGet(Random::new),
                uniqueValueSupplier().orElseGet(() -> serial()
                    .build()
                )
            );
        }

    }

}
