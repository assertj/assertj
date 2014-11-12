package org.assertj.core.api;

import org.junit.Test;

public class ThrowingRunnableAssertTest {
  @Test
  public void testCorrectExceptionThrown() throws Exception {
    Assertions.assertThatExceptionThrownBy(new ThrowingRunnable() {
      @Override
      public void run() throws Exception {
        throw new IllegalArgumentException("something was wrong");
      }
    }).isInstanceOf(IllegalArgumentException.class).hasMessage("something was wrong");
  }

  @Test
  public void testNoExceptionThrown() throws Exception {
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
  public void testWrongExceptionThrown() throws Exception {
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
  public void testWrongMessage() throws Exception {
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
