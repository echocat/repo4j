package org.echocat.repo4j.util;

import javax.annotation.Nonnull;
import java.util.Objects;

public interface Tuple<L, R> {

    L left();

    R right();

    @Nonnull
    static <L, R> Tuple<L, R> tupleOf(L left, R right) {
        return new Impl<>(left, right);
    }

    static class Impl<L, R> implements Tuple<L, R> {

        private final L left;
        private final R right;

        protected Impl(L left, R right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public L left() {
            return left;
        }

        @Override
        public R right() {
            return right;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) { return true; }
            if (!(o instanceof Tuple)) { return false; }
            final Tuple<?, ?> that = (Tuple<?, ?>) o;
            return Objects.equals(left(), that.left()) &&
                Objects.equals(right(), that.right());
        }

        @Override
        public int hashCode() {
            return Objects.hash(left(), right());
        }

        @Override
        public String toString() {
            return left() + " : " + right();
        }

    }

}
