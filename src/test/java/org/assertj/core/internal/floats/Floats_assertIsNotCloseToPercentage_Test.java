/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.internal.floats;

import static java.lang.Float.NEGATIVE_INFINITY;
import static java.lang.Float.NaN;
import static java.lang.Float.POSITIVE_INFINITY;
import static java.lang.Math.abs;
import static org.assertj.core.api.Assertions.withinPercentage;
import static org.assertj.core.data.Percentage.withPercentage;
import static org.assertj.core.error.ShouldNotBeEqualWithinPercentage.shouldNotBeEqualWithinPercentage;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.FloatsBaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;

@RunWith(DataProviderRunner.class)
public class Floats_assertIsNotCloseToPercentage_Test extends FloatsBaseTest {

  private static final Float ZERO = 0f;
  private static final Float ONE = 1f;
  private static final Float TEN = 10f;
  private static final Float ONE_HUNDRED = 100f;

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    floats.assertIsNotCloseToPercentage(someInfo(), null, ONE, withPercentage(ONE));
  }

  @Test(expected = NullPointerException.class)
  public void should_fail_if_expected_value_is_null() {
    floats.assertIsNotCloseToPercentage(someInfo(), ONE, null, withPercentage(ONE));
  }

  @Test(expected = NullPointerException.class)
  public void should_fail_if_percentage_is_null() {
    floats.assertIsNotCloseToPercentage(someInfo(), ONE, ZERO, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void should_fail_if_percentage_is_negative() {
    floats.assertIsNotCloseToPercentage(someInfo(), ONE, ZERO, withPercentage(-1.0f));
  }

  @Test
  @DataProvider({
      "1, 2, 1",
      "1, 11, 90",
      "-1, -2, 1",
      "-1, -11, 90",
      "0, -1, 99"
  })
  public void should_pass_if_difference_is_greater_than_given_percentage(Float actual, Float other, Float percentage) {
    floats.assertIsNotCloseToPercentage(someInfo(), actual, other, withPercentage(percentage));
  }

  @Test
  @DataProvider({
      "1, 1, 0",
      "2, 1, 100",
      "1, 2, 50",
      "-1, -1, 0",
      "-2, -1, 100",
      "-1, -2, 50"
  })
  public void should_fail_if_difference_is_equal_to_given_percentage(Float actual, Float other, Float percentage) {
    AssertionInfo info = someInfo();
    try {
      floats.assertIsNotCloseToPercentage(someInfo(), actual, other, withPercentage(percentage));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotBeEqualWithinPercentage(actual, other, withPercentage(percentage),
                                                                      abs(actual - other)));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_too_close_to_expected_value() {
    AssertionInfo info = someInfo();
    try {
      floats.assertIsNotCloseToPercentage(someInfo(), ONE, TEN, withPercentage(ONE_HUNDRED));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotBeEqualWithinPercentage(ONE, TEN, withinPercentage(100),
                                                                      TEN - ONE));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_and_expected_are_NaN() {
    thrown.expectAssertionError();
    floats.assertIsNotCloseToPercentage(someInfo(), NaN, NaN, withPercentage(ONE));
  }

  @Test
  public void should_fail_if_actual_and_expected_are_POSITIVE_INFINITY() {
    thrown.expectAssertionError();
    floats.assertIsNotCloseToPercentage(someInfo(), POSITIVE_INFINITY, POSITIVE_INFINITY, withPercentage(ONE));
  }

  @Test
  public void should_fail_if_actual_and_expected_are_NEGATIVE_INFINITY() {
    thrown.expectAssertionError();
    floats.assertIsNotCloseToPercentage(someInfo(), NEGATIVE_INFINITY, NEGATIVE_INFINITY, withPercentage(ONE));
  }

  @Test
  public void should_pass_if_actual_is_POSITIVE_INFINITY_and_expected_is_not() {
    floats.assertIsNotCloseToPercentage(someInfo(), POSITIVE_INFINITY, ONE, withPercentage(ONE));
  }

  @Test
  public void should_pass_if_actual_is_POSITIVE_INFINITY_and_expected_is_NEGATIVE_INFINITY() {
    floats.assertIsNotCloseToPercentage(someInfo(), POSITIVE_INFINITY, NEGATIVE_INFINITY, withPercentage(ONE));
  }

  @Test
  public void should_pass_if_actual_is_NEGATIVE_INFINITY_and_expected_is_not() {
    floats.assertIsNotCloseToPercentage(someInfo(), NEGATIVE_INFINITY, ONE, withPercentage(ONE));
  }

  @Test
  public void should_pass_if_actual_is_NEGATIVE_INFINITY_and_expected_is_POSITIVE_INFINITY() {
    floats.assertIsNotCloseToPercentage(someInfo(), NEGATIVE_INFINITY, POSITIVE_INFINITY, withPercentage(ONE));
  }
}
