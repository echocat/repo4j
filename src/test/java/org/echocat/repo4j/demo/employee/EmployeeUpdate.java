package org.echocat.repo4j.demo.employee;

import org.echocat.repo4j.demo.employee.Employee.Department;
import org.echocat.repo4j.update.Update;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.ZonedDateTime;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

public class EmployeeUpdate implements Update<Employee> {

    @Nonnull
    private Optional<String> name = empty();
    @Nonnull
    private Optional<ZonedDateTime> birthday = empty();
    @Nonnull
    private Optional<Department> department = empty();

    @Nonnull
    public Optional<String> name() {
        return name;
    }

    @Nonnull
    public Optional<ZonedDateTime> birthday() {
        return birthday;
    }

    @Nonnull
    public Optional<Department> department() {
        return department;
    }

    public EmployeeUpdate setName(@Nullable String name) {
        this.name = ofNullable(name);
        return this;
    }

    public EmployeeUpdate setBirthday(@Nullable ZonedDateTime birthday) {
        this.birthday = ofNullable(birthday);
        return this;
    }

    public EmployeeUpdate setDepartment(@Nullable Department department) {
        this.department = ofNullable(department);
        return this;
    }

}
