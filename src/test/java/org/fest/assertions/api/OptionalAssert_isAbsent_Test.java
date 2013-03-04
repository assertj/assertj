package org.fest.assertions.api;

import static org.fest.assertions.api.GUAVA.assertThat;
import static org.fest.util.FailureMessages.actualIsNull;

import org.junit.Test;

import com.google.common.base.Optional;

/**
 * @author Kornel
 * @author Joel Costigliola
 */
public class OptionalAssert_isAbsent_Test extends BaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    // given
    Optional<String> actual = null;
    // expect
    expectException(AssertionError.class, actualIsNull());
    // when
    assertThat(actual).isAbsent();
  }

  @Test
  public void should_fail_when_expected_present_optional_is_absent() {
    // given
    final Optional<String> testedOptional = Optional.of("X");
    // expect
    expectException(AssertionError.class, "Expecting Optional to contain nothing (absent Optional) but contained <'X'>");
    // when
    assertThat(testedOptional).isAbsent();
  }

  @Test
  public void should_pass_when_optional_is_absent() {
    // given
    final Optional<String> testedOptional = Optional.absent();
    // when
    assertThat(testedOptional).isAbsent();
    // then
    // pass
  }

}
