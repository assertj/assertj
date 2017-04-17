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

import static org.assertj.core.error.ShouldBeInSameMinuteWindow.shouldBeInSameMinuteWindow;
import static org.assertj.core.internal.ErrorMessages.dateToCompareActualWithIsNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.util.Date;

import org.junit.Test;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Dates;
import org.assertj.core.internal.DatesBaseTest;


/**
 * Tests for <code>{@link Dates#assertIsInSameMinuteWindowAs(AssertionInfo, Date, Date)}</code>.
 *
 * @author Joel Costigliola
 */
public class Dates_assertIsInSameMinuteWindowAs_Test extends DatesBaseTest {

  @Override
  protected void initActualDate() {
    actual = parseDatetime("2011-01-01T03:15:00");
  }

  @Test
  public void should_pass_if_actual_is_in_same_minute_window_as_given_date() {
    dates.assertIsInSameMinuteWindowAs(someInfo(), actual, parseDatetime("2011-01-01T03:15:59"));
    dates.assertIsInSameMinuteWindowAs(someInfo(), actual, parseDatetime("2011-01-01T03:14:01"));
  }

  @Test
  public void should_fail_if_actual_is_exactly_one_minute_away_from_given_date() {
    AssertionInfo info = someInfo();
    Date other = parseDatetime("2011-01-01T03:16:00");
    try {
      dates.assertIsInSameMinuteWindowAs(info, actual, other);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeInSameMinuteWindow(actual, other));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_not_in_same_minute_window_as_given_date() {
    AssertionInfo info = someInfo();
    Date other = parseDatetime("2011-01-01T03:16:01");
    try {
      dates.assertIsInSameMinuteWindowAs(info, actual, other);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeInSameMinuteWindow(actual, other));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    dates.assertIsInSameMinuteWindowAs(someInfo(), null, new Date());
  }

  @Test
  public void should_throw_error_if_given_date_is_null() {
    thrown.expectNullPointerException(dateToCompareActualWithIsNull());
    dates.assertIsInSameMinuteWindowAs(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_actual_is_not_in_same_minute_window_as_given_date_whatever_custom_comparison_strategy_is
    () {
    AssertionInfo info = someInfo();
    Date other = parseDatetime("2011-01-01T03:13:59");
    try {
      datesWithCustomComparisonStrategy.assertIsInSameMinuteWindowAs(info, actual, other);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeInSameMinuteWindow(actual, other));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    datesWithCustomComparisonStrategy.assertIsInSameMinuteWindowAs(someInfo(), null, new Date());
  }

  @Test
  public void should_throw_error_if_given_date_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectNullPointerException(dateToCompareActualWithIsNull());
    datesWithCustomComparisonStrategy.assertIsInSameMinuteWindowAs(someInfo(), actual, null);
  }

  @Test
  public void should_pass_if_actual_is_in_same_minute_window_as_given_date_whatever_custom_comparison_strategy_is() {
    datesWithCustomComparisonStrategy.assertIsInSameMinuteWindowAs(someInfo(), actual,
                                                                   parseDatetime("2011-01-01T03:15:59"));
  }

}
