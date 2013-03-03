package org.fest.assertions.api;

import org.junit.Test;

import com.google.common.base.Optional;

/**
 * @author Kornel
 */
public class OptionalAssert_isAbsent_Test extends BaseTest {

  @Test
  public void should_fail_when_expected_present_optional_is_absent() {
    // given
    final Optional<String> testedOptional = Optional.of("X");

    // expect
    expectException(AssertionError.class, "Expecting <Optional.of(X)> to be absent");

    // when
    GUAVA.assertThat(testedOptional).isAbsent();
  }

  @Test
  public void should_pass_when_optional_is_absent() {
    // given
    final Optional<String> testedOptional = Optional.absent();

    // when
    GUAVA.assertThat(testedOptional).isAbsent();

    // then
    // pass
  }

}
