package org.echocat.repo4j.remove;

import org.echocat.repo4j.matching.Query;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

@FunctionalInterface
public interface Remover<T, Q extends Query<T>> {

    /**
     * Removes all matching entities based on given query.
     *
     * @return Returns the number of removed elements.
     *         If not supported or not feasible <code>null</code> is returned.
     */
    @Nonnegative
    Long removeBy(@Nonnull Q query);

}
