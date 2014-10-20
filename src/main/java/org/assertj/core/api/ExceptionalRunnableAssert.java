package org.assertj.core.api;

public class ExceptionalRunnableAssert {
  private final ExceptionalRunnable runnable;

  public ExceptionalRunnableAssert(ExceptionalRunnable runnable) {
    this.runnable = runnable;
  }

  public <T extends Throwable> AbstractThrowableAssert<?, ? extends Throwable> toThrow(Class<T> exceptionClass)
      throws Exception {

    try {
      // run the ExceptionalRunnable
      runnable.run();

      // fail if the expected exception was *not* thrown
      Fail.failBecauseExceptionWasNotThrown(exceptionClass);

      // this will *never* happen...
      return null;

    } catch (Throwable e) {
      // check if the right exception was thrown
      return Assertions.assertThat(e).isInstanceOf(exceptionClass);
    }
  }
}
