package org.echocat.repo4j.entity;

import org.echocat.repo4j.IdEnabled;
import org.echocat.repo4j.util.ReflectionUtils;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Objects;
import java.util.Optional;

import static org.echocat.repo4j.util.OptionalUtils.ofNonnull;
import static org.echocat.repo4j.util.OptionalUtils.valueOf;

@FunctionalInterface
public interface IdentifiedEntity<ID> extends Entity, IdEnabled<ID> {

    @Immutable
    public abstract static class Base<ID> implements IdentifiedEntity<ID> {

        @Nonnull
        private final Class<?> baseType;
        @Nonnull
        private final ID id;

        protected Base(
            @Nonnull Class<?> baseType,
            @Nonnull ID id
        ) {
            this.baseType = baseType;
            this.id = id;
        }

        @Nonnull
        protected Class<?> baseType() {
            return baseType;
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
            final IdentifiedEntity<?> that = (IdentifiedEntity<?>) o;
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

    public abstract static class BaseBuilder<ID, E extends IdentifiedEntity<ID>, B extends BaseBuilder<ID, E, B>> implements Builder<E, B> {

        @Nonnull
        private Optional<ID> id = Optional.empty();

        protected BaseBuilder() {}

        @Nonnull
        @Override
        public B with(@Nonnull E base) {
            withId(base.id());
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
        protected Optional<ID> id() {
            return id;
        }

        @Nonnull
        @Override
        public E build() {
            return buildInternal(
                valueOf(id, "id")
            );
        }

        @Nonnull
        protected abstract E buildInternal(@Nonnull ID id);

        @Nonnull
        protected B instance() {
            // noinspection unchecked
            return (B) this;
        }

    }

}
