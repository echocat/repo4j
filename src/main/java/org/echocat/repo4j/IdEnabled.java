package org.echocat.repo4j;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface IdEnabled<ID> {

    @Nonnull
    ID getId();

}
