package org.echocat.repo4j.create;

import org.echocat.repo4j.create.Requirement.TemplateBasedRequirement;
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
import static org.echocat.repo4j.util.UniqueValueCreator.uuid;

@Immutable
@ThreadSafe
public class StringCreator extends Creator.Base<String, Requirement<String>> {

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
        if (requirement instanceof Requirement.TemplateBasedRequirement) {
            return createBy((TemplateBasedRequirement<String, Pattern>) requirement);
        }
        if (requirement instanceof UniqueRequirement<?>) {
            return createBy((UniqueRequirement<String>) requirement);
        }
        return super.createBy(requirement);
    }

    @Nonnull
    protected String createBy(@Nonnull TemplateBasedRequirement<String, Pattern> requirement) {
        final String template = requirement.template();
        final Matcher matcher = requirement.valuePlaceholderPattern().matcher(template);
        final Requirement<String> valueRequirement = requirement.valueRequirement();
        boolean result = matcher.find();
        if (result) {
            final StringBuffer sb = new StringBuffer();
            do {
                final String value = createBy(valueRequirement);
                matcher.appendReplacement(sb, quoteReplacement(value));
                result = matcher.find();
            } while (result);
            matcher.appendTail(sb);
            return sb.toString();
        }
        return template;
    }

    @Nonnull
    protected String createBy(@SuppressWarnings("unused") @Nonnull UniqueRequirement<String> requirement) {
        return Objects.toString(uniqueValueSupplier().get(), "");
    }

    @Nonnull
    protected Supplier<?> uniqueValueSupplier() {
        return uniqueValueSupplier;
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
