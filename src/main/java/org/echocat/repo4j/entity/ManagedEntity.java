package org.echocat.repo4j.entity;

import org.echocat.repo4j.IdEnabled;
import org.echocat.repo4j.util.ReflectionUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;
import static org.echocat.repo4j.util.OptionalUtils.ofNonnull;
import static org.echocat.repo4j.util.OptionalUtils.valueOf;

public interface ManagedEntity<ID> extends Entity, IdEnabled<ID> {

    public static final ZoneId UTC = ZoneId.of("UTC");

    @Nonnull
    LocalDateTime created();

    @Nonnull
    LocalDateTime lastModified();

    @Immutable
    public abstract static class Base<ID> implements ManagedEntity<ID> {

        @Nonnull
        private final Class<?> baseType;
        @Nonnull
        private final ID id;
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
            this.baseType = baseType;
            this.id = id;
            this.created = created;
            this.lastModified = lastModified;
        }

        @Nonnull
        protected Class<?> baseType() {
            return baseType;
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

        @Nonnull
        @Override
        public ID id() {
            return id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) { return true; }
            if (!baseType().isInstance(o)) { return false; }
            final ManagedEntity<?> that = (ManagedEntity<?>) o;
            return Objects.equals(id(), that.id());
        }

        @Override
        public int hashCode() {
            return Objects.hash(id());
        }

        @Override
        public String toString() {
            return baseType().getSimpleName() + "#" + id() + "{"
                + ReflectionUtils.toString(this, candidate -> !"id".equals(candidate.left()))
                + "}";
        }

    }

    public abstract static class BaseBuilder<ID, E extends ManagedEntity<ID>, B extends BaseBuilder<ID, E, B>> implements Builder<E, B> {

        @Nonnull
        private Optional<ID> id = Optional.empty();
        @Nonnull
        private Optional<LocalDateTime> created = Optional.empty();
        @Nonnull
        private Optional<LocalDateTime> lastModified = Optional.empty();

        protected BaseBuilder() {}

        @Nonnull
        @Override
        public B with(@Nonnull E base) {
            withId(base.id());
            withCreated(base.created());
            withLastModified(base.lastModified());
            withInternal(base);
            return instance();
        }

        protected abstract void withInternal(@Nonnull E base);

        @Nonnull
        public B withId(@Nonnull ID id) {
            this.id = ofNonnull(id, "id");
            return instance();
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
        protected Optional<ID> id() {
            return id;
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
        public E build() {
            final LocalDateTime created = created().orElseGet(() -> now(UTC));
            return buildInternal(
                valueOf(id, "id"),
                created,
                lastModified().orElse(created)
            );
        }

        @Nonnull
        protected abstract E buildInternal(@Nonnull ID id, @Nonnull LocalDateTime created, @Nonnull LocalDateTime lastModified);

        @Nonnull
        protected B instance() {
            // noinspection unchecked
            return (B) this;
        }

    }

}
