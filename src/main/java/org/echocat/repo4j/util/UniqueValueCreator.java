package org.echocat.repo4j.util;

import jdk.nashorn.internal.ir.annotations.Immutable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

@Immutable
@ThreadSafe
public interface UniqueValueCreator<V> extends Supplier<V> {

    @Nonnull
    V next();

    @Nonnull
    Class<V> type();

    @Override
    @Nonnull
    default V get() {
        return next();
    }

    @Nonnull
    static UuidBuilder uuid() {
        return new UuidBuilder();
    }

    @Nonnull
    static SerialBuilder<Long> serial() {
        return serialOf(Long.class);
    }

    @Nonnull
    static <T extends Number> SerialBuilder<T> serialOf(@Nonnull Class<T> type) {
        return new SerialBuilder<>(type);
    }

    @Immutable
    @ThreadSafe
    class Uuid implements UniqueValueCreator<UUID> {

        @Nonnull
        private final java.util.Random random;

        protected Uuid(
            @Nonnull java.util.Random random
        ) {
            this.random = random;
        }

        @Nonnull
        @Override
        public UUID next() {
            final byte[] randomBytes = new byte[16];
            random().nextBytes(randomBytes);
            randomBytes[6] &= 0x0f;  /* clear version        */
            randomBytes[6] |= 0x40;  /* set to version 4     */
            randomBytes[8] &= 0x3f;  /* clear variant        */
            randomBytes[8] |= 0x80;  /* set to IETF variant  */
            return new UUID(longOf(randomBytes, 0), longOf(randomBytes, 8));
        }

        protected long longOf(byte[] b, int off) {
            return ((b[off + 7] & 0xFFL)) +
                ((b[off + 6] & 0xFFL) << 8) +
                ((b[off + 5] & 0xFFL) << 16) +
                ((b[off + 4] & 0xFFL) << 24) +
                ((b[off + 3] & 0xFFL) << 32) +
                ((b[off + 2] & 0xFFL) << 40) +
                ((b[off + 1] & 0xFFL) << 48) +
                (((long) b[off]) << 56);
        }


        @Nonnull
        protected Random random() {
            return random;
        }

        @Nonnull
        @Override
        public Class<UUID> type() {
            return UUID.class;
        }

    }

    @Immutable
    @ThreadSafe
    class UuidBuilder {

        @Nonnull
        private Optional<Random> random = empty();

        protected UuidBuilder() {}

        @Nonnull
        public UuidBuilder setRandom(@Nullable Random random) {
            this.random = ofNullable(random);
            return this;
        }

        @Nonnull
        public UniqueValueCreator<UUID> build() {
            return new Uuid(
                random.orElse(new SecureRandom())
            );
        }

    }

    class Serial<T extends Number> implements UniqueValueCreator<T> {

        @Nonnull
        private final Class<T> type;
        @Nonnull
        private final AtomicReference<T> value;
        @Nonnull
        private final T initialValue;
        @Nonnull
        private final T incrementBy;
        @Nonnull
        private final BiFunction<T, T, T> incrementFunction;

        protected Serial(
            @Nonnull Class<T> type,
            @Nonnull T initialValue,
            @Nonnull T incrementBy,
            @Nonnull BiFunction<T, T, T> incrementFunction
        ) {
            this.type = type;
            this.value = new AtomicReference<>(initialValue);
            this.initialValue = initialValue;
            this.incrementBy = incrementBy;
            this.incrementFunction = incrementFunction;
        }

        @Nonnull
        @Override
        public T next() {
            return value().updateAndGet(current -> incrementFunction.apply(current, incrementBy));
        }

        @Nonnull
        @Override
        public Class<T> type() {
            return type;
        }

        @Nonnull
        protected AtomicReference<T> value() {
            return value;
        }

        @Nonnull
        public T initialValue() {
            return initialValue;
        }

        @Nonnull
        public T incrementBy() {
            return incrementBy;
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + "{" +
                "initValue: " + initialValue() +
                ", incrementBy: " + incrementBy() +
                ", currentValue: " + value() +
                "}";
        }

    }

    class SerialBuilder<T extends Number> {

        @Nonnull
        private final Class<T> type;
        @Nonnull
        private Optional<T> initialValue = empty();
        @Nonnull
        private Optional<T> incrementBy = empty();

        protected SerialBuilder(
            @Nonnull Class<T> type
        ) {
            this.type = type;
        }

        @Nonnull
        public SerialBuilder<T> setInitialValue(@Nullable T initialValue) {
            this.initialValue = ofNullable(initialValue);
            return this;
        }

        @Nonnull
        public SerialBuilder<T> setIncrementBy(@Nullable T incrementBy) {
            this.incrementBy = ofNullable(incrementBy);
            return this;
        }

        @Nonnull
        public UniqueValueCreator<T> build() {
            //noinspection unchecked
            return new Serial<>(
                type,
                initialValue.orElseGet(() -> (T) selectInitialValue()),
                incrementBy.orElseGet(() -> (T) selectIncrementBy()),
                (BiFunction<T, T, T>) selectIncrementFunction()
            );
        }

        @Nonnull
        protected BiFunction<?, ?, ?> selectIncrementFunction() {
            if (Short.class.equals(type)) {
                return (BiFunction<Short, Short, Short>) (current, incrementBy) -> (short) (current + incrementBy);
            } else if (Integer.class.equals(type)) {
                return (BiFunction<Integer, Integer, Integer>) Integer::sum;
            } else if (Long.class.equals(type)) {
                return (BiFunction<Long, Long, Long>) Long::sum;
            } else if (Float.class.equals(type)) {
                return (BiFunction<Float, Float, Float>) Float::sum;
            } else if (Double.class.equals(type)) {
                return (BiFunction<Double, Double, Double>) Double::sum;
            }
            throw new UnsupportedOperationException("Could not handle type: " + type.getName());
        }

        @Nonnull
        protected Number selectInitialValue() {
            if (Short.class.equals(type)) {
                return (short) 0;
            } else if (Integer.class.equals(type)) {
                return 0;
            } else if (Long.class.equals(type)) {
                return 0L;
            } else if (Float.class.equals(type)) {
                return 0F;
            } else if (Double.class.equals(type)) {
                return 0D;
            }
            throw new UnsupportedOperationException("Could not handle type: " + type.getName());
        }

        @Nonnull
        protected Number selectIncrementBy() {
            if (Short.class.equals(type)) {
                return (short) 1;
            } else if (Integer.class.equals(type)) {
                return 1;
            } else if (Long.class.equals(type)) {
                return 1L;
            } else if (Float.class.equals(type)) {
                return 1F;
            } else if (Double.class.equals(type)) {
                return 1D;
            }
            throw new UnsupportedOperationException("Could not handle type: " + type.getName());
        }

    }

}
