/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.internal.shorts;

import static org.assertj.tests.core.testkit.TestData.someInfo;
import static org.assertj.tests.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;

import org.junit.jupiter.api.Test;

class Shorts_assertIsNotZero_Test extends ShortsBaseTest {

  @Test
  void should_succeed_since_actual_is_not_zero() {
    shorts.assertIsNotZero(someInfo(), (short) 2);
  }

  @Test
  void should_fail_since_actual_is_zero() {
    assertThatAssertionErrorIsThrownBy(() -> shorts.assertIsNotZero(someInfo(), (short) 0))
                                                                                           .withMessage("%nExpecting actual:%n  0%nnot to be equal to:%n  0%n".formatted());
  }

  @Test
  void should_succeed_since_actual_is_zero_whatever_custom_comparison_strategy_is() {
    shortsWithAbsValueComparisonStrategy.assertIsNotZero(someInfo(), (short) 1);
  }

  @Test
  void should_fail_since_actual_is_not_zero_whatever_custom_comparison_strategy_is() {
    assertThatAssertionErrorIsThrownBy(() -> shortsWithAbsValueComparisonStrategy.assertIsNotZero(someInfo(), (short) 0))
                                                                                                                         .withMessage("%nExpecting actual:%n  0%nnot to be equal to:%n  0%n".formatted());
  }

}
