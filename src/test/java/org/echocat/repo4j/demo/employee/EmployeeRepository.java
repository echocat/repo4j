package org.echocat.repo4j.demo.employee;

import org.echocat.repo4j.ModifiableRepository;
import org.echocat.repo4j.create.Creator;
import org.echocat.repo4j.create.Requirement;
import org.echocat.repo4j.create.StringCreator;
import org.echocat.repo4j.create.ZonedDateTimeCreator;
import org.echocat.repo4j.demo.employee.Employee.Department;
import org.echocat.repo4j.util.UniqueValueCreator;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static org.echocat.repo4j.create.StringCreator.stringCreator;
import static org.echocat.repo4j.create.ZonedDateTimeCreator.zonedDateTimeCreator;
import static org.echocat.repo4j.demo.employee.Employee.buildEmployee;
import static org.echocat.repo4j.util.OptionalUtils.valueOf;
import static org.echocat.repo4j.util.UniqueValueCreator.uuid;

public class EmployeeRepository implements ModifiableRepository<Employee, EmployeeQuery, EmployeeRequirement, EmployeeUpdate> {

    @Nonnull
    private final Set<Employee> employees;

    @Nonnull
    private final UniqueValueCreator<UUID> idCreator = uuid().build();
    @Nonnull
    private final StringCreator nameCreator = stringCreator().build();
    @Nonnull
    private final ZonedDateTimeCreator birthdayCreator = zonedDateTimeCreator().build();
    @Nonnull
    private final Creator<Department, Requirement<Department>> departmentCreator = Creator.genericGeneratorFor(Department.class);

    public EmployeeRepository() {
        this(new HashSet<>());
    }

    protected EmployeeRepository(@Nonnull Set<Employee> employees) {
        this.employees = employees;
    }

    @Nonnull
    @Override
    public Employee createBy(@Nonnull EmployeeRequirement requirement) {
        final Employee employee = buildEmployee()
            .withId(idCreator.next())
            .withName(valueOf(requirement.name().map(nameCreator::createBy), "name"))
            .withBirthday(valueOf(requirement.birthday().map(birthdayCreator::createBy), "birthday"))
            .withDepartment(valueOf(requirement.department().map(departmentCreator::createBy), "department"))
            .build();
        employees().add(employee);
        return employee;
    }

    @Override
    public void update(@Nonnull EmployeeQuery query, @Nonnull EmployeeUpdate by) {
        final Iterator<Employee> i = employees().iterator();
        final Set<Employee> updated = new HashSet<>();
        while (i.hasNext()) {
            final Employee candidate = i.next();
            if (query.test(candidate)) {
                updated.add(by.update(candidate));
                i.remove();
            }
        }
        employees().addAll(updated);
    }

    @Nonnull
    @Override
    public Stream<? extends Employee> findBy(@Nonnull EmployeeQuery query) {
        return findByInternal(query);
    }

    @Nonnull
    private Stream<Employee> findByInternal(@Nonnull EmployeeQuery query) {
        return employees().stream()
            .filter(query);
    }

    @Override
    public Long removeBy(@Nonnull EmployeeQuery query) {
        employees().removeIf(query);
        return null;
    }

    @Nonnull
    protected Set<Employee> employees() {
        return employees;
    }

}
