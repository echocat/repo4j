package org.echocat.repo4j;

import org.echocat.repo4j.create.Creator;
import org.echocat.repo4j.create.Requirement;
import org.echocat.repo4j.matching.Query;
import org.echocat.repo4j.remove.Remover;
import org.echocat.repo4j.update.Update;
import org.echocat.repo4j.update.Updater;

public interface ModifiableRepository<T,
    Q extends Query<T>,
    R extends Requirement<T>,
    U extends Update<T>> extends
    ReadableRepository<T, Q>,
    Creator<T, R>,
    Updater<T, U, Q>,
    Remover<T, Q>
{}
