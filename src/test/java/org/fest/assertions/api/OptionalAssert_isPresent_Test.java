package org.fest.assertions.api;

import static org.fest.assertions.api.GUAVA.assertThat;
import static org.fest.util.FailureMessages.actualIsNull;

import org.junit.Test;

import com.google.common.base.Optional;

/**
 * @author Kornel
 * @author Joel Costigliola
 */
public class OptionalAssert_isPresent_Test extends BaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    // given
    Optional<String> actual = null;
    // expect
    expectException(AssertionError.class, actualIsNull());
    // when
    assertThat(actual).isPresent();
  }

  @Test
  public void should_fail_when_expecting_absent_optional_to_be_present() {
    // given
    final Optional<Object> testedOptional = Optional.absent();
    // expect
    expectException(AssertionError.class,
        "Expecting Optional to contain a non-null instance but contained nothing (absent Optional)");
    // when
    assertThat(testedOptional).isPresent();
  }

  @Test
  public void should_pass_when_optional_is_present() {
    // given
    final Optional<Object> testedOptional = Optional.of(new Object());
    // when
    assertThat(testedOptional).isPresent();
    // then
    // pass
  }

}
