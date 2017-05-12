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
package org.assertj.core.internal.doubles;

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.NaN;
import static java.lang.Double.POSITIVE_INFINITY;
import static java.lang.Math.abs;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.assertj.core.error.ShouldNotBeEqualWithinOffset.shouldNotBeEqual;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.DoublesBaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;

@RunWith(DataProviderRunner.class)
public class Doubles_assertIsNotCloseTo_Test extends DoublesBaseTest {

  private static final Double ZERO = 0d;
  private static final Double ONE = 1d;
  private static final Double TWO = 2d;
  private static final Double THREE = 3d;
  private static final Double TEN = 10d;

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    doubles.assertIsNotCloseTo(someInfo(), null, ONE, byLessThan(ONE));
  }

  @Test(expected = NullPointerException.class)
  public void should_fail_if_expected_value_is_null() {
    doubles.assertIsNotCloseTo(someInfo(), ONE, null, byLessThan(ONE));
  }

  @Test(expected = NullPointerException.class)
  public void should_fail_if_offset_is_null() {
    doubles.assertIsNotCloseTo(someInfo(), ONE, ZERO, null);
  }

  @Test
  public void should_pass_if_difference_is_less_than_given_offset() {
    doubles.assertIsNotCloseTo(someInfo(), ONE, THREE, byLessThan(ONE));
    doubles.assertIsNotCloseTo(someInfo(), ONE, TEN, byLessThan(TWO));
  }

  @Test
  @DataProvider({
      "1.0, 1.0, 0.0",
      "1.0, 0.0, 1.0",
      "1.0, 2.0, 1.0"
  })
  public void should_fail_if_difference_is_equal_to_given_offset(Double actual, Double other, Double offset) {
    AssertionInfo info = someInfo();
    try {
      doubles.assertIsNotCloseTo(someInfo(), actual, other, byLessThan(offset));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotBeEqual(actual, other, byLessThan(offset), abs(actual - other)));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
  
  @Test
  public void should_fail_if_actual_is_too_close_to_expected_value() {
    AssertionInfo info = someInfo();
    try {
      doubles.assertIsNotCloseTo(info, ONE, TWO, byLessThan(TEN));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotBeEqual(ONE, TWO, byLessThan(TEN), TWO - ONE));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_and_expected_are_NaN() {
    thrown.expectAssertionError();
    doubles.assertIsNotCloseTo(someInfo(), NaN, NaN, byLessThan(ONE));
  }

  @Test
  public void should_fail_if_actual_and_expected_are_POSITIVE_INFINITY() {
    thrown.expectAssertionError();
    doubles.assertIsNotCloseTo(someInfo(), POSITIVE_INFINITY, POSITIVE_INFINITY, byLessThan(ONE));
  }

  @Test
  public void should_fail_if_actual_and_expected_are_NEGATIVE_INFINITY() {
    thrown.expectAssertionError();
    doubles.assertIsNotCloseTo(someInfo(), NEGATIVE_INFINITY, NEGATIVE_INFINITY, byLessThan(ONE));
  }

  @Test
  public void should_pass_if_actual_is_POSITIVE_INFINITY_and_expected_is_not() {
    doubles.assertIsNotCloseTo(someInfo(), POSITIVE_INFINITY, ONE, byLessThan(ONE));
  }

  @Test
  public void should_pass_if_actual_is_POSITIVE_INFINITY_and_expected_is_NEGATIVE_INFINITY() {
    doubles.assertIsNotCloseTo(someInfo(), POSITIVE_INFINITY, NEGATIVE_INFINITY, byLessThan(ONE));
  }

  @Test
  public void should_pass_if_actual_is_NEGATIVE_INFINITY_and_expected_is_not() {
    doubles.assertIsNotCloseTo(someInfo(), NEGATIVE_INFINITY, ONE, byLessThan(ONE));
  }

  @Test
  public void should_pass_if_actual_is_NEGATIVE_INFINITY_and_expected_is_POSITIVE_INFINITY() {
    doubles.assertIsNotCloseTo(someInfo(), NEGATIVE_INFINITY, POSITIVE_INFINITY, byLessThan(ONE));
  }
}
