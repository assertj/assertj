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
package org.assertj.core.internal.integers;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeOdd.shouldBeOdd;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Integers;
import org.assertj.core.internal.IntegersBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for <code>{@link Integers#assertIsOdd(AssertionInfo, Number)}</code>.
 *
 * @author Cal027
 */
@DisplayName("Integers assertIsOdd")
class Integers_assertIsOdd_Test extends IntegersBaseTest {

  @ParameterizedTest
  @ValueSource(ints = { 1, 3, -5, 7 })
  void should_pass_since_actual_is_odd(int actual) {
    // WHEN/THEN
    integers.assertIsOdd(someInfo(), actual);
  }

  @ParameterizedTest
  @ValueSource(ints = { 0, 2, -4, 6 })
  void should_fail_since_actual_is_not_odd(int actual) {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> integers.assertIsOdd(someInfo(), actual));
    // THEN
    then(assertionError).hasMessage(shouldBeOdd(actual).create());

  }

  @ParameterizedTest
  @ValueSource(ints = { 1, 3, -5, 7 })
  void should_pass_since_actual_is_odd_whatever_custom_comparison_strategy_is(int actual) {
    // WHEN/THEN
    integersWithAbsValueComparisonStrategy.assertIsOdd(someInfo(), actual);
  }

  @ParameterizedTest
  @ValueSource(ints = { 0, 2, -4, 6 })
  void should_fail_since_actual_is_not_odd_whatever_custom_comparison_strategy_is(int actual) {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> integersWithAbsValueComparisonStrategy.assertIsOdd(someInfo(),
            actual));
    // THEN
    then(assertionError).hasMessage(shouldBeOdd(actual).create());
  }

}
