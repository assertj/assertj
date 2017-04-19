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
package org.assertj.core.internal.dates;

import static org.assertj.core.error.ShouldBeInSameMonth.shouldBeInSameMonth;
import static org.assertj.core.internal.ErrorMessages.dateToCompareActualWithIsNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;


import static org.mockito.Mockito.verify;

import java.util.Date;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Dates;
import org.assertj.core.internal.DatesBaseTest;
import org.junit.Test;


/**
 * Tests for <code>{@link Dates#assertIsInSameMonthAs(AssertionInfo, Date, Date)}</code>.
 * 
 * @author Joel Costigliola
 */
public class Dates_assertIsInSameMonthAs_Test extends DatesBaseTest {

  @Test
  public void should_fail_if_actual_is_not_in_same_month_as_given_date() {
    AssertionInfo info = someInfo();
    Date other = parseDate("2011-02-01");
    try {
      dates.assertIsInSameMonthAs(info, actual, other);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeInSameMonth(actual, other));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    dates.assertIsInSameMonthAs(someInfo(), null, new Date());
  }

  @Test
  public void should_throw_error_if_given_date_is_null() {
    thrown.expectNullPointerException(dateToCompareActualWithIsNull());
    dates.assertIsInSameMonthAs(someInfo(), actual, null);
  }

  @Test
  public void should_pass_if_actual_is_in_same_month_as_given_date() {
    dates.assertIsInSameMonthAs(someInfo(), actual, parseDate("2011-01-11"));
  }

  @Test
  public void should_fail_if_actual_is_not_in_same_month_as_given_date_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();
    Date other = parseDate("2011-02-01");
    try {
      datesWithCustomComparisonStrategy.assertIsInSameMonthAs(info, actual, other);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeInSameMonth(actual, other));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    datesWithCustomComparisonStrategy.assertIsInSameMonthAs(someInfo(), null, new Date());
  }

  @Test
  public void should_throw_error_if_given_date_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectNullPointerException(dateToCompareActualWithIsNull());
    datesWithCustomComparisonStrategy.assertIsInSameMonthAs(someInfo(), actual, null);
  }

  @Test
  public void should_pass_if_actual_is_in_same_month_as_given_date_whatever_custom_comparison_strategy_is() {
    datesWithCustomComparisonStrategy.assertIsInSameMonthAs(someInfo(), actual, parseDate("2011-01-11"));
  }

}
