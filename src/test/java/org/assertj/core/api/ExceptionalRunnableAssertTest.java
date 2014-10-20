package org.assertj.core.api;

import org.junit.Test;

public class ExceptionalRunnableAssertTest {
  @Test
  public void testCorrectExceptionThrown() throws Exception {
    assertThat(new ExceptionalRunnable() {
      @Override
      public void run() throws Exception {
        throw new IllegalArgumentException("something was wrong");
      }
    }).toThrow(IllegalArgumentException.class).hasMessage("something was wrong");
  }

  @Test(expected = AssertionError.class)
  public void testNoExceptionThrown() throws Exception {
    assertThat(new ExceptionalRunnable() {
      @Override
      public void run() throws Exception {
        // no exception
      }
    }).toThrow(IllegalArgumentException.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWrongExceptionThrown() throws Exception {
    assertThat(new ExceptionalRunnable() {
      @Override
      public void run() throws Exception {
        throw new IllegalArgumentException();
      }
    }).toThrow(IllegalStateException.class);
  }

  @Test(expected = AssertionError.class)
  public void testWrongMessage() throws Exception {
    Assertions.expect(new ExceptionalRunnable() {
      @Override
      public void run() throws Exception {
        throw new IllegalArgumentException("something was wrong");
      }
    }).toThrow(IllegalArgumentException.class).hasMessage("something was not wrong?!");
  }
}
