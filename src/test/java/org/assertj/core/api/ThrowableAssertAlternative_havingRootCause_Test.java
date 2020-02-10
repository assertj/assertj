package org.assertj.core.api;

import org.assertj.core.util.AssertionsUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ThrowableAssertAlternative havingRootCause")
class ThrowableAssertAlternative_havingRootCause_Test {

  @Test
  void should_return_root_cause_if_throwable_has_cause() {
    Throwable throwable = new Throwable("top level message", new Throwable("cause message", new Throwable("root message")));
    new ThrowableAssertAlternative<>(throwable)
      .havingRootCause()
      .withMessage("root message");
  }

  @Test
  void should_fail_if_throwable_has_no_root_cause() {
    //WHEN
    ThrowableAssert.ThrowingCallable code = () -> {
      Throwable throwable = new Throwable("top level message");
      new ThrowableAssertAlternative<>(throwable)
        .havingRootCause();
    };
    //THEN
    AssertionsUtil.assertThatAssertionErrorIsThrownBy(code).withMessage("expecting java.lang.Throwable: top level message to have a root cause but it did not");
  }

}
