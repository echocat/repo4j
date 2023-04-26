package org.echocat.repo4j.create;

import org.echocat.repo4j.range.Range;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.LITERAL;
import static org.echocat.repo4j.create.Requirement.TemplateBasedRequirement.DEFAULT_PLACEHOLDER_PATTERN;

public interface Requirement<T> {

    @Nonnull
    static <T> FixedRequirement<T> fixedRequirementOf(@Nonnull T value) {
        return new FixedRequirement.Base<>(value);
    }

    @Nonnull
    static <T> UniqueRequirement<T> uniqueRequirement() {
        return new UniqueRequirement.Base<>();
    }

    @Nonnull
    static TemplateBasedRequirement<String, Pattern> templateBasedRequirementOf(@Nonnull String template, @Nonnull Requirement<String> valueRequirement) {
        return new TemplateBasedRequirement.Base<>(template, DEFAULT_PLACEHOLDER_PATTERN, valueRequirement);
    }

    @Nonnull
    static <T, P> TemplateBasedRequirement<T, P> templateBasedRequirementOf(@Nonnull T template, @Nonnull P valuePlaceholderPattern, @Nonnull Requirement<T> valueRequirement) {
        return new TemplateBasedRequirement.Base<>(template, valuePlaceholderPattern, valueRequirement);
    }

    @Nonnull
    static <T, P> RangeRequirement<T> rangeRequirementOf(@Nonnull Range<T> range) {
        return new RangeRequirement.Base<>(range);
    }

    @FunctionalInterface
    interface FixedRequirement<V> extends Requirement<V> {

        @Nonnull
        V value();

        class Base<T> implements FixedRequirement<T> {

            @Nonnull
            private final T value;

            protected Base(@Nonnull T value) {
                this.value = value;
            }

            @Nonnull
            @Override
            public T value() {
                return value;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (!(o instanceof FixedRequirement)) {
                    return false;
                }
                final FixedRequirement<?> that = (FixedRequirement<?>) o;
                return Objects.equals(value(), that.value());
            }

            @Override
            public int hashCode() {
                return Objects.hash(value());
            }

            @Override
            public String toString() {
                return FixedRequirement.class.getSimpleName() + "{" + value() + "}";
            }

        }

    }

    interface UniqueRequirement<T> extends Requirement<T> {

        class Base<T, P> implements UniqueRequirement<T> {

            protected Base() {}

            @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                //noinspection RedundantIfStatement
                if (!(o instanceof UniqueRequirement)) {
                    return false;
                }
                return true;
            }

            @Override
            public int hashCode() {
                return 0;
            }

            @Override
            public String toString() {
                return UniqueRequirement.class.getSimpleName() + "{" + "}";
            }

        }

    }

    interface TemplateBasedRequirement<T, P> extends Requirement<T> {

        String DEFAULT_PLACEHOLDER = "$$";
        Pattern DEFAULT_PLACEHOLDER_PATTERN = Pattern.compile(DEFAULT_PLACEHOLDER, LITERAL);

        @Nonnull
        T template();

        @Nonnull
        P valuePlaceholderPattern();

        @Nonnull
        Requirement<T> valueRequirement();

        class Base<T, P> implements TemplateBasedRequirement<T, P> {

            @Nonnull
            private final T template;
            @Nonnull
            private final P valuePlaceholderPattern;
            @Nonnull
            private final Requirement<T> valueRequirement;

            protected Base(
                @Nonnull T template,
                @Nonnull P valuePlaceholderPattern,
                @Nonnull Requirement<T> valueRequirement
            ) {
                this.template = template;
                this.valuePlaceholderPattern = valuePlaceholderPattern;
                this.valueRequirement = valueRequirement;
            }

            @Override
            @Nonnull
            public T template() {
                return template;
            }

            @Nonnull
            @Override
            public P valuePlaceholderPattern() {
                return valuePlaceholderPattern;
            }

            @Override
            @Nonnull
            public Requirement<T> valueRequirement() {
                return valueRequirement;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (!(o instanceof Requirement.TemplateBasedRequirement)) {
                    return false;
                }
                final TemplateBasedRequirement<?, ?> that = (TemplateBasedRequirement<?, ?>) o;
                return Objects.equals(template(), that.template())
                    && Objects.equals(valuePlaceholderPattern(), that.valuePlaceholderPattern())
                    && Objects.equals(valueRequirement(), that.valueRequirement());
            }

            @Override
            public int hashCode() {
                return Objects.hash(template(), valuePlaceholderPattern(), valueRequirement());
            }

            @Override
            public String toString() {
                return TemplateBasedRequirement.class.getSimpleName()
                    + "{template: " + template()
                    + ", valuePlaceholderPattern: " + valuePlaceholderPattern()
                    + ", valueRequirement: " + valueRequirement()
                    + "}";
            }

        }
    }

    @FunctionalInterface
    interface RangeRequirement<T> extends Requirement<T> {
        @Nonnull
        Range<T> range();

        class Base<T> implements RangeRequirement<T> {

            @Nonnull
            private final Range<T> range;

            protected Base(@Nonnull Range<T> range) {
                this.range = range;
            }

            @Nonnull
            @Override
            public Range<T> range() {
                return range;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (!(o instanceof RangeRequirement)) {
                    return false;
                }
                final RangeRequirement<?> that = (RangeRequirement<?>) o;
                return Objects.equals(range(), that.range());
            }

            @Override
            public int hashCode() {
                return Objects.hash(range());
            }

            @Override
            public String toString() {
                return RangeRequirement.class.getSimpleName() + "{" + range() + "}";
            }

        }

    }

}
