package org.echocat.repo4j.demo.employee;

import org.echocat.repo4j.IdEnabled;

import javax.annotation.Nonnull;
import java.time.ZonedDateTime;
import java.util.UUID;

public interface Employee extends IdEnabled<UUID> {

    @Override
    @Nonnull
    UUID getId();

    @Nonnull
    String getName();

    @Nonnull
    ZonedDateTime getBirthday();

    @Nonnull
    Department getDepartment();

    public enum Department {
        it,
        accounting,
        mangement
    }

}
