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
public class OptionalAssert_hasValue_Test {

  @Rule
  public ExpectedException thrown = none();

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
