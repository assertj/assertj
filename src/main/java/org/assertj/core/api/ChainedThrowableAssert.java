package org.assertj.core.api;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;

/**
 * Assertion methods for {@link java.lang.Throwable}.
 * <p>
 * This class is linked with the {@link ExpectThrowableAssert} and allow to check that an exception
 * type is thrown by several lambda.
 */
public class ChainedThrowableAssert<T extends Throwable>
    extends AbstractThrowableAssert<ChainedThrowableAssert<T>, T> {
  private final ExpectThrowableAssert<T> parent;

  ChainedThrowableAssert(final ExpectThrowableAssert<T> parent, final T actual) {
    super(actual, ChainedThrowableAssert.class);
    this.parent = parent;
  }

  /**
   * Assert that an exception of type T is actually thrown by the {@code throwingCallable}.
   *
   * @param throwingCallable
   * @return return a {@link ChainedThrowableAssert}.
   * @see ExpectThrowableAssert#isThrownBy(ThrowingCallable)
   */
  public ChainedThrowableAssert<T> isThrownBy(final ThrowingCallable throwingCallable) {
    return parent.isThrownBy(throwingCallable);
  }
}
