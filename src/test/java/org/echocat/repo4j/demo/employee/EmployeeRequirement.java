package org.echocat.repo4j.demo.employee;

import org.echocat.repo4j.create.Requirement;
import org.echocat.repo4j.demo.employee.Employee.Department;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.ZonedDateTime;
import java.util.Optional;

import static java.util.Optional.ofNullable;

public class EmployeeRequirement implements Requirement<Employee> {

    @Nonnull
    private Optional<Requirement<String>> name = Optional.empty();
    @Nonnull
    private Optional<Requirement<ZonedDateTime>> birthday = Optional.empty();
    @Nonnull
    private Optional<Requirement<Department>> department = Optional.empty();

    @Nonnull
    public Optional<Requirement<String>> name() {
        return name;
    }

    @Nonnull
    public Optional<Requirement<ZonedDateTime>> birthday() {
        return birthday;
    }

    @Nonnull
    public Optional<Requirement<Department>> department() {
        return department;
    }

    @Nonnull
    public EmployeeRequirement setName(@Nullable Requirement<String> name) {
        this.name = ofNullable(name);
        return this;
    }

    @Nonnull
    public EmployeeRequirement setBirthday(@Nullable Requirement<ZonedDateTime> birthday) {
        this.birthday = ofNullable(birthday);
        return this;
    }

    @Nonnull
    public EmployeeRequirement setDepartment(@Nullable Requirement<Department> department) {
        this.department = ofNullable(department);
        return this;
    }

}
