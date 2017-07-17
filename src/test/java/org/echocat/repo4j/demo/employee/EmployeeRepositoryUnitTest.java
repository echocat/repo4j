package org.echocat.repo4j.demo.employee;

import org.echocat.repo4j.demo.employee.EmployeeRepository.EmployeeImpl;
import org.echocat.repo4j.range.Range;
import org.junit.Test;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static java.time.ZonedDateTime.now;
import static java.util.Collections.addAll;
import static java.util.stream.Collectors.toList;
import static org.echocat.repo4j.create.Requirement.*;
import static org.echocat.repo4j.demo.employee.Employee.Department.accounting;
import static org.echocat.repo4j.demo.employee.Employee.Department.it;
import static org.echocat.repo4j.range.Range.rangeOf;
import static org.echocat.unittest.utils.matchers.HasSize.hasSize;
import static org.echocat.unittest.utils.matchers.IsEqualTo.isEqualTo;
import static org.echocat.unittest.utils.matchers.IsEqualTo.isTrue;
import static org.echocat.unittest.utils.matchers.IsNull.isNotNull;
import static org.echocat.unittest.utils.matchers.IsSameAs.isSameInstance;
import static org.echocat.unittest.utils.matchers.IterableMatchers.containsAtLeastOneElementThat;
import static org.echocat.unittest.utils.matchers.StringMatchers.startsWith;
import static org.junit.Assert.assertThat;

@SuppressWarnings("ConstantConditions")
public class EmployeeRepositoryUnitTest {

    @Test
    public void createBy() throws Exception {
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
        assertThat(actual1.getId(), isNotNull());
        assertThat(actual1.getName(), startsWith("Employee "));
        assertThat(actual1.getBirthday().isAfter(birthdayRange.from().get()), isTrue());
        assertThat(actual1.getBirthday().isBefore(birthdayRange.to().get()), isTrue());
        assertThat(actual1.getDepartment(), isEqualTo(it));

        assertThat(instance.employees(), hasSize(1L));

        final Employee actual2 = instance.findBy(new EmployeeQuery()).findFirst().get();

        assertThat(actual2, isEqualTo(actual1));
    }

    @Test
    public void update() throws Exception {
        final ZonedDateTime timeA = now();
        final ZonedDateTime timeB = now().minusYears(10);
        final UUID id = new UUID(1, 2);
        final EmployeeImpl employee = new EmployeeImpl()
            .setId(id)
            .setName("foo")
            .setBirthday(timeA)
            .setDepartment(it);

        final EmployeeRepository instance = givenInstance(employee);

        instance.update(
            new EmployeeQuery()
                .withId(id),
            new EmployeeUpdate()
                .setName("bar")
                .setBirthday(timeB)
        );

        assertThat(employee.getId(), isSameInstance(id));
        assertThat(employee.getName(), isEqualTo("bar"));
        assertThat(employee.getBirthday(), isSameInstance(timeB));
        assertThat(employee.getDepartment(), isSameInstance(it));
    }

    @Test
    public void findBy() throws Exception {
        final EmployeeImpl employeeA = new EmployeeImpl()
            .setId(UUID.randomUUID())
            .setName("foo")
            .setBirthday(now().minusYears(10))
            .setDepartment(it);
        final EmployeeImpl employeeB = new EmployeeImpl()
            .setId(UUID.randomUUID())
            .setName("bar")
            .setBirthday(now().minusYears(20))
            .setDepartment(accounting);
        final EmployeeRepository instance = givenInstance(employeeA, employeeB);

        final List<Employee> actual = instance.findBy(new EmployeeQuery()
                .withName("bar")
            , toList());

        assertThat(actual, hasSize(1L));
        assertThat(actual, containsAtLeastOneElementThat(isSameInstance(employeeB)));
    }

    @Test
    public void removeBy() throws Exception {
        final EmployeeImpl employeeA = new EmployeeImpl()
            .setId(UUID.randomUUID())
            .setName("foo")
            .setBirthday(now().minusYears(10))
            .setDepartment(it);
        final EmployeeImpl employeeB = new EmployeeImpl()
            .setId(UUID.randomUUID())
            .setName("bar")
            .setBirthday(now().minusYears(20))
            .setDepartment(accounting);
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
    private static EmployeeRepository givenInstance(@Nullable EmployeeImpl... employees) {
        final Set<EmployeeImpl> target = new HashSet<>();
        if (employees != null) {
            addAll(target, employees);
        }
        return new EmployeeRepository(target);
    }


}
