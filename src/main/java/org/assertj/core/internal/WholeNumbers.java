package org.assertj.core.internal;

import org.assertj.core.api.AssertionInfo;
import static org.assertj.core.error.ShouldBeEven.shouldBeEven;
import static org.assertj.core.error.ShouldBeOdd.shouldBeOdd;

public interface WholeNumbers<NUMBER extends Number> {

  boolean isEven(NUMBER number);

  default boolean isOdd(NUMBER number) {
    return !isEven(number);
  }

  /**
   * Asserts that the actual value is odd.
   *
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is {@code null}.
   */
  default void assertIsOdd(AssertionInfo info, NUMBER actual) {
    if (!isOdd(actual)) throw Failures.instance().failure(info, shouldBeOdd(actual));
  }

  /**
   * Asserts that the actual value is even.
   *
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is {@code null}.
   */
  default void assertIsEven(AssertionInfo info, NUMBER actual) {
    if (!isEven(actual)) throw Failures.instance().failure(info, shouldBeEven(actual));
  }
}
