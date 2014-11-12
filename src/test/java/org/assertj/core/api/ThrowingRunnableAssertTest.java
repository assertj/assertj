package org.assertj.core.api;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionThrownBy;
import static org.assertj.core.api.Fail.failBecauseExceptionWasNotThrown;

public class ThrowingRunnableAssertTest {
  @Test
  public void should_pass_if_correct_exception_is_thrown() throws Exception {
    assertThatExceptionThrownBy(new ThrowingRunnable() {
      @Override
      public void run() throws Exception {
        throw new IllegalArgumentException("something was wrong");
      }
    }).isInstanceOf(IllegalArgumentException.class).hasMessage("something was wrong");
  }

  @Test
  public void should_fail_if_no_exception_is_thrown() throws Exception {
    try {
      assertThatExceptionThrownBy(new ThrowingRunnable() {
        @Override
        public void run() throws Exception {
          // no exception
        }
      }).isInstanceOf(IllegalArgumentException.class);

      failBecauseExceptionWasNotThrown(AssertionError.class);

    } catch (AssertionError e) {
      assertThat(e).hasMessage("Expected IllegalArgumentException to be thrown");
    }
  }

  @Test
  public void should_fail_if_wrong_exception_is_thrown() throws Exception {
    try {
      assertThatExceptionThrownBy(new ThrowingRunnable() {
        @Override
        public void run() throws Exception {
          throw new IllegalArgumentException();
        }
      }).isInstanceOf(IllegalStateException.class);

      failBecauseExceptionWasNotThrown(AssertionError.class);

    } catch (AssertionError e) {
      assertThat(e).hasMessageContaining(
          "Expecting:\n" +
          " <java.lang.IllegalArgumentException>\n" +
          "to be an instance of:\n" +
          " <java.lang.IllegalStateException>\n" +
          "but was instance of:\n" +
          " <java.lang.IllegalArgumentException>");
    }
  }

  @Test
  public void should_fail_if_thrown_exception_has_wrong_message() throws Exception {
    try {
      assertThatExceptionThrownBy(new ThrowingRunnable() {
        @Override
        public void run() throws Exception {
          throw new IllegalArgumentException("something was wrong");
        }
      }).isInstanceOf(IllegalArgumentException.class).hasMessage("something was not wrong?!");

      failBecauseExceptionWasNotThrown(AssertionError.class);

    } catch (AssertionError e) {
      assertThat(e).hasMessageContaining(
          "Expecting message:\n" +
          " <\"something was not wrong?!\">\n" +
          "but was:\n" +
          " <\"something was wrong\">");
    }
  }
}
