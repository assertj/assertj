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

import static org.assertj.core.error.ShouldBeInSameSecondWindow.shouldBeInSameSecondWindow;
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
 * Tests for <code>{@link Dates#assertIsInSameSecondWindowAs(AssertionInfo, Date, Date)}</code>.
 * 
 * @author Joel Costigliola
 */
public class Dates_assertIsInSameSecondWindowAs_Test extends DatesBaseTest {

  @Override
  protected void initActualDate() {
    actual = parseDatetime("2011-01-01T03:15:05");
  }

  @Test
  public void should_pass_if_actual_is_in_same_second_window_as_given_date() {
    dates.assertIsInSameSecondWindowAs(someInfo(), actual, parseDatetimeWithMs("2011-01-01T03:15:05.999"));
    dates.assertIsInSameSecondWindowAs(someInfo(), actual, parseDatetimeWithMs("2011-01-01T03:15:05.001"));
    // in that test, the two dates have different seconds fields : 05 and 04 but their diff < 1s
    dates.assertIsInSameSecondWindowAs(someInfo(), actual, new Date(actual.getTime() - 1));
  }

  @Test
  public void should_fail_if_actual_is_not_in_same_second_as_given_date() {
    AssertionInfo info = someInfo();
    Date other = parseDatetime("2011-01-01T03:15:02");
    try {
      dates.assertIsInSameSecondWindowAs(info, actual, other);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeInSameSecondWindow(actual, other));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_if_dates_time_difference_is_exactly_one_second() {
    AssertionInfo info = someInfo();
    Date other = parseDatetime("2011-01-01T03:15:06");
    try {
      dates.assertIsInSameSecondWindowAs(info, actual, other);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeInSameSecondWindow(actual, other));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    dates.assertIsInSameSecondWindowAs(someInfo(), null, new Date());
  }

  @Test
  public void should_throw_error_if_given_date_is_null() {
    thrown.expectNullPointerException(dateToCompareActualWithIsNull());
    dates.assertIsInSameSecondWindowAs(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_actual_is_not_in_same_second_as_given_date_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();
    Date other = parseDatetime("2011-01-01T03:15:02");
    try {
      datesWithCustomComparisonStrategy.assertIsInSameSecondWindowAs(info, actual, other);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeInSameSecondWindow(actual, other));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    datesWithCustomComparisonStrategy.assertIsInSameSecondWindowAs(someInfo(), null, new Date());
  }

  @Test
  public void should_throw_error_if_given_date_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectNullPointerException(dateToCompareActualWithIsNull());
    datesWithCustomComparisonStrategy.assertIsInSameSecondWindowAs(someInfo(), actual, null);
  }

  @Test
  public void should_pass_if_actual_is_in_same_second_as_given_date_whatever_custom_comparison_strategy_is() {
    Date other = parseDatetime("2011-01-01T03:15:05");
    datesWithCustomComparisonStrategy.assertIsInSameSecondWindowAs(someInfo(), actual, other);
    datesWithCustomComparisonStrategy.assertIsInSameSecondWindowAs(someInfo(), actual, new Date(other.getTime() + 999));
  }

}
