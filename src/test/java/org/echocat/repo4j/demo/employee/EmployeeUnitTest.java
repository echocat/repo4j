package org.echocat.repo4j.demo.employee;

import org.junit.Test;

import static java.time.ZonedDateTime.now;
import static java.util.UUID.randomUUID;
import static org.echocat.repo4j.demo.employee.Employee.Department.it;
import static org.echocat.repo4j.demo.employee.Employee.buildEmployee;
import static org.echocat.unittest.utils.matchers.IsEqualTo.isEqualTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class EmployeeUnitTest {

    @Test
    public void rightOutputOfToString() {
        final Employee employee = buildEmployee()
            .withId(randomUUID())
            .withName("name")
            .withBirthday(now())
            .withDepartment(it)
            .build();
        assertThat(employee.toString(), isEqualTo(Employee.class.getSimpleName() + "#" + employee.id() + "{" +
            "birthday=" + employee.birthday() +
            ", created=" + employee.created() +
            ", department=" + employee.department() +
            ", lastModified=" + employee.lastModified() +
            ", name='" + employee.name() + "'" +
            "}"
        ));
    }

}
