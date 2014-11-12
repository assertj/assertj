package org.assertj.core.api;

import org.junit.Test;

public class ThrowingRunnableAssertTest {
  @Test
  public void should_pass_if_correct_exception_is_thrown() throws Exception {
    Assertions.assertThatExceptionThrownBy(new ThrowingRunnable() {
      @Override
      public void run() throws Exception {
        throw new IllegalArgumentException("something was wrong");
      }
    }).isInstanceOf(IllegalArgumentException.class).hasMessage("something was wrong");
  }

  @Test
  public void should_fail_if_no_exception_is_thrown() throws Exception {
    try {
      Assertions.assertThatExceptionThrownBy(new ThrowingRunnable() {
        @Override
        public void run() throws Exception {
          // no exception
        }
      }).isInstanceOf(IllegalArgumentException.class);

      Fail.failBecauseExceptionWasNotThrown(AssertionError.class);

    } catch (AssertionError e) {
      Assertions.assertThat(e).hasMessage("Expected IllegalArgumentException to be thrown");
    }
  }

  @Test
  public void should_fail_if_wrong_exception_is_thrown() throws Exception {
    try {
      Assertions.assertThatExceptionThrownBy(new ThrowingRunnable() {
        @Override
        public void run() throws Exception {
          throw new IllegalArgumentException();
        }
      }).isInstanceOf(IllegalStateException.class);

      Fail.failBecauseExceptionWasNotThrown(AssertionError.class);

    } catch (AssertionError e) {
      Assertions.assertThat(e).hasMessageContaining(
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
      Assertions.assertThatExceptionThrownBy(new ThrowingRunnable() {
        @Override
        public void run() throws Exception {
          throw new IllegalArgumentException("something was wrong");
        }
      }).isInstanceOf(IllegalArgumentException.class).hasMessage("something was not wrong?!");

      Fail.failBecauseExceptionWasNotThrown(AssertionError.class);

    } catch (AssertionError e) {
      Assertions.assertThat(e).hasMessageContaining(
          "Expecting message:\n" +
          " <\"something was not wrong?!\">\n" +
          "but was:\n" +
          " <\"something was wrong\">");
    }
  }
}
