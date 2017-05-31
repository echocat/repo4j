package org.echocat.repo4j.create;

import org.echocat.repo4j.create.Requirement.RangeRequirement;
import org.echocat.repo4j.range.Range;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static java.time.ZoneOffset.UTC;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

@Immutable
@ThreadSafe
public class OffsetDateTimeCreator extends TemporalCreatorSupport<OffsetDateTime, Requirement<OffsetDateTime>> {

    @Nonnull
    public static Builder offsetDateTimeCreator() {
        return new Builder();
    }

    @Nonnull
    private final ZoneOffset defaultOffset;

    public OffsetDateTimeCreator(
        @Nonnull Creator<Instant, Requirement<Instant>> instantCreator,
        @Nonnull ZoneOffset defaultOffset
    ) {
        //noinspection unchecked
        super(OffsetDateTime.class, instantCreator);
        this.defaultOffset = defaultOffset;
    }

    @Nonnull
    @Override
    protected OffsetDateTime map(@Nonnull Instant input) {
        return OffsetDateTime.ofInstant(input, defaultOffset());
    }

    @Nonnull
    @Override
    protected Instant map(@Nonnull OffsetDateTime input) {
        return input.toInstant();
    }

    @Nonnull
    @Override
    protected OffsetDateTime createBy(@Nonnull RangeRequirement<OffsetDateTime> requirement) {
        final Range<OffsetDateTime> range = requirement.range();
        final ZoneOffset offset = range.from()
            .map(OffsetDateTime::getOffset)
            .orElseGet(() -> range.to()
                .map(OffsetDateTime::getOffset)
                .orElseGet(this::defaultOffset)
            );
        final Instant instant = createInstantBy(requirement);
        return OffsetDateTime.ofInstant(instant, offset);
    }

    @Nonnull
    protected ZoneOffset defaultOffset() {
        return defaultOffset;
    }

    public static class Builder extends TemporalCreatorSupport.Builder<Builder> {

        protected Builder() {}

        @Nonnull
        private Optional<ZoneOffset> defaultOffset = empty();

        @Nonnull
        public Builder setDefaultOffset(@Nullable ZoneOffset defaultOffset) {
            this.defaultOffset = ofNullable(defaultOffset);
            return this;
        }

        @Nonnull
        public Builder setDefaultZoneId(@Nullable String zoneId) {
            return setDefaultOffset(zoneId != null ? ZoneOffset.of(zoneId) : null);
        }

        @Nonnull
        public OffsetDateTimeCreator build() {
            return new OffsetDateTimeCreator(
                getInstantCreator(),
                defaultOffset.orElse(UTC)
            );
        }

    }

}
