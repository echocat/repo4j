package org.echocat.repo4j.create;

import org.echocat.repo4j.create.Requirement.RangeRequirement;
import org.echocat.repo4j.range.Range;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;

import static java.time.ZoneOffset.UTC;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

@Immutable
@ThreadSafe
public class ZonedDateTimeCreator extends TemporalCreatorSupport<ZonedDateTime, Requirement<ZonedDateTime>> {

    @Nonnull
    public static Builder zonedDateTimeCreator() {
        return new Builder();
    }

    @Nonnull
    private final ZoneId defaultZoneId;

    public ZonedDateTimeCreator(
        @Nonnull Creator<Instant, Requirement<Instant>> instantCreator,
        @Nonnull ZoneId defaultZoneId
    ) {
        //noinspection unchecked
        super(ZonedDateTime.class, instantCreator);
        this.defaultZoneId = defaultZoneId;
    }

    @Nonnull
    @Override
    protected ZonedDateTime map(@Nonnull Instant input) {
        return ZonedDateTime.ofInstant(input, defaultZoneId());
    }

    @Nonnull
    @Override
    protected Instant map(@Nonnull ZonedDateTime input) {
        return input.toInstant();
    }

    @Nonnull
    @Override
    protected ZonedDateTime createBy(@Nonnull RangeRequirement<ZonedDateTime> requirement) {
        final Range<ZonedDateTime> range = requirement.range();
        final ZoneId zoneId = range.from()
            .map(ZonedDateTime::getZone)
            .orElseGet(() -> range.to()
                .map(ZonedDateTime::getZone)
                .orElseGet(this::defaultZoneId)
            );
        final Instant instant = createInstantBy(requirement);
        return ZonedDateTime.ofInstant(instant, zoneId);
    }

    @Nonnull
    protected ZoneId defaultZoneId() {
        return defaultZoneId;
    }

    public static class Builder extends TemporalCreatorSupport.Builder<Builder> {

        protected Builder() {}

        @Nonnull
        private Optional<ZoneId> defaultZoneId = empty();

        @Nonnull
        public Builder setDefaultZoneId(@Nullable ZoneId defaultZoneId) {
            this.defaultZoneId = ofNullable(defaultZoneId);
            return this;
        }

        @Nonnull
        public Builder setDefaultZoneId(@Nullable String zoneId) {
            return setDefaultZoneId(zoneId != null ? ZoneId.of(zoneId) : null);
        }

        @Nonnull
        public Builder setDefaultZoneId(@Nullable String prefix, @Nullable ZoneOffset offset) {
            return setDefaultZoneId(prefix != null ? ZoneId.ofOffset(prefix, offset != null ? offset : UTC) : null);
        }

        @Nonnull
        public ZonedDateTimeCreator build() {
            return new ZonedDateTimeCreator(
                getInstantCreator(),
                defaultZoneId.orElseGet(ZoneId::systemDefault)
            );
        }

    }

}
