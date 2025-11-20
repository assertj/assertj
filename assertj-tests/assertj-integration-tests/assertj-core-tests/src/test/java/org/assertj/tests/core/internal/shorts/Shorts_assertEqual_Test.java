/*
 * Copyright 2012-2025 the original author or authors.
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

import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.testkit.TestData.someInfo;
import static org.assertj.tests.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Shorts;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Shorts#assertEqual(AssertionInfo, Object, Object)}</code>.
 *
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class Shorts_assertEqual_Test extends ShortsBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    assertThatAssertionErrorIsThrownBy(() -> shorts.assertEqual(someInfo(), null, (short) 8)).withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_shorts_are_equal() {
    shorts.assertEqual(someInfo(), (short) 8, (short) 8);
  }

  @Test
  void should_fail_if_shorts_are_not_equal() {
    // GIVEN
    AssertionInfo info = someInfo();
    // WHEN
    expectAssertionError(() -> shorts.assertEqual(info, (short) 6, (short) 8));
    // THEN
    verify(failures).failure(info, shouldBeEqual((short) 6, (short) 8, info.representation()));
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatAssertionErrorIsThrownBy(() -> shortsWithAbsValueComparisonStrategy.assertEqual(someInfo(), null,
                                                                                              (short) 8)).withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_shorts_are_equal_according_to_custom_comparison_strategy() {
    shortsWithAbsValueComparisonStrategy.assertEqual(someInfo(), (short) 8, (short) -8);
  }

  @Test
  void should_fail_if_shorts_are_not_equal_according_to_custom_comparison_strategy() {
    // GIVEN
    AssertionInfo info = someInfo();
    // WHEN
    expectAssertionError(() -> shortsWithAbsValueComparisonStrategy.assertEqual(info, (short) 6, (short) 8));
    // THEN
    verify(failures).failure(info, shouldBeEqual((short) 6, (short) 8, absValueComparisonStrategy,
                                                 new StandardRepresentation()));
  }
}
