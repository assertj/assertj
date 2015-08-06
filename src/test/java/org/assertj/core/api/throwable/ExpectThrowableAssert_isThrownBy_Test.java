package org.assertj.core.api.throwable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Fail.shouldHaveThrown;

import java.util.NoSuchElementException;

import org.junit.Test;

public class ExpectThrowableAssert_isThrownBy_Test {

  @Test
  public void should_build_ExpectThrowableAssert_with_exception_thrown_by_lambda() {
    NoSuchElementException ex = new NoSuchElementException("no such element!");
    // @format:off
    assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> {throw ex;})
                                                     .isSameAs(ex)
                                                     .withNoCause();
    // @format:on
  }

  @Test
  public void should_allow_to_check_exception_thrown_by_lambda() {
    // @format:off
    Throwable exceptionWithCause = new NoSuchElementException("this too").initCause(new IllegalArgumentException("The cause"));
    assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> { throw exceptionWithCause;})
                                                     .withMessage("this too")
                                                     .withMessageStartingWith("this")
                                                     .withMessageEndingWith("too")
                                                     .withMessageMatching(".*is.*")
                                                     .withCauseExactlyInstanceOf(IllegalArgumentException.class)
                                                     .withCauseInstanceOf(IllegalArgumentException.class);
    // @format:on
  }
  
  @Test
  public void should_fail_if_nothing_is_thrown_by_lambda() {
    try {
      assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> {});
    } catch (AssertionError e) {
      assertThat(e).hasMessage("Expecting code to raise a throwable.");
      return;
    }
    shouldHaveThrown(AssertionError.class);
  }
}
