package org.echocat.repo4j.entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;

public interface ManagedEntity<ID> extends IdentifiedEntity<ID> {

    public static final ZoneId UTC = ZoneId.of("UTC");

    @Nonnull
    LocalDateTime created();

    @Nonnull
    LocalDateTime lastModified();

    @Immutable
    public abstract static class Base<ID> extends IdentifiedEntity.Base<ID> implements ManagedEntity<ID> {

        @Nonnull
        private final LocalDateTime created;
        @Nonnull
        private final LocalDateTime lastModified;

        protected Base(
            @Nonnull Class<?> baseType,
            @Nonnull ID id,
            @Nonnull LocalDateTime created,
            @Nonnull LocalDateTime lastModified
        ) {
            super(baseType, id);
            this.created = created;
            this.lastModified = lastModified;
        }

        @Nonnull
        @Override
        public LocalDateTime created() {
            return created;
        }

        @Nonnull
        @Override
        public LocalDateTime lastModified() {
            return lastModified;
        }

    }

    public abstract static class BaseBuilder<ID, E extends ManagedEntity<ID>, B extends BaseBuilder<ID, E, B>>
        extends IdentifiedEntity.BaseBuilder<ID, E, B> {

        @Nonnull
        private Optional<LocalDateTime> created = Optional.empty();
        @Nonnull
        private Optional<LocalDateTime> lastModified = Optional.empty();

        protected BaseBuilder() {}

        @Nonnull
        @Override
        public B with(@Nonnull E base) {
            withCreated(base.created());
            withLastModified(base.lastModified());
            return super.with(base);
        }

        @Nonnull
        public B withCreated(@Nullable LocalDateTime created) {
            this.created = ofNullable(created);
            return instance();
        }

        @Nonnull
        public B withLastModified(@Nullable LocalDateTime lastModified) {
            this.lastModified = ofNullable(lastModified);
            return instance();
        }

        @Nonnull
        protected Optional<LocalDateTime> created() {
            return created;
        }

        @Nonnull
        protected Optional<LocalDateTime> lastModified() {
            return lastModified;
        }


        @Nonnull
        @Override
        protected E buildInternal(@Nonnull ID id) {
            final LocalDateTime created = created().orElseGet(() -> now(UTC));
            return buildInternal(
                id,
                created,
                lastModified().orElse(created)
            );
        }

        @Nonnull
        protected abstract E buildInternal(@Nonnull ID id, @Nonnull LocalDateTime created, @Nonnull LocalDateTime lastModified);

    }

}
