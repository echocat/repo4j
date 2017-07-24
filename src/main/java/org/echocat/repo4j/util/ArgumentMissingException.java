package org.echocat.repo4j.util;

import javax.annotation.Nonnull;

public class ArgumentMissingException extends ArgumentConditionFailedException {

    public ArgumentMissingException(@Nonnull String argumentName) {
        super(argumentName + " not provided");
    }

}
