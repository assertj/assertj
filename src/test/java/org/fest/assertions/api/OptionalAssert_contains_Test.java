package org.fest.assertions.api;

import static org.fest.assertions.api.GUAVA.assertThat;
import static org.fest.util.FailureMessages.actualIsNull;

import org.junit.Test;

import com.google.common.base.Optional;

/**
 * @author Kornel
 * @author Joel Costigliola
 */
public class OptionalAssert_contains_Test extends BaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    // given
    Optional<String> actual = null;
    // expect
    expectException(AssertionError.class, actualIsNull());
    // when
    assertThat(actual).contains("Test 2");
  }

  @Test
  public void should_fail_when_option_does_not_contain_expected_value() {
    // given
    final Optional<String> testedOptional = Optional.of("Test");
    // expect
    expectException(AssertionError.class,
        "\nExpecting Optional to contain value \n<'Test 2'>\n but contained \n<'Test'>");
    // when
    assertThat(testedOptional).contains("Test 2");
  }

  @Test
  public void should_fail_when_optional_contains_nothing() {
    // given
    final Optional<String> testedOptional = Optional.absent();
    // expect
    expectException(AssertionError.class,
        "Expecting Optional to contain <'Test'> but contained nothing (absent Optional)");
    // when
    assertThat(testedOptional).contains("Test");
  }

  @Test
  public void should_pass_when_actual_contains_expected_value() {
    // given
    final Optional<String> testedOptional = Optional.of("Test");
    // when
    assertThat(testedOptional).contains("Test");
    // then
    // pass
  }
}
