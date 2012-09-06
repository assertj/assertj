/*
 * Created on Dec 24, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.internal.dates;

import static org.fest.assertions.error.ShouldBeInSameHour.shouldBeInSameHour;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.assertions.test.ErrorMessages.dateToCompareActualWithIsNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;

import static org.mockito.Mockito.verify;

import java.util.Date;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.Dates;
import org.fest.assertions.internal.DatesBaseTest;

/**
 * Tests for <code>{@link Dates#assertIsInSameHourAs(AssertionInfo, Date, Date)}</code>.
 * 
 * @author Joel Costigliola
 */
public class Dates_assertIsInSameHourAs_Test extends DatesBaseTest {

  @Override
  protected void initActualDate() {
    actual = parseDatetime("2011-01-01T03:01:02");
  }

  @Test
  public void should_fail_if_actual_is_not_in_same_hour_as_given_date() {
    AssertionInfo info = someInfo();
    Date other = parseDatetime("2011-01-01T04:01:02");
    try {
      dates.assertIsInSameHourAs(info, actual, other);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeInSameHour(actual, other));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    dates.assertIsInSameHourAs(someInfo(), null, new Date());
  }

  @Test
  public void should_throw_error_if_given_date_is_null() {
    thrown.expectNullPointerException(dateToCompareActualWithIsNull());
    dates.assertIsInSameHourAs(someInfo(), actual, null);
  }

  @Test
  public void should_pass_if_actual_is_in_same_hour_as_given_date() {
    dates.assertIsInSameHourAs(someInfo(), actual, parseDatetime("2011-01-01T03:59:02"));
  }

  @Test
  public void should_fail_if_actual_is_not_in_same_hour_as_given_date_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();
    Date other = parseDatetime("2011-01-01T04:01:02");
    try {
      datesWithCustomComparisonStrategy.assertIsInSameHourAs(info, actual, other);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeInSameHour(actual, other));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    datesWithCustomComparisonStrategy.assertIsInSameHourAs(someInfo(), null, new Date());
  }

  @Test
  public void should_throw_error_if_given_date_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectNullPointerException(dateToCompareActualWithIsNull());
    datesWithCustomComparisonStrategy.assertIsInSameHourAs(someInfo(), actual, null);
  }

  @Test
  public void should_pass_if_actual_is_in_same_hour_as_given_date_whatever_custom_comparison_strategy_is() {
    datesWithCustomComparisonStrategy.assertIsInSameHourAs(someInfo(), actual, parseDatetime("2011-01-01T03:59:02"));
  }

}
