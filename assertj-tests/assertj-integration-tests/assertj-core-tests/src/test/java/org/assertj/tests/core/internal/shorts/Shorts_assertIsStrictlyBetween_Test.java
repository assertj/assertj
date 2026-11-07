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

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeBetween.shouldBeBetween;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.testkit.TestData.someInfo;
import static org.assertj.tests.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Shorts;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Shorts#assertIsStrictlyBetween(AssertionInfo, Short, Short, Short)}</code>.
 *
 * @author William Delanoue
 */
class Shorts_assertIsStrictlyBetween_Test {

  private static final Short ZERO = 0;
  private static final Short ONE = 1;
  private static final Short TWO = 2;
  private static final Short TEN = 10;

  @Test
  void should_fail_if_actual_is_null() {
    assertThatAssertionErrorIsThrownBy(() -> Shorts.instance().assertIsStrictlyBetween(someInfo(), null, ZERO,
                                                                                       ONE)).withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_start_is_null() {
    assertThatNullPointerException().isThrownBy(() -> Shorts.instance().assertIsStrictlyBetween(someInfo(), ONE, null, ONE));
  }

  @Test
  void should_fail_if_end_is_null() {
    assertThatNullPointerException().isThrownBy(() -> Shorts.instance().assertIsStrictlyBetween(someInfo(), ONE, ZERO, null));
  }

  @Test
  void should_pass_if_actual_is_in_range() {
    Shorts.instance().assertIsStrictlyBetween(someInfo(), ONE, ZERO, TEN);
  }

  @Test
  void should_fail_if_actual_is_equal_to_range_start() {
    // GIVEN
    AssertionInfo info = someInfo();
    // WHEN
    var error = expectAssertionError(() -> Shorts.instance().assertIsStrictlyBetween(info, ONE, ONE, TEN));
    // THEN
    then(error).hasMessage(shouldBeBetween(ONE, ONE, TEN, false, false).create());
  }

  @Test
  void should_fail_if_actual_is_equal_to_range_end() {
    // GIVEN
    AssertionInfo info = someInfo();
    // WHEN
    var error = expectAssertionError(() -> Shorts.instance().assertIsStrictlyBetween(info, ONE, ZERO, ONE));
    // THEN
    then(error).hasMessage(shouldBeBetween(ONE, ZERO, ONE, false, false).create());
  }

  @Test
  void should_fail_if_actual_is_not_in_range_start() {
    // GIVEN
    AssertionInfo info = someInfo();
    // WHEN
    var error = expectAssertionError(() -> Shorts.instance().assertIsStrictlyBetween(info, ONE, TWO, TEN));
    // THEN
    then(error).hasMessage(shouldBeBetween(ONE, TWO, TEN, false, false).create());
  }

  @Test
  void should_fail_if_actual_is_not_in_range_end() {
    assertThatIllegalArgumentException().isThrownBy(() -> Shorts.instance().assertIsStrictlyBetween(someInfo(), ONE, ZERO, ZERO))
                                        .withMessage("The end value <0> must not be less than or equal to the start value <0>!");
  }
}
