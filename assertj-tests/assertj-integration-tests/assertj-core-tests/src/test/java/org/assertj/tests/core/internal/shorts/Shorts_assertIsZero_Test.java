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

import static org.assertj.tests.core.testkit.ErrorMessagesForTest.shouldBeEqualMessage;
import static org.assertj.tests.core.testkit.TestData.someInfo;
import static org.assertj.tests.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Shorts;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Shorts#assertIsNegative(AssertionInfo, Short)}</code>.
 *
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class Shorts_assertIsZero_Test extends ShortsBaseTest {

  @Test
  void should_succeed_since_actual_is_zero() {
    shorts.assertIsZero(someInfo(), (short) 0);
  }

  @Test
  void should_fail_since_actual_is_not_zero() {
    assertThatAssertionErrorIsThrownBy(() -> shorts.assertIsZero(someInfo(), (short) 2))
                                                                                        .withMessage(shouldBeEqualMessage("2",
                                                                                                                          "0"));
  }

  @Test
  void should_succeed_since_actual_is_not_zero_whatever_custom_comparison_strategy_is() {
    shortsWithAbsValueComparisonStrategy.assertIsZero(someInfo(), (short) 0);
  }

  @Test
  void should_fail_since_actual_is_zero_whatever_custom_comparison_strategy_is() {
    assertThatAssertionErrorIsThrownBy(() -> shortsWithAbsValueComparisonStrategy.assertIsZero(someInfo(), (short) 2))
                                                                                                                      .withMessage(shouldBeEqualMessage("2",
                                                                                                                                                        "0"));
  }

}
