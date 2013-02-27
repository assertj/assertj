package org.fest.assertions.api;

import static org.junit.rules.ExpectedException.none;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.base.Optional;

/**
 * @author Kornel
 */
public class OptionalAssert_isPresent_Test {

  @Rule
  public ExpectedException thrown = none();

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
  public void should_pass_when_optional_is_present() {
    // given
    final Optional<Object> testedOptional = Optional.of(new Object());

    // when
    GUAVA.assertThat(testedOptional).isPresent();

    // then
    // pass
  }

}
