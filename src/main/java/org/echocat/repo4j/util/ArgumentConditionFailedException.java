package org.echocat.repo4j.util;

public class ArgumentConditionFailedException extends IllegalArgumentException {

    public ArgumentConditionFailedException(String s) {
        super(s);
    }

    public ArgumentConditionFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArgumentConditionFailedException(Throwable cause) {
        super(cause);
    }

}
