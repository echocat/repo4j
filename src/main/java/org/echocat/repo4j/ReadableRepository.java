package org.echocat.repo4j;

import org.echocat.repo4j.matching.Query;
import org.echocat.repo4j.matching.Retriever;

@FunctionalInterface
public interface ReadableRepository<T, Q extends Query<T>> extends Repository, Retriever<T, Q> {}
