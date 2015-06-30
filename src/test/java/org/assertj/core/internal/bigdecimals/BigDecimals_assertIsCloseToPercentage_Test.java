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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.internal.bigdecimals;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.withinPercentage;
import static org.assertj.core.data.Percentage.withPercentage;
import static org.assertj.core.error.ShouldBeEqualWithinPercentage.shouldBeEqualWithinPercentage;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.BigDecimalsBaseTest;
import org.junit.Test;

public class BigDecimals_assertIsCloseToPercentage_Test extends BigDecimalsBaseTest {

  private static final BigDecimal TWO = new BigDecimal(2);

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    bigDecimals.assertIsCloseToPercentage(someInfo(), null, ONE, withPercentage(1));
  }

  @Test(expected = NullPointerException.class)
  public void should_fail_if_expected_value_is_null() {
    bigDecimals.assertIsCloseToPercentage(someInfo(), ONE, null, withPercentage(1));
  }

  @Test(expected = NullPointerException.class)
  public void should_fail_if_percentage_is_null() {
    bigDecimals.assertIsCloseToPercentage(someInfo(), ONE, ZERO, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void should_fail_if_percentage_is_negative() {
    bigDecimals.assertIsCloseToPercentage(someInfo(), ONE, ZERO, withPercentage(-1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void should_fail_if_percentage_is_greater_than_one_hundred() {
    bigDecimals.assertIsCloseToPercentage(someInfo(), ONE, ZERO, withPercentage(101));
  }

  @Test
  public void should_pass_if_difference_is_less_than_given_percentage() {
    bigDecimals.assertIsCloseToPercentage(someInfo(), ONE, ONE, withPercentage(1));
    bigDecimals.assertIsCloseToPercentage(someInfo(), ONE, TWO, withPercentage(100));
  }

  @Test
  public void should_pass_if_difference_is_equal_to_given_percentage() {
    bigDecimals.assertIsCloseToPercentage(someInfo(), ONE, ONE, withPercentage(0));
    bigDecimals.assertIsCloseToPercentage(someInfo(), ONE, TWO, withPercentage(50));
  }

  @Test
  public void should_fail_if_actual_is_not_close_enough_to_expected_value() {
    AssertionInfo info = someInfo();
    try {
      bigDecimals.assertIsCloseToPercentage(someInfo(), ONE, TEN, withPercentage(10));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeEqualWithinPercentage(ONE, TEN, withinPercentage(10), TEN.subtract(ONE)));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
