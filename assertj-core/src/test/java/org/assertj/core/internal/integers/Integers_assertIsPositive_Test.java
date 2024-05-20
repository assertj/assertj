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
package org.assertj.core.internal.integers;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.testkit.TestData.someInfo;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Integers;
import org.assertj.core.internal.IntegersBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Integers#assertIsPositive(AssertionInfo, Integer)}</code>.
 *
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class Integers_assertIsPositive_Test extends IntegersBaseTest {

  @Test
  void should_succeed_since_actual_is_positive() {
    integers.assertIsPositive(someInfo(), 6);
  }

  @Test
  void should_fail_since_actual_is_not_positive() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> integers.assertIsPositive(someInfo(), -6))
                                                   .withMessage(format("%nExpecting actual:%n  -6%nto be greater than:%n  0%n"));
  }

  @Test
  void should_succeed_since_actual_is_positive_according_to_custom_comparison_strategy() {
    integersWithAbsValueComparisonStrategy.assertIsPositive(someInfo(), -1);
  }

  @Test
  void should_fail_since_actual_is_not_positive_according_to_custom_comparison_strategy() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> integersWithAbsValueComparisonStrategy.assertIsPositive(someInfo(),
                                                                                                                             0))
                                                   .withMessage(format("%nExpecting actual:%n  0%nto be greater than:%n  0%nwhen comparing values using AbsValueComparator"));
  }

}
