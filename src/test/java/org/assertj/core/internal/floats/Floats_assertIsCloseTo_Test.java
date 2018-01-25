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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.internal.floats;

import static java.lang.Float.NEGATIVE_INFINITY;
import static java.lang.Float.NaN;
import static java.lang.Float.POSITIVE_INFINITY;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.data.Offset.offset;
import static org.assertj.core.error.ShouldBeEqualWithinOffset.shouldBeEqual;
import static org.assertj.core.internal.ErrorMessages.offsetIsNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.FloatsBaseTest;
import org.junit.Test;

public class Floats_assertIsCloseTo_Test extends FloatsBaseTest {

  private static final Float ZERO = 0f;
  private static final Float ONE = 1f;
  private static final Float TWO = 2f;
  private static final Float TEN = 10f;

  // success

  @Test
  public void should_pass_if_difference_is_less_than_given_offset() {
    floats.assertIsCloseTo(someInfo(), ONE, ONE, within(ONE));
    floats.assertIsCloseTo(someInfo(), ONE, TWO, within(TEN));
    floats.assertIsCloseTo(someInfo(), ONE, TWO, byLessThan(TEN));
  }

  @Test
  public void should_pass_if_difference_is_equal_to_given_offset() {
    floats.assertIsCloseTo(someInfo(), ONE, ONE, within(ZERO));
    floats.assertIsCloseTo(someInfo(), ONE, ZERO, within(ONE));
    floats.assertIsCloseTo(someInfo(), ONE, TWO, within(ONE));
  }

  @Test
  public void should_pass_if_actual_and_expected_are_POSITIVE_INFINITY() {
    floats.assertIsCloseTo(someInfo(), POSITIVE_INFINITY, POSITIVE_INFINITY, within(ONE));
    floats.assertIsCloseTo(someInfo(), POSITIVE_INFINITY, POSITIVE_INFINITY, byLessThan(ONE));
  }

  @Test
  public void should_pass_if_actual_and_expected_are_NEGATIVE_INFINITY() {
    floats.assertIsCloseTo(someInfo(), NEGATIVE_INFINITY, NEGATIVE_INFINITY, within(ONE));
    floats.assertIsCloseTo(someInfo(), NEGATIVE_INFINITY, NEGATIVE_INFINITY, byLessThan(ONE));
  }

  @Test
  public void should_pass_if_actual_and_expected_are_NaN() {
    floats.assertIsCloseTo(someInfo(), NaN, NaN, within(ONE));
    floats.assertIsCloseTo(someInfo(), NaN, NaN, byLessThan(ONE));
  }

  // error or failure

  @Test
  public void should_throw_error_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    floats.assertIsCloseTo(someInfo(), null, ONE, within(ONE));
  }

  @Test
  public void should_throw_error_if_expected_value_is_null() {
    thrown.expectNullPointerException("The given number should not be null");
    floats.assertIsCloseTo(someInfo(), 6f, null, offset(1f));
  }

  @Test(expected = NullPointerException.class)
  public void should_throw_error_if_offset_is_null() {
    floats.assertIsCloseTo(someInfo(), ONE, ZERO, null);
  }

  @Test
  public void should_fail_if_actual_is_not_close_enough_to_expected_value() {
    AssertionInfo info = someInfo();
    try {
      floats.assertIsCloseTo(info, ONE, TEN, within(ONE));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeEqual(ONE, TEN, within(ONE), TEN - ONE));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_not_close_enough_to_expected_value_with_a_strict_offset() {
    AssertionInfo info = someInfo();
    try {
      floats.assertIsCloseTo(info, ONE, TEN, byLessThan(ONE));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeEqual(ONE, TEN, byLessThan(ONE), TEN - ONE));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_difference_is_equal_to_the_given_strict_offset() {
    AssertionInfo info = someInfo();
    try {
      floats.assertIsCloseTo(info, TWO, ONE, byLessThan(ONE));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeEqual(TWO, ONE, byLessThan(ONE), TWO - ONE));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_NaN_and_expected_is_not() {
    thrown.expectAssertionError();
    floats.assertIsCloseTo(someInfo(), NaN, ONE, within(ONE));
  }

  @Test
  public void should_fail_if_actual_is_POSITIVE_INFINITY_and_expected_is_not() {
    thrown.expectAssertionError();
    floats.assertIsCloseTo(someInfo(), POSITIVE_INFINITY, ONE, within(ONE));
  }

  @Test
  public void should_fail_if_actual_is_NEGATIVE_INFINITY_and_expected_is_not() {
    thrown.expectAssertionError();
    floats.assertIsCloseTo(someInfo(), NEGATIVE_INFINITY, ONE, within(ONE));
  }

  @Test
  public void should_fail_if_actual_is_POSITIVE_INFINITY_and_expected_is_NEGATIVE_INFINITY() {
    thrown.expectAssertionError();
    floats.assertIsCloseTo(someInfo(), POSITIVE_INFINITY, NEGATIVE_INFINITY, within(ONE));
  }

  @Test
  public void should_fail_if_actual_is_NEGATIVE_INFINITY_and_expected_is_POSITIVE_INFINITY() {
    thrown.expectAssertionError();
    floats.assertIsCloseTo(someInfo(), NEGATIVE_INFINITY, POSITIVE_INFINITY, within(ONE));
  }

  // with comparison stratgey

  @Test
  public void should_pass_if_difference_is_less_than_given_offset_whatever_custom_comparison_strategy_is() {
    floatsWithAbsValueComparisonStrategy.assertIsCloseTo(someInfo(), ONE, ONE, within(ONE));
    floatsWithAbsValueComparisonStrategy.assertIsCloseTo(someInfo(), ONE, TWO, within(TEN));
    floatsWithAbsValueComparisonStrategy.assertIsCloseTo(someInfo(), ONE, TWO, byLessThan(TEN));
  }

  @Test
  public void should_pass_if_difference_is_equal_to_given_offset_whatever_custom_comparison_strategy_is() {
    floatsWithAbsValueComparisonStrategy.assertIsCloseTo(someInfo(), ONE, ONE, within(ZERO));
    floatsWithAbsValueComparisonStrategy.assertIsCloseTo(someInfo(), ONE, ZERO, within(ONE));
    floatsWithAbsValueComparisonStrategy.assertIsCloseTo(someInfo(), ONE, TWO, within(ONE));
  }

  @Test
  public void should_throw_error_if_offset_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectNullPointerException(offsetIsNull());
    floatsWithAbsValueComparisonStrategy.assertIsCloseTo(someInfo(), new Float(8f), new Float(8f), null);
  }

  @Test
  public void should_fail_if_actual_is_not_close_enough_to_expected_value_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();
    try {
      floatsWithAbsValueComparisonStrategy.assertIsCloseTo(info, new Float(6f), new Float(8f), offset(1f));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeEqual(6f, 8f, offset(1f), 2f));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_not_strictly_close_enough_to_expected_value_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();
    try {
      floatsWithAbsValueComparisonStrategy.assertIsCloseTo(info, new Float(6f), new Float(8f), byLessThan(1f));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeEqual(6f, 8f, byLessThan(1f), 2f));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_throw_error_if_expected_value_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectNullPointerException("The given number should not be null");
    floatsWithAbsValueComparisonStrategy.assertIsCloseTo(someInfo(), 6f, null, offset(1f));
  }

}
