package org.fest.assertions.api;

import org.junit.Test;

import com.google.common.base.Optional;

/**
 * @author Kornel
 */
public class OptionalAssert_isPresent_Test extends BaseTest {

  @Test
  public void should_fail_when_expecting_absent_optional_to_be_present() {
    // given
    final Optional<Object> testedOptional = Optional.absent();

    // expect
    expectException(AssertionError.class, "Expecting <Optional.absent()> to be present");

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
