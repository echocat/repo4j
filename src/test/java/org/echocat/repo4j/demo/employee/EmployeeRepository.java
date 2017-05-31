package org.echocat.repo4j.demo.employee;

import org.echocat.repo4j.ModifiableRepository;
import org.echocat.repo4j.create.Creator;
import org.echocat.repo4j.create.Requirement;
import org.echocat.repo4j.create.StringCreator;
import org.echocat.repo4j.create.ZonedDateTimeCreator;
import org.echocat.repo4j.demo.employee.Employee.Department;
import org.echocat.repo4j.util.UniqueValueCreator;

import javax.annotation.Nonnull;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static org.echocat.repo4j.create.StringCreator.stringCreator;
import static org.echocat.repo4j.create.ZonedDateTimeCreator.zonedDateTimeCreator;
import static org.echocat.repo4j.util.UniqueValueCreator.uuid;

public class EmployeeRepository implements ModifiableRepository<Employee, EmployeeQuery, EmployeeRequirement, EmployeeUpdate> {

    @Nonnull
    private final Set<EmployeeImpl> employees;

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

    protected EmployeeRepository(@Nonnull Set<EmployeeImpl> employees) {
        this.employees = employees;
    }

    @Nonnull
    @Override
    public Employee createBy(@Nonnull EmployeeRequirement requirement) {
        final EmployeeImpl employee = new EmployeeImpl()
            .setId(idCreator.next())
            .setName(requirement.name().map(nameCreator::createBy).orElseThrow(() -> new IllegalArgumentException("No name specified in requirement.")))
            .setBirthday(requirement.birthday().map(birthdayCreator::createBy).orElseThrow(() -> new IllegalArgumentException("No birthday specified in requirement.")))
            .setDepartment(requirement.department().map(departmentCreator::createBy).orElseThrow(() -> new IllegalArgumentException("No department specified in requirement.")))
            ;
        employees().add(employee);
        return employee;
    }

    @Override
    public void update(@Nonnull EmployeeQuery query, @Nonnull EmployeeUpdate by) {
        findByInternal(query).forEach(candidate -> {
            by.name().ifPresent(candidate::setName);
            by.birthday().ifPresent(candidate::setBirthday);
            by.department().ifPresent(candidate::setDepartment);
        });
    }

    @Nonnull
    @Override
    public Stream<? extends Employee> findBy(@Nonnull EmployeeQuery query) {
        return findByInternal(query);
    }

    @Nonnull
    private Stream<EmployeeImpl> findByInternal(@Nonnull EmployeeQuery query) {
        return employees().stream()
            .filter(query);
    }

    @Override
    public Long removeBy(@Nonnull EmployeeQuery query) {
        employees().removeIf(query);
        return null;
    }

    @Nonnull
    protected Set<EmployeeImpl> employees() {
        return employees;
    }

    protected static class EmployeeImpl implements Employee {

        private UUID id;
        private String name;
        private ZonedDateTime birthday;
        private Department department;

        @Nonnull
        @Override
        public UUID getId() {
            return id;
        }

        @Nonnull
        @Override
        public String getName() {
            return name;
        }

        @Nonnull
        @Override
        public ZonedDateTime getBirthday() {
            return birthday;
        }

        @Nonnull
        @Override
        public Department getDepartment() {
            return department;
        }

        @Nonnull
        protected EmployeeImpl setId(UUID id) {
            this.id = id;
            return this;
        }

        @Nonnull
        protected EmployeeImpl setName(String name) {
            this.name = name;
            return this;
        }

        @Nonnull
        protected EmployeeImpl setBirthday(ZonedDateTime birthday) {
            this.birthday = birthday;
            return this;
        }

        @Nonnull
        protected EmployeeImpl setDepartment(Department department) {
            this.department = department;
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) { return true; }
            if (o == null || getClass() != o.getClass()) { return false; }
            final EmployeeImpl employee = (EmployeeImpl) o;
            return Objects.equals(getId(), employee.getId());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getId());
        }

        @Override
        public String toString() {
            return "EmployeeImpl{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                ", department=" + department +
                '}';
        }

    }

}
