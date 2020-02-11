package org.assertj.core.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldHaveCause.shouldHaveCause;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

@DisplayName("ThrowableAssertAlternative havingCause")
class ThrowableAssertAlternative_havingCause_Test {

  @Test
  void should_return_cause_if_throwable_has_cause() {
    Throwable cause = new Throwable("cause message");
    Throwable throwable = new Throwable("top level message", cause);
    assertThat(new ThrowableAssertAlternative<>(throwable)
                 .havingCause().actual)
      .isSameAs(cause);
  }

  @Test
  void should_fail_if_throwable_has_no_cause() {
    //GIVEN
    Throwable throwable = new Throwable("top level message");
    ThrowableAssertAlternative<Throwable> taa = new ThrowableAssertAlternative<>(throwable);
    //WHEN
    AssertionError error = expectAssertionError(taa::havingCause);
    //THEN
    assertThat(error).hasMessage(shouldHaveCause(throwable).create());
  }

  @Test
  void should_fail_if_throwable_is_null() {
    //GIVEN
    Throwable throwable = null;
    ThrowableAssertAlternative<Throwable> taa = new ThrowableAssertAlternative<>(throwable);
    //WHEN
    ThrowableAssert.ThrowingCallable code = taa::havingCause;
    AssertionError error = expectAssertionError(code);
    //THEN
    assertThat(error).hasMessage(shouldNotBeNull().create());
  }
}
