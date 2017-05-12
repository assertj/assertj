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
import static org.assertj.core.api.Assertions.byLessThan;
import static org.assertj.core.error.ShouldNotBeEqualWithinOffset.shouldNotBeEqual;
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
public class Floats_assertIsNotCloseTo_Test extends FloatsBaseTest {

  private static final Float ZERO = 0f;
  private static final Float ONE = 1f;
  private static final Float TWO = 2f;
  private static final Float THREE = 3f;
  private static final Float TEN = 10f;

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    floats.assertIsNotCloseTo(someInfo(), null, ONE, byLessThan(ONE));
  }

  @Test(expected = NullPointerException.class)
  public void should_fail_if__expected_value_is_null() {
    floats.assertIsNotCloseTo(someInfo(), ONE, null, byLessThan(ONE));
  }

  @Test(expected = NullPointerException.class)
  public void should_fail_if_offset_is_null() {
    floats.assertIsNotCloseTo(someInfo(), ONE, ZERO, null);
  }

  @Test
  public void should_pass_if_difference_is_greater_than_given_offset() {
    floats.assertIsNotCloseTo(someInfo(), ONE, THREE, byLessThan(ONE));
    floats.assertIsNotCloseTo(someInfo(), ONE, TEN, byLessThan(TWO));
  }

  @Test
  @DataProvider({
      "1f, 1f, 0f",
      "1f, 0f, 1f",
      "1f, 2f, 1f"
  })
  public void should_fail_if_difference_is_equal_to_given_offset(Float actual, Float other, Float offset) {
    AssertionInfo info = someInfo();
    try {
      floats.assertIsNotCloseTo(someInfo(), actual, other, byLessThan(offset));
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
      floats.assertIsNotCloseTo(info, ONE, TWO, byLessThan(TEN));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotBeEqual(ONE, TWO, byLessThan(TEN), TWO - ONE));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_and_expected_are_NaN() {
    thrown.expectAssertionError();
    floats.assertIsNotCloseTo(someInfo(), NaN, NaN, byLessThan(ONE));
  }

  @Test
  public void should_fail_if_actual_and_expected_are_POSITIVE_INFINITY() {
    thrown.expectAssertionError();
    floats.assertIsNotCloseTo(someInfo(), POSITIVE_INFINITY, POSITIVE_INFINITY, byLessThan(ONE));
  }

  @Test
  public void should_fail_if_actual_and_expected_are_NEGATIVE_INFINITY() {
    thrown.expectAssertionError();
    floats.assertIsNotCloseTo(someInfo(), NEGATIVE_INFINITY, NEGATIVE_INFINITY, byLessThan(ONE));
  }

  @Test
  public void should_pass_if_actual_is_POSITIVE_INFINITY_and_expected_is_not() {
    floats.assertIsNotCloseTo(someInfo(), POSITIVE_INFINITY, ONE, byLessThan(ONE));
  }

  @Test
  public void should_pass_if_actual_is_POSITIVE_INFINITY_and_expected_is_NEGATIVE_INFINITY() {
    floats.assertIsNotCloseTo(someInfo(), POSITIVE_INFINITY, NEGATIVE_INFINITY, byLessThan(ONE));
  }

  @Test
  public void should_pass_if_actual_is_NEGATIVE_INFINITY_and_expected_is_not() {
    floats.assertIsNotCloseTo(someInfo(), NEGATIVE_INFINITY, ONE, byLessThan(ONE));
  }

  @Test
  public void should_pass_if_actual_is_NEGATIVE_INFINITY_and_expected_is_POSITIVE_INFINITY() {
    floats.assertIsNotCloseTo(someInfo(), NEGATIVE_INFINITY, POSITIVE_INFINITY, byLessThan(ONE));
  }
}
