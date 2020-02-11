package org.assertj.core.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldHaveRootCause.shouldHaveRootCause;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

@DisplayName("ThrowableAssertAlternative havingRootCause")
class ThrowableAssertAlternative_havingRootCause_Test {

  @Test
  void should_return_root_cause_if_throwable_has_cause() {
    Throwable rootCause = new Throwable("root message");
    Throwable throwable = new Throwable("top level message", new Throwable("cause message", rootCause));

    assertThat(new ThrowableAssertAlternative<>(throwable)
                 .havingRootCause().actual)
      .isSameAs(rootCause);
  }

  @Test
  void should_fail_if_throwable_has_no_root_cause() {
    //GIVEN
    Throwable throwable = new Throwable("top level message");
    ThrowableAssertAlternative<Throwable> taa = new ThrowableAssertAlternative<>(throwable);
    //WHEN
    AssertionError error = expectAssertionError(taa::havingRootCause);
    //THEN
    assertThat(error).hasMessage(shouldHaveRootCause(throwable).create());
  }

  @Test
  void should_fail_if_throwable_is_null() {
    //GIVEN
    Throwable throwable = null;
    ThrowableAssertAlternative<Throwable> taa = new ThrowableAssertAlternative<>(throwable);
    //WHEN
    AssertionError error = expectAssertionError(taa::havingRootCause);
    //THEN
    assertThat(error).hasMessage(shouldNotBeNull().create());
  }

}
