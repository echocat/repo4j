package org.echocat.repo4j.create;

import org.echocat.repo4j.create.Requirement.FixedRequirement;

import javax.annotation.Nonnull;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

@FunctionalInterface
public interface Creator<V, R extends Requirement<V>> extends Function<R, V> {

    /**
     * Creates an entity based on given requirement.
     */
    @Nonnull
    V createBy(@Nonnull R requirement);

    @Override
    @Nonnull
    default V apply(@Nonnull R requirement) {
        requireNonNull(requirement, "Requirement should not be null.");
        final V result = createBy(requirement);
        requireNonNull(result, "Result should not be null.");
        return result;
    }

    @Nonnull
    static <V, R extends Requirement<V>> Creator<V, R> genericGeneratorFor(@Nonnull Class<V> type) {
        return new Base<>(type);
    }

    static class Base<V, R extends Requirement<V>> implements Creator<V, R> {

        @Nonnull
        private final Class<V> type;

        protected Base(
            @Nonnull Class<V> type
        ) {
            this.type = type;
        }

        @Nonnull
        @Override
        public V createBy(@Nonnull R requirement) {
            if (requirement instanceof FixedRequirement<?>) {
                //noinspection unchecked
                return createBy((FixedRequirement<V>) requirement);
            }
            throw createUnsupportedRequirement(requirement);
        }

        @Nonnull
        protected V createBy(@Nonnull FixedRequirement<V> requirement) {
            return requirement.value();
        }

        @Nonnull
        protected RuntimeException createUnsupportedRequirement(@Nonnull Requirement<?> requirement) {
            return new UnsupportedOperationException("Cannot use a requirement of type " + requirement.getClass().getName()
                + " to create an entity of type " + type().getName() + ".");
        }

        @Nonnull
        public Class<V> type() {
            return type;
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + "{type: " + type().getSimpleName() + "}";
        }

    }

}
