package org.assertj.core.api;

import org.assertj.core.util.AssertionsUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ThrowableAssertAlternative havingCause")
class ThrowableAssertAlternative_havingCause_Test {

  @Test
  void should_return_cause_if_throwable_has_cause() {
    Throwable throwable = new Throwable("top level message", new Throwable("cause message"));
    new ThrowableAssertAlternative<>(throwable)
      .havingCause()
      .withMessage("cause message");
  }

  @Test
  void should_fail_if_throwable_has_no_cause() {
    //WHEN
    ThrowableAssert.ThrowingCallable code = () -> {
      Throwable throwable = new Throwable("top level message");
      new ThrowableAssertAlternative<>(throwable)
        .havingCause();
    };
    //THEN
    AssertionsUtil.assertThatAssertionErrorIsThrownBy(code).withMessage("expecting java.lang.Throwable: top level message to have a cause but it did not");
  }

}
