package org.echocat.repo4j.create;

import org.echocat.repo4j.create.Requirement.ContainingRequirement;
import org.echocat.repo4j.create.Requirement.UniqueRequirement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Optional.ofNullable;
import static java.util.regex.Matcher.quoteReplacement;
import static java.util.regex.Pattern.LITERAL;
import static java.util.regex.Pattern.compile;
import static org.echocat.repo4j.util.UniqueValueCreator.uuid;

@Immutable
@ThreadSafe
public class StringCreator extends Creator.Base<String, Requirement<String>> {

    public static final String UNIQUE_VALUE_PLACE_HOLDER = "$$uniqueValue$$";

    @Nonnull
    protected static final Pattern PLACE_HOLDER_PATTERN = compile(UNIQUE_VALUE_PLACE_HOLDER, LITERAL);
    @Nonnull
    private static final StringCreator DEFAULT = stringCreator()
        .build();

    @Nonnull
    public static StringCreator defaultStringCreator() {
        return DEFAULT;
    }

    @Nonnull
    public static Builder stringCreator() {
        return new Builder();
    }

    @Nonnull
    private final Supplier<?> uniqueValueSupplier;

    protected StringCreator(
        @Nonnull Supplier<?> uniqueValueSupplier
    ) {
        super(String.class);
        this.uniqueValueSupplier = uniqueValueSupplier;
    }

    @Nonnull
    @Override
    public String createBy(@Nonnull Requirement<String> requirement) {
        if (requirement instanceof UniqueRequirement<?>) {
            //noinspection unchecked
            return createBy((UniqueRequirement<String>) requirement);
        }
        return super.createBy(requirement);
    }

    @Nonnull
    protected String createBy(@Nonnull ContainingRequirement<String, String, Object, Requirement<Object>> requirement) {
        final String pattern = requirement.pattern();
        final Matcher matcher = PLACE_HOLDER_PATTERN.matcher(pattern);
        boolean result = matcher.find();
        if (result) {
            final StringBuffer sb = new StringBuffer();
            do {
                matcher.appendReplacement(sb, quoteReplacement(nextUniqueValue()));
                result = matcher.find();
            } while (result);
            matcher.appendTail(sb);
            return sb.toString();
        }
        return pattern;
    }

    @Nonnull
    protected String createBy(@SuppressWarnings("unused") @Nonnull UniqueRequirement<String> requirement) {
        return nextUniqueValue();
    }

    @Nonnull
    protected Supplier<?> uniqueValueSupplier() {
        return uniqueValueSupplier;
    }

    @Nonnull
    protected String nextUniqueValue() {
        return Objects.toString(uniqueValueSupplier().get(), "");
    }

    public static class Builder {

        @Nonnull
        private Optional<Supplier<?>> uniqueValueSupplier = Optional.empty();

        protected Builder() {}

        @Nonnull
        public Builder setUniqueValueSupplier(@Nullable Supplier<?> uniqueValueSupplier) {
            this.uniqueValueSupplier = ofNullable(uniqueValueSupplier);
            return this;
        }

        @Nonnull
        public StringCreator build() {
            return new StringCreator(
                uniqueValueSupplier.orElseGet(() -> uuid()
                    .build())
            );
        }
    }

}
