package org.echocat.repo4j.demo.employee;

import org.echocat.repo4j.demo.employee.Employee.Department;
import org.echocat.repo4j.matching.Query;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static java.util.regex.Pattern.*;

public class EmployeeQuery implements Query<Employee>, Predicate<Employee> {

    @Nonnull
    public static EmployeeQuery employee() {
        return new EmployeeQuery();
    }

    @Nonnull
    private final Set<UUID> id = new HashSet<>();
    @Nonnull
    private Optional<Pattern> name = empty();
    @Nonnull
    private final Set<Department> department = new HashSet<>();

    @Nonnull
    public Set<UUID> id() {
        return Collections.unmodifiableSet(id);
    }

    @Nonnull
    public Optional<Pattern> name() {
        return name;
    }

    @Nonnull
    public Set<Department> department() {
        return Collections.unmodifiableSet(department);
    }

    @Nonnull
    public EmployeeQuery withId(@Nonnull UUID id) {
        this.id.add(id);
        return this;
    }

    @Nonnull
    public EmployeeQuery withIds(@Nullable UUID... ids) {
        if (ids != null) {
            for (final UUID id : ids) {
                withId(id);
            }
        }
        return this;
    }

    @Nonnull
    public EmployeeQuery withName(@Nullable Pattern name) {
        this.name = ofNullable(name);
        return this;
    }

    @Nonnull
    public EmployeeQuery withName(@Nullable String name) {
        return withName(name != null ? compile(name, LITERAL) : null);
    }

    @Nonnull
    public EmployeeQuery withNameStaringWith(@Nullable String name) {
        return withName(name != null ? compile(quote(name) + ".*") : null);
    }

    @Nonnull
    public EmployeeQuery withDepartment(@Nonnull Department department) {
        this.department.add(department);
        return this;
    }

    @Nonnull
    public EmployeeQuery withDepartments(@Nullable Department... departments) {
        if (departments != null) {
            for (final Department department : departments) {
                withDepartment(department);
            }
        }
        return this;
    }

    @Override
    public boolean test(@Nullable Employee employee) {
        return employee != null
            && testId(employee)
            && testName(employee)
            && testDepartment(employee)
            ;
    }

    protected boolean testId(@Nonnull Employee employee) {
        final Set<UUID> id = id();
        return id.isEmpty() || id.contains(employee.id());
    }

    protected boolean testName(@Nonnull Employee employee) {
        return name()
            .map(pattern -> pattern.matcher(employee.name()).matches())
            .orElse(true);
    }

    protected boolean testDepartment(@Nonnull Employee employee) {
        final Set<Department> department = department();
        return department.isEmpty() || department.contains(employee.department());
    }

}
