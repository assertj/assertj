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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.internal.longs;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.testkit.TestData.someInfo;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Longs;
import org.assertj.core.internal.LongsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Longs#assertIsPositive(AssertionInfo, Long)}</code>.
 *
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class Longs_assertIsPositive_Test extends LongsBaseTest {

  @Test
  void should_succeed_since_actual_is_positive() {
    longs.assertIsPositive(someInfo(), 6L);
  }

  @Test
  void should_fail_since_actual_is_not_positive() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> longs.assertIsPositive(someInfo(), -6L))
                                                   .withMessage("%nExpecting actual:%n  -6L%nto be greater than:%n  0L%n".formatted());
  }

  @Test
  void should_succeed_since_actual_is_positive_according_to_custom_comparison_strategy() {
    longsWithAbsValueComparisonStrategy.assertIsPositive(someInfo(), -1L);
  }

  @Test
  void should_fail_since_actual_is_not_positive_according_to_custom_comparison_strategy() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> longsWithAbsValueComparisonStrategy.assertIsPositive(someInfo(),
                                                                                                                          0L))
                                                   .withMessage("%nExpecting actual:%n  0L%nto be greater than:%n  0L%nwhen comparing values using AbsValueComparator".formatted());
  }
}
