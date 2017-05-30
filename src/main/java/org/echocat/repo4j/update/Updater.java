package org.echocat.repo4j.update;

import org.echocat.repo4j.matching.Query;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface Updater<T, U extends Update<T>, Q extends Query<T>> {

    /**
     * Update all entities that matches the given query by given update (by).
     */
    void update(@Nonnull Q query, @Nonnull U by);

}
