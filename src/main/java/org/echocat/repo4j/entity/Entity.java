package org.echocat.repo4j.entity;

import javax.annotation.Nonnull;

public interface Entity {

    public interface Builder<E extends Entity, B extends Builder<E, B>> {

        @Nonnull
        B with(@Nonnull E base);

        @Nonnull
        E build();

    }

}
