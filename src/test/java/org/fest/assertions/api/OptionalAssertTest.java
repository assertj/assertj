package org.fest.assertions.api;

import static org.junit.rules.ExpectedException.none;

import org.junit.ComparisonFailure;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.base.Optional;

/**
 * @author Kornel
 */
public class OptionalAssertTest {

    @Rule
    public ExpectedException thrown = none();

    @Test
    public void shouldFailWhenExpectedPresentOptionalIsAbset() {
        // given
        final Optional<String> testedOptional = Optional.of("X");

        // expect
        thrown.expect(AssertionError.class);
        thrown.expectMessage("Expecting <Optional.of(X)> to be absent");

        // when
        GUAVA.assertThat(testedOptional).isAbsent();
    }

    @Test
    public void shouldFailWhenExpectedValuesDiffer() {
        // given
        final Optional<String> testedOptional = Optional.of("Test");

        // expect
        thrown.expect(ComparisonFailure.class);

        // when
        GUAVA.assertThat(testedOptional).hasValue("Test 2");
    }

    @Test
    public void shouldFailWhenExpectingAbsentOptionalToBePresent() {
        // given
        final Optional<Object> testedOptional = Optional.absent();

        // expect
        thrown.expect(AssertionError.class);
        thrown.expectMessage("Expecting <Optional.absent()> to be present");

        // when
        GUAVA.assertThat(testedOptional).isPresent();
    }

    @Test
    public void shouldFailWhenExpectingValueFromAnAbsentOptional() {
        // given
        final Optional<String> testedOptional = Optional.absent();

        // expect
        thrown.expect(AssertionError.class);
        thrown.expectMessage("Expecting <Optional.absent()> to have value <'Test'>");

        // when
        GUAVA.assertThat(testedOptional).hasValue("Test");
    }
}
