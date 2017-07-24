package org.echocat.repo4j.demo.employee;

import org.echocat.repo4j.demo.employee.Employee.Builder;
import org.echocat.repo4j.demo.employee.Employee.Department;
import org.echocat.repo4j.update.Update;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.ZonedDateTime;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.echocat.repo4j.demo.employee.Employee.buildEmployee;

public class EmployeeUpdate implements Update<Employee> {

    @Nonnull
    public static EmployeeUpdate updateEmployeeTo() {
        return new EmployeeUpdate();
    }

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

    @Nonnull
    public EmployeeUpdate withName(@Nullable String name) {
        this.name = ofNullable(name);
        return this;
    }

    @Nonnull
    public EmployeeUpdate withBirthday(@Nullable ZonedDateTime birthday) {
        this.birthday = ofNullable(birthday);
        return this;
    }

    @Nonnull
    public EmployeeUpdate withDepartment(@Nullable Department department) {
        this.department = ofNullable(department);
        return this;
    }

    @Nonnull
    public Employee update(@Nonnull Employee input) {
        final Builder builder = buildEmployee().with(input);
        name().ifPresent(builder::withName);
        birthday().ifPresent(builder::withBirthday);
        department().ifPresent(builder::withDepartment);
        return builder.build();
    }

}
