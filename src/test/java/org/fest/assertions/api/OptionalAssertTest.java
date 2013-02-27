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
  public void should_fail_when_expected_present_optional_is_abset() {
    // given
    final Optional<String> testedOptional = Optional.of("X");

    // expect
    thrown.expect(AssertionError.class);
    thrown.expectMessage("Expecting <Optional.of(X)> to be absent");

    // when
    GUAVA.assertThat(testedOptional).isAbsent();
  }

  @Test
  public void should_fail_when_expected_values_differ() {
    // given
    final Optional<String> testedOptional = Optional.of("Test");

    // expect
    thrown.expect(ComparisonFailure.class);

    // when
    GUAVA.assertThat(testedOptional).hasValue("Test 2");
  }

  @Test
  public void should_fail_when_expecting_absent_optional_to_be_present() {
    // given
    final Optional<Object> testedOptional = Optional.absent();

    // expect
    thrown.expect(AssertionError.class);
    thrown.expectMessage("Expecting <Optional.absent()> to be present");

    // when
    GUAVA.assertThat(testedOptional).isPresent();
  }

  @Test
  public void should_fail_when_expecting_value_from_an_absent_optional() {
    // given
    final Optional<String> testedOptional = Optional.absent();

    // expect
    thrown.expect(AssertionError.class);
    thrown.expectMessage("Expecting <Optional.absent()> to have value <'Test'>");

    // when
    GUAVA.assertThat(testedOptional).hasValue("Test");
  }
}
