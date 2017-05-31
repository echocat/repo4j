package org.echocat.repo4j.create;

import org.echocat.repo4j.create.Requirement.RangeRequirement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Optional;

import static java.time.ZoneOffset.UTC;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

@Immutable
@ThreadSafe
public class LocalDateTimeCreator extends TemporalCreatorSupport<LocalDateTime, Requirement<LocalDateTime>> {

    @Nonnull
    public static Builder localDateTimeCreator() {
        return new Builder();
    }

    @Nonnull
    private final ZoneId zoneId;

    public LocalDateTimeCreator(
        @Nonnull Creator<Instant, Requirement<Instant>> instantCreator,
        @Nonnull ZoneId zoneId
    ) {
        //noinspection unchecked
        super(LocalDateTime.class, instantCreator);
        this.zoneId = zoneId;
    }

    @Nonnull
    @Override
    protected LocalDateTime map(@Nonnull Instant input) {
        return LocalDateTime.ofInstant(input, zoneId());
    }

    @Nonnull
    @Override
    protected Instant map(@Nonnull LocalDateTime input) {
        final ZoneOffset offset = zoneId().getRules().getOffset(input);
        return input.toInstant(offset);
    }

    @Override
    @Nonnull
    protected LocalDateTime createBy(@Nonnull RangeRequirement<LocalDateTime> requirement) {
        final Instant instant = createInstantBy(requirement);
        return map(instant);
    }

    @Nonnull
    protected ZoneId zoneId() {
        return zoneId;
    }

    public static class Builder extends TemporalCreatorSupport.Builder<Builder> {

        protected Builder() {}

        @Nonnull
        private Optional<ZoneId> zoneId = empty();

        @Nonnull
        public Builder setZoneId(@Nullable ZoneId zoneId) {
            this.zoneId = ofNullable(zoneId);
            return this;
        }

        @Nonnull
        public Builder setZoneId(@Nullable String zoneId) {
            return setZoneId(zoneId != null ? ZoneId.of(zoneId) : null);
        }

        @Nonnull
        public Builder setZoneId(@Nullable String prefix, @Nullable ZoneOffset offset) {
            return setZoneId(prefix != null ? ZoneId.ofOffset(prefix, offset != null ? offset : UTC) : null);
        }

        @Nonnull
        public LocalDateTimeCreator build() {
            return new LocalDateTimeCreator(
                getInstantCreator(),
                zoneId.orElseGet(ZoneId::systemDefault)
            );
        }

    }

}
