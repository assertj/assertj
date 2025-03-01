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

import static org.assertj.core.error.ShouldBeGreaterOrEqual.shouldBeGreaterOrEqual;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.testkit.TestData.someInfo;
import static org.assertj.tests.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.junit.jupiter.api.Test;

class Shorts_assertGreaterThanOrEqualTo_Test extends ShortsBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    assertThatAssertionErrorIsThrownBy(() -> shorts.assertGreaterThanOrEqualTo(someInfo(), null,
                                                                               (short) 8)).withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_is_greater_than_other() {
    shorts.assertGreaterThanOrEqualTo(someInfo(), (short) 8, (short) 6);
  }

  @Test
  void should_pass_if_actual_is_equal_to_other() {
    shorts.assertGreaterThanOrEqualTo(someInfo(), (short) 6, (short) 6);
  }

  @Test
  void should_fail_if_actual_is_less_than_other() {
    // GIVEN
    AssertionInfo info = someInfo();
    // WHEN
    expectAssertionError(() -> shorts.assertGreaterThanOrEqualTo(info, (short) 6, (short) 8));
    // THEN
    verify(failures).failure(info, shouldBeGreaterOrEqual((short) 6, (short) 8));
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatAssertionErrorIsThrownBy(() -> shortsWithAbsValueComparisonStrategy.assertGreaterThanOrEqualTo(someInfo(), null,
                                                                                                             (short) 8))
                                                                                                                        .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_is_greater_than_other_according_to_custom_comparison_strategy() {
    shortsWithAbsValueComparisonStrategy.assertGreaterThanOrEqualTo(someInfo(), (short) -8, (short) 6);
  }

  @Test
  void should_pass_if_actual_is_equal_to_other_according_to_custom_comparison_strategy() {
    shortsWithAbsValueComparisonStrategy.assertGreaterThanOrEqualTo(someInfo(), (short) 6, (short) -6);
  }

  @Test
  void should_fail_if_actual_is_less_than_other_according_to_custom_comparison_strategy() {
    // GIVEN
    AssertionInfo info = someInfo();
    // WHEN
    expectAssertionError(() -> shortsWithAbsValueComparisonStrategy.assertGreaterThanOrEqualTo(info, (short) 6, (short) -8));
    // THEN
    verify(failures).failure(info, shouldBeGreaterOrEqual((short) 6, (short) -8, absValueComparisonStrategy));
  }
}
