/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2020 the original author or authors.
 */
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
