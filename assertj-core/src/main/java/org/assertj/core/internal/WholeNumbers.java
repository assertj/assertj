/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.internal;

import static org.assertj.core.error.ShouldBeEven.shouldBeEven;
import static org.assertj.core.error.ShouldBeOdd.shouldBeOdd;

import org.assertj.core.api.AssertionInfo;

/**
 * Assertions and predicates for whole numbers.
 *
 * @param <NUMBER> the number type
 */
public interface WholeNumbers<NUMBER extends Number> {

  /**
   * Checks whether the given number is even.
   *
   * @param number the number to check
   * @return whether the number is even
   */
  boolean isEven(NUMBER number);

  /**
   * Checks whether the given number is odd.
   *
   * @param number the number to check
   * @return whether the number is odd
   */
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
