package org.fest.assertions.api;

import static org.junit.rules.ExpectedException.none;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.base.Optional;

/**
 * @author Kornel
 */
public class OptionalAssert_isAbsent_Test {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_fail_when_expected_present_optional_is_absent() {
    // given
    final Optional<String> testedOptional = Optional.of("X");

    // expect
    thrown.expect(AssertionError.class);
    thrown.expectMessage("Expecting <Optional.of(X)> to be absent");

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
