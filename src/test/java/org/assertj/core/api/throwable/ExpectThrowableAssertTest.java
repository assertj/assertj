package org.assertj.core.api.throwable;

import static org.assertj.core.api.Fail.shouldHaveThrown;
import static org.assertj.core.api.StrictAssertions.assertThat;
import static org.assertj.core.api.StrictAssertions.assertThatException;

import java.util.NoSuchElementException;

import org.junit.Test;

public class ExpectThrowableAssertTest {

  @Test
  public void should_build_ExpectThrowableAssert_with_exception_thrown_by_lambda() {
    NoSuchElementException ex = new NoSuchElementException("no such element!");
    // @format:off
    assertThatException(NoSuchElementException.class)
      .isThrownBy(() -> {throw ex;})
        .isSameAs(ex)
        .withNoCause()
      .isThrownBy(() -> {throw new NoSuchElementException("this too").initCause(new IllegalArgumentException("The cause"));})
        .withMessage("this too")
        .withCauseInstanceOf(IllegalArgumentException.class)
    ;
    // @format:on
  }
  
  @Test
  public void should_fail_if_nothing_is_thrown_by_lambda() {
    try {
      assertThatException(NoSuchElementException.class).isThrownBy(() -> {});
    } catch (AssertionError e) {
      assertThat(e).hasMessage("Expecting code to raise a throwable.");
      return;
    }
    shouldHaveThrown(AssertionError.class);
  }
}
