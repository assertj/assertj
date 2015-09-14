package org.assertj.core.api;

import static java.util.Objects.requireNonNull;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;

/**
 * Assertion methods for exception.
 * <p>
 * The class itself does not do much, it delegates it work to {@link #isThrownBy(ThrowingCallable)}.
 *
 * @param <T> type of throwable to be thrown.
 */
public class ExpectThrowableAssert<T extends Throwable> {
  private final Class<? extends T> expectedExceptionType;

  /**
   * Default constructor.
   *
   * @param exceptionType class representing the target (expected) exception.
   */
  ExpectThrowableAssert(final Class<? extends T> exceptionType) {
    this.expectedExceptionType = requireNonNull(exceptionType, "exceptionType");
  }

  /**
   * Assert that an exception of type T is actually thrown by the {@code throwingCallable}.
   *
   * @param throwingCallable
   * @return return a {@link ChainedThrowableAssert}.
   */
  public ChainedThrowableAssert<T> isThrownBy(final ThrowingCallable throwingCallable) {
    Throwable throwable = ThrowableAssert.catchThrowable(throwingCallable);
    StrictAssertions.assertThat(throwable).hasBeenThrown().isInstanceOf(expectedExceptionType);
    @SuppressWarnings("unchecked")
    T c = (T) throwable;
    return new ChainedThrowableAssert<>(this, c);
  }

}
