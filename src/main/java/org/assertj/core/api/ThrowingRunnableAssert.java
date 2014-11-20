package org.assertj.core.api;

public class ThrowingRunnableAssert {
  private final ThrowingRunnable runnable;

  public ThrowingRunnableAssert(ThrowingRunnable runnable) {
    this.runnable = runnable;
  }

  public <T extends Throwable> AbstractThrowableAssert<?, ? extends Throwable> isInstanceOf(Class<T> exceptionClass)
      throws Exception {

    try {
      // run the ExceptionalRunnable
      runnable.run();

      // fail if the expected exception was *not* thrown
      Fail.failBecauseExceptionWasNotThrown(exceptionClass);

      // this will *never* happen...
      return null;

    } catch (AssertionError e) {
      // do not handle AssertionErrors in the next catch block!
      throw e;

    } catch (Throwable e) {
      // check if the right exception was thrown
      return new ThrowableAssertProxy(e).isInstanceOf(exceptionClass);
    }
  }

  public <T extends Throwable> ThrowableAssertProxy toThrow(Class<T> exceptionClass)
      throws Exception {
    return (ThrowableAssertProxy) isInstanceOf(exceptionClass);
  }

  public static class ThrowableAssertProxy extends ThrowableAssert {
    protected ThrowableAssertProxy(Throwable actual) {
      super(actual);
    }

    public ThrowableAssert that() {
      return this;
    }
  }
}
