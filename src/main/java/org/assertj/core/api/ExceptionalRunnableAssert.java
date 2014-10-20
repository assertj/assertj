package org.assertj.core.api;

public class ExceptionalRunnableAssert {
  private final ExceptionalRunnable runnable;

  public ExceptionalRunnableAssert(ExceptionalRunnable runnable) {
    this.runnable = runnable;
  }

  public <T extends Throwable> void toThrow(Class<T> exceptionClass) throws Exception {
    try {
      // run the ExceptionalRunnable
      runnable.run();

      // fail if the expected exception was *not* thrown
      Fail.failBecauseExceptionWasNotThrown(exceptionClass);

    } catch (Throwable e) {
      // ignore expected exceptions and rethrow others
      if (!exceptionClass.isInstance(e))
        throw e;
    }
  }
}
