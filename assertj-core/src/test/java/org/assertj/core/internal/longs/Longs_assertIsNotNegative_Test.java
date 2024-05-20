/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.internal.longs;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.testkit.TestData.someInfo;

import org.assertj.core.internal.LongsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Longs#assertIsNotNegative(AssertionInfo, Longs))}</code>.
 *
 * @author Nicolas François
 */
class Longs_assertIsNotNegative_Test extends LongsBaseTest {

  @Test
  void should_succeed_since_actual_is_not_negative() {
    longs.assertIsNotNegative(someInfo(), 6L);
  }

  @Test
  void should_succeed_since_actual_is_zero() {
    longs.assertIsNotNegative(someInfo(), 0L);
  }

  @Test
  void should_fail_since_actual_is_negative() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> longs.assertIsNotNegative(someInfo(), -6L))
                                                   .withMessage(format("%nExpecting actual:%n  -6L%nto be greater than or equal to:%n  0L%n"));
  }

  @Test
  void should_succeed_since_actual_negative_is_not_negative_according_to_custom_comparison_strategy() {
    longsWithAbsValueComparisonStrategy.assertIsNotNegative(someInfo(), -1L);
  }

  @Test
  void should_succeed_since_actual_positive_is_not_negative_according_to_custom_comparison_strategy() {
    longsWithAbsValueComparisonStrategy.assertIsNotNegative(someInfo(), 1L);
  }

}
