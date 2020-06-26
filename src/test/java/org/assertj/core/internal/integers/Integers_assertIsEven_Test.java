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
import static org.assertj.core.error.ShouldBeEven.shouldBeEven;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Integers;
import org.assertj.core.internal.IntegersBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for <code>{@link Integers#assertIsEven(AssertionInfo, Number)}</code>.
 *
 * @author Cal027
 */
@DisplayName("Integers assertIsEven")
class Integers_assertIsEven_Test extends IntegersBaseTest {

  @ParameterizedTest
  @ValueSource(ints = { 0, 2, -4, 6 })
  void should_pass_since_actual_is_even(int actual) {
    // WHEN/THEN
    integers.assertIsEven(someInfo(), actual);
  }

  @ParameterizedTest
  @ValueSource(ints = { 1, 3, -5, 7 })
  void should_fail_since_actual_is_not_even(int actual) {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> integers.assertIsEven(someInfo(), actual));
    // THEN
    then(assertionError).hasMessage(shouldBeEven(actual).create());
  }

  @ParameterizedTest
  @ValueSource(ints = { 0, 2, -4, 6 })
  void should_pass_since_actual_is_even_whatever_custom_comparison_strategy_is(int actual) {
    // WHEN/THEN
    integersWithAbsValueComparisonStrategy.assertIsEven(someInfo(), actual);
  }

  @ParameterizedTest
  @ValueSource(ints = { 1, 3, -5, 7 })
  void should_fail_since_actual_is_not_even_whatever_custom_comparison_strategy_is(int actual) {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> integersWithAbsValueComparisonStrategy.assertIsEven(someInfo(),
                                                                                                                   actual));
    // THEN
    then(assertionError).hasMessage(shouldBeEven(actual).create());
  }

}
