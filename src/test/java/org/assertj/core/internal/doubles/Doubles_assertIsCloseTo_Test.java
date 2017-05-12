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
import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.error.ShouldBeEqualWithinOffset.shouldBeEqual;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.DoublesBaseTest;
import org.junit.Test;

public class Doubles_assertIsCloseTo_Test extends DoublesBaseTest {

  private static final Double ZERO = 0d;
  private static final Double ONE = 1d;
  private static final Double TWO = 2d;
  private static final Double TEN = 10d;

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    doubles.assertIsCloseTo(someInfo(), null, ONE, within(ONE));
  }

  @Test(expected = NullPointerException.class)
  public void should_fail_if_expected_value_is_null() {
    doubles.assertIsCloseTo(someInfo(), ONE, null, within(ONE));
  }

  @Test(expected = NullPointerException.class)
  public void should_fail_if_offset_is_null() {
    doubles.assertIsCloseTo(someInfo(), ONE, ZERO, null);
  }

  @Test
  public void should_pass_if_difference_is_less_than_given_offset() {
    doubles.assertIsCloseTo(someInfo(), ONE, ONE, within(ONE));
    doubles.assertIsCloseTo(someInfo(), ONE, TWO, within(TEN));
  }

  @Test
  public void should_pass_if_difference_is_equal_to_given_offset() {
    doubles.assertIsCloseTo(someInfo(), ONE, ONE, within(ZERO));
    doubles.assertIsCloseTo(someInfo(), ONE, ZERO, within(ONE));
    doubles.assertIsCloseTo(someInfo(), ONE, TWO, within(ONE));
  }

  @Test
  public void should_fail_if_actual_is_not_close_enough_to_expected_value() {
    AssertionInfo info = someInfo();
    try {
      doubles.assertIsCloseTo(info, ONE, TEN, within(ONE));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeEqual(ONE, TEN, within(ONE), TEN - ONE));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_NaN_and_expected_is_not() {
    thrown.expectAssertionError();
    doubles.assertIsCloseTo(someInfo(), NaN, ONE, within(ONE));
  }

  @Test
  public void should_pass_if_actual_and_expected_are_NaN() {
    doubles.assertIsCloseTo(someInfo(), NaN, NaN, within(ONE));
  }

  @Test
  public void should_fail_if_actual_is_POSITIVE_INFINITY_and_expected_is_not() {
    thrown.expectAssertionError();
    doubles.assertIsCloseTo(someInfo(), POSITIVE_INFINITY, ONE, within(ONE));
  }

  @Test
  public void should_pass_if_actual_and_expected_are_POSITIVE_INFINITY() {
    doubles.assertIsCloseTo(someInfo(), POSITIVE_INFINITY, POSITIVE_INFINITY, within(ONE));
  }

  @Test
  public void should_fail_if_actual_is_NEGATIVE_INFINITY_and_expected_is_not() {
    thrown.expectAssertionError();
    doubles.assertIsCloseTo(someInfo(), NEGATIVE_INFINITY, ONE, within(ONE));
  }

  @Test
  public void should_pass_if_actual_and_expected_are_NEGATIVE_INFINITY() {
    doubles.assertIsCloseTo(someInfo(), NEGATIVE_INFINITY, NEGATIVE_INFINITY, within(ONE));
  }

  @Test
  public void should_fail_if_actual_is_POSITIVE_INFINITY_and_expected_is_NEGATIVE_INFINITY() {
    thrown.expectAssertionError();
    doubles.assertIsCloseTo(someInfo(), POSITIVE_INFINITY, NEGATIVE_INFINITY, within(ONE));
  }

  @Test
  public void should_fail_if_actual_is_NEGATIVE_INFINITY_and_expected_is_POSITIVE_INFINITY() {
    thrown.expectAssertionError();
    doubles.assertIsCloseTo(someInfo(), NEGATIVE_INFINITY, POSITIVE_INFINITY, within(ONE));
  }
}
