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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.internal.doubles;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.test.TestData.someInfo;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Doubles;
import org.assertj.core.internal.DoublesBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Doubles#assertIsNegative(AssertionInfo, Double)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class Doubles_assertIsNotZero_Test extends DoublesBaseTest {

  @Test
  void should_succeed_since_actual_is_not_zero() {
    doubles.assertIsNotZero(someInfo(), 2.0);
  }

  @Test
  void should_fail_since_actual_is_zero() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> doubles.assertIsNotZero(someInfo(), 0.0))
                                                   .withMessage(format("%nExpecting actual:%n  0.0%nnot to be equal to:%n  0.0%n"));
  }

  @Test
  void should_succeed_since_actual_is_not_zero_whatever_custom_comparison_strategy_is() {
    doublesWithAbsValueComparisonStrategy.assertIsNotZero(someInfo(), 2.0d);
  }

  @Test
  void should_fail_since_actual_is_zero_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> doublesWithAbsValueComparisonStrategy.assertIsNotZero(someInfo(),
                                                                                                                           0.0d))
                                                   .withMessage(format("%nExpecting actual:%n  0.0%nnot to be equal to:%n  0.0%n"));
  }

}
