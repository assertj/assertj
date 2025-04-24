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
package org.assertj.tests.core.internal.shorts;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeEven.shouldBeEven;
import static org.assertj.tests.core.testkit.TestData.someInfo;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Shorts;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for <code>{@link Shorts#assertIsEven(AssertionInfo, Number)}</code>.
 *
 * @author Cal027
 */
class Shorts_assertIsEven_Test extends ShortsBaseTest {

  @ParameterizedTest
  @ValueSource(shorts = { 0, 2, -4, 6 })
  void should_pass_since_actual_is_even(short actual) {
    shorts.assertIsEven(someInfo(), actual);
  }

  @ParameterizedTest
  @ValueSource(shorts = { 1, 3, -5, 7 })
  void should_fail_since_actual_is_not_even(short actual) {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> shorts.assertIsEven(someInfo(), actual));
    // THEN
    then(assertionError).hasMessage(shouldBeEven(actual).create());
  }

  @ParameterizedTest
  @ValueSource(shorts = { 0, 2, -4, 6 })
  void should_pass_since_actual_is_even_whatever_custom_comparison_strategy_is(short actual) {
    shortsWithAbsValueComparisonStrategy.assertIsEven(someInfo(), actual);
  }

  @ParameterizedTest
  @ValueSource(shorts = { 1, 3, -5, 7 })
  void should_fail_since_actual_is_not_even_whatever_custom_comparison_strategy_is(short actual) {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> shortsWithAbsValueComparisonStrategy.assertIsEven(someInfo(),
                                                                                                                 actual));
    // THEN
    then(assertionError).hasMessage(shouldBeEven(actual).create());
  }

}
