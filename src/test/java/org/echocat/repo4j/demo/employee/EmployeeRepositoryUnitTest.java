package org.echocat.repo4j.demo.employee;

import org.echocat.repo4j.range.Range;
import org.junit.Test;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.ZonedDateTime;
import java.util.*;

import static java.time.ZonedDateTime.now;
import static java.util.Collections.addAll;
import static java.util.stream.Collectors.toList;
import static org.echocat.repo4j.create.Requirement.*;
import static org.echocat.repo4j.demo.employee.Employee.Department.accounting;
import static org.echocat.repo4j.demo.employee.Employee.Department.it;
import static org.echocat.repo4j.demo.employee.Employee.buildEmployee;
import static org.echocat.repo4j.demo.employee.EmployeeQuery.employee;
import static org.echocat.repo4j.demo.employee.EmployeeUpdate.updateEmployeeTo;
import static org.echocat.repo4j.range.Range.rangeOf;
import static org.echocat.unittest.utils.matchers.HasSize.hasSize;
import static org.echocat.unittest.utils.matchers.IsEqualTo.isEqualTo;
import static org.echocat.unittest.utils.matchers.IsEqualTo.isTrue;
import static org.echocat.unittest.utils.matchers.IsNull.isNotNull;
import static org.echocat.unittest.utils.matchers.IsSameAs.isSameInstance;
import static org.echocat.unittest.utils.matchers.IterableMatchers.containsAtLeastOneElementThat;
import static org.echocat.unittest.utils.matchers.StringMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings({"ConstantConditions", "OptionalGetWithoutIsPresent"})
public class EmployeeRepositoryUnitTest {

    @Test
    public void createBy() {
        final EmployeeRepository instance = givenInstance();
        final ZonedDateTime now = now();
        final Range<ZonedDateTime> birthdayRange = rangeOf(now.minusYears(50), now.minusYears(10));

        assertThat(instance.employees(), hasSize(0L));

        final Employee actual1 = instance.createBy(new EmployeeRequirement()
            .setName(templateBasedRequirementOf("Employee $$", uniqueRequirement()))
            .setBirthday(rangeRequirementOf(birthdayRange))
            .setDepartment(fixedRequirementOf(it))
        );

        assertThat(actual1, isNotNull());
        assertThat(actual1.id(), isNotNull());
        assertThat(actual1.name(), startsWith("Employee "));
        assertThat(actual1.birthday().isAfter(birthdayRange.from().get()), isTrue());
        assertThat(actual1.birthday().isBefore(birthdayRange.to().get()), isTrue());
        assertThat(actual1.department(), isEqualTo(it));

        assertThat(instance.employees(), hasSize(1L));

        final Employee actual2 = instance.findBy(new EmployeeQuery()).findFirst().get();

        assertThat(actual2, isEqualTo(actual1));
    }

    @Test
    public void update() {
        final ZonedDateTime timeA = now();
        final ZonedDateTime timeB = now().minusYears(10);
        final UUID id = new UUID(1, 2);
        final Employee employee = buildEmployee()
            .withId(id)
            .withName("foo")
            .withBirthday(timeA)
            .withDepartment(it)
            .build();

        final EmployeeRepository instance = givenInstance(employee);

        instance.update(
            employee()
                .withId(id),
            updateEmployeeTo()
                .withName("bar")
                .withBirthday(timeB)
        );

        final Employee actual = instance.getOneBy(employee().withId(id));
        assertThat(actual.id(), isSameInstance(id));
        assertThat(actual.name(), isEqualTo("bar"));
        assertThat(actual.birthday(), isSameInstance(timeB));
        assertThat(actual.department(), isSameInstance(it));
    }

    @Test
    public void findBy() {
        final Employee employeeA = buildEmployee()
            .withId(UUID.randomUUID())
            .withName("foo")
            .withBirthday(now().minusYears(10))
            .withDepartment(it)
            .build();
        final Employee employeeB = buildEmployee()
            .withId(UUID.randomUUID())
            .withName("bar")
            .withBirthday(now().minusYears(20))
            .withDepartment(accounting)
            .build();
        final EmployeeRepository instance = givenInstance(employeeA, employeeB);

        final List<Employee> actual = instance.findBy(new EmployeeQuery()
                .withName("bar")
            , toList());

        assertThat(actual, hasSize(1L));
        assertThat(actual, containsAtLeastOneElementThat(isSameInstance(employeeB)));
    }

    @Test
    public void removeBy() {
        final Employee employeeA = buildEmployee()
            .withId(UUID.randomUUID())
            .withName("foo")
            .withBirthday(now().minusYears(10))
            .withDepartment(it)
            .build();
        final Employee employeeB = buildEmployee()
            .withId(UUID.randomUUID())
            .withName("bar")
            .withBirthday(now().minusYears(20))
            .withDepartment(accounting)
            .build();
        final EmployeeRepository instance = givenInstance(employeeA, employeeB);

        assertThat(instance.employees(), hasSize(2L));
        assertThat(instance.employees(), containsAtLeastOneElementThat(isSameInstance(employeeA)));
        assertThat(instance.employees(), containsAtLeastOneElementThat(isSameInstance(employeeB)));

        instance.removeBy(new EmployeeQuery()
            .withName("foo")
        );

        assertThat(instance.employees(), hasSize(1L));
        assertThat(instance.employees(), containsAtLeastOneElementThat(isSameInstance(employeeB)));
    }

    @Nonnull
    private static EmployeeRepository givenInstance(@Nullable Employee... employees) {
        final Set<Employee> target = new HashSet<>();
        if (employees != null) {
            addAll(target, employees);
        }
        return new EmployeeRepository(target);
    }


}
