package org.fest.assertions.api;

import static org.fest.assertions.api.GUAVA.assertThat;
import static org.fest.util.FailureMessages.actualIsNull;

import org.junit.Test;

import com.google.common.base.Optional;

/**
 * @author Kornel
 * @author Joel Costigliola
 */
public class OptionalAssert_hasValue_Test extends BaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    // given
    Optional<String> actual = null;
    // expect
    expectException(AssertionError.class, actualIsNull());
    // when
    assertThat(actual).hasValue("Test 2");
  }

  @Test
  public void should_fail_when_expected_values_differ() {
    // given
    final Optional<String> testedOptional = Optional.of("Test");
    // expect
    thrown.expect(AssertionError.class);
    // when
    assertThat(testedOptional).hasValue("Test 2");
  }

  @Test
  public void should_fail_when_expecting_value_from_an_absent_optional() {
    // given
    final Optional<String> testedOptional = Optional.absent();
    // expect
    expectException(AssertionError.class, "Expecting <Optional.absent()> to have value <'Test'>");
    // when
    assertThat(testedOptional).hasValue("Test");
  }

  @Test
  public void should_pass_when_actual_has_expected_value() {
    // given
    final Optional<String> testedOptional = Optional.of("Test");
    // when
    assertThat(testedOptional).hasValue("Test");
    // then
    // pass
  }
}
