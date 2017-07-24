package org.echocat.repo4j.demo.employee;

import org.echocat.repo4j.entity.ManagedEntity;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.echocat.repo4j.util.OptionalUtils.ofNonnull;
import static org.echocat.repo4j.util.OptionalUtils.valueOf;

@Immutable
public class Employee extends ManagedEntity.Base<UUID> {

    @Nonnull
    public static Builder buildEmployee() {
        return new Builder();
    }

    @Nonnull
    private final String name;
    @Nonnull
    private final ZonedDateTime birthday;
    @Nonnull
    private final Department department;

    protected Employee(
        @Nonnull UUID uuid,
        @Nonnull LocalDateTime created,
        @Nonnull LocalDateTime lastModified,
        @Nonnull String name,
        @Nonnull ZonedDateTime birthday,
        @Nonnull Department department
    ) {
        super(Employee.class, uuid, created, lastModified);
        this.name = name;
        this.birthday = birthday;
        this.department = department;
    }

    @Nonnull
    public String name() {
        return name;
    }

    @Nonnull
    public ZonedDateTime birthday() {
        return birthday;
    }

    @Nonnull
    public Department department() {
        return department;
    }

    public static class Builder extends ManagedEntity.BaseBuilder<UUID, Employee, Builder> {

        @Nonnull
        private Optional<String> name = Optional.empty();
        @Nonnull
        private Optional<ZonedDateTime> birthday = Optional.empty();
        @Nonnull
        private Optional<Department> department = Optional.empty();

        @Nonnull
        public Builder withName(@Nonnull String name) {
            this.name = ofNonnull(name, "name");
            return this;
        }

        @Nonnull
        public Builder withBirthday(@Nonnull ZonedDateTime birthday) {
            this.birthday = ofNonnull(birthday, "birthday");
            return this;
        }

        @Nonnull
        public Builder withDepartment(@Nonnull Department department) {
            this.department = ofNonnull(department, "department");
            return this;
        }

        @Override
        protected void withInternal(@Nonnull Employee base) {
            withName(base.name());
            withBirthday(base.birthday());
            withDepartment(base.department());
        }

        @Nonnull
        @Override
        protected Employee buildInternal(
            @Nonnull UUID uuid,
            @Nonnull LocalDateTime created,
            @Nonnull LocalDateTime lastModified
        ) {
            return new Employee(
                uuid,
                created,
                lastModified,
                valueOf(name, "name"),
                valueOf(birthday, "birthday"),
                valueOf(department, "department")
            );
        }

    }

    public enum Department {
        it,
        accounting,
        mangement
    }

}
