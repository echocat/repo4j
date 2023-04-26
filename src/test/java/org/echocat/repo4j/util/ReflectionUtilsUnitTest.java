package org.echocat.repo4j.util;

import org.junit.Test;

import static org.echocat.repo4j.util.ReflectionUtils.toPropertyName;
import static org.echocat.unittest.utils.matchers.IsEqualTo.isEqualTo;
import static org.echocat.unittest.utils.matchers.OptionalMatchers.whereContentMatches;
import static org.hamcrest.MatcherAssert.assertThat;

public class ReflectionUtilsUnitTest {

    @Test
    public void testToPropertyName() {
        assertThat(toPropertyName("getFoo"), whereContentMatches(isEqualTo("foo")));
        assertThat(toPropertyName("getFooBar"), whereContentMatches(isEqualTo("fooBar")));
        assertThat(toPropertyName("isFoo"), whereContentMatches(isEqualTo("foo")));
        assertThat(toPropertyName("isFooBar"), whereContentMatches(isEqualTo("fooBar")));
        assertThat(toPropertyName("hasFoo"), whereContentMatches(isEqualTo("foo")));
        assertThat(toPropertyName("hasFooBar"), whereContentMatches(isEqualTo("fooBar")));

        assertThat(toPropertyName("getfooBar"), whereContentMatches(isEqualTo("getfooBar")));
        assertThat(toPropertyName("getfooBar"), whereContentMatches(isEqualTo("getfooBar")));
        assertThat(toPropertyName("get"), whereContentMatches(isEqualTo("get")));
        assertThat(toPropertyName("isfooBar"), whereContentMatches(isEqualTo("isfooBar")));
        assertThat(toPropertyName("isfooBar"), whereContentMatches(isEqualTo("isfooBar")));
        assertThat(toPropertyName("is"), whereContentMatches(isEqualTo("is")));
        assertThat(toPropertyName("hasfooBar"), whereContentMatches(isEqualTo("hasfooBar")));
        assertThat(toPropertyName("hasfooBar"), whereContentMatches(isEqualTo("hasfooBar")));
        assertThat(toPropertyName("has"), whereContentMatches(isEqualTo("has")));
        assertThat(toPropertyName("setFooBar"), whereContentMatches(isEqualTo("setFooBar")));
    }

}
