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

  @Test(expected = AssertionError.class)
  public void testNoExceptionThrown() throws Exception {
    Assertions.assertThatExceptionThrownBy(new ThrowingRunnable() {
      @Override
      public void run() throws Exception {
        // no exception
      }
    }).isInstanceOf(IllegalArgumentException.class);
  }

  @Test(expected = AssertionError.class)
  public void testWrongExceptionThrown() throws Exception {
    Assertions.assertThatExceptionThrownBy(new ThrowingRunnable() {
      @Override
      public void run() throws Exception {
        throw new IllegalArgumentException();
      }
    }).isInstanceOf(IllegalStateException.class);
  }

  @Test(expected = AssertionError.class)
  public void testWrongMessage() throws Exception {
    Assertions.assertThatExceptionThrownBy(new ThrowingRunnable() {
      @Override
      public void run() throws Exception {
        throw new IllegalArgumentException("something was wrong");
      }
    }).isInstanceOf(IllegalArgumentException.class).hasMessage("something was not wrong?!");
  }
}
