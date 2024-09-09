package org.assertj.core.api;

/**
 * Functional interface to represent any {@link java.util.concurrent.Callable} that can throw any {@link Throwable}
 *
 * @author Mikhail Polivakha
 * @param <T> - the return type of the call
 * @param <E> - the type of the exception expected to be thrown
 */
public interface ThrowingCallable<T, E extends Throwable> {

  /**
   * Execution that can result in error
   */
  T call() throws E;
}
