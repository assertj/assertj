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

import static org.assertj.core.error.ShouldBeBetween.shouldBeBetween;
import static org.assertj.core.internal.ErrorMessages.endDateToCompareActualWithIsNull;
import static org.assertj.core.internal.ErrorMessages.startDateToCompareActualWithIsNull;
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
 * Tests for <code>{@link Dates#assertIsBetween(AssertionInfo, Date, Date, Date, boolean, boolean)}</code>.
 * 
 * @author Joel Costigliola
 */
public class Dates_assertIsBetween_Test extends DatesBaseTest {

  @Override
  protected void initActualDate() {
    actual = parseDate("2011-09-27");
  }

  @Test
  public void should_fail_if_actual_is_not_between_given_period() {
    AssertionInfo info = someInfo();
    actual = parseDate("2011-10-01");
    Date start = parseDate("2011-09-01");
    Date end = parseDate("2011-09-30");
    boolean inclusiveStart = true;
    boolean inclusiveEnd = true;
    try {
      dates.assertIsBetween(info, actual, start, end, inclusiveStart, inclusiveEnd);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeBetween(actual, start, end, inclusiveStart, inclusiveEnd));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_equals_to_start_of_given_period_and_start_is_not_included_in_given_period() {
    AssertionInfo info = someInfo();
    actual = parseDate("2011-09-01");
    Date start = parseDate("2011-09-01");
    Date end = parseDate("2011-09-30");
    boolean inclusiveStart = false;
    boolean inclusiveEnd = true;
    try {
      dates.assertIsBetween(info, actual, start, end, inclusiveStart, inclusiveEnd);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeBetween(actual, start, end, inclusiveStart, inclusiveEnd));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_equals_to_end_of_given_period_and_end_is_not_included_in_given_period() {
    AssertionInfo info = someInfo();
    actual = parseDate("2011-09-30");
    Date start = parseDate("2011-09-01");
    Date end = parseDate("2011-09-30");
    boolean inclusiveStart = true;
    boolean inclusiveEnd = false;
    try {
      dates.assertIsBetween(info, actual, start, end, inclusiveStart, inclusiveEnd);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeBetween(actual, start, end, inclusiveStart, inclusiveEnd));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_throw_error_if_start_date_is_null() {
    thrown.expectNullPointerException(startDateToCompareActualWithIsNull());
    Date end = parseDate("2011-09-30");
    dates.assertIsBetween(someInfo(), actual, null, end, true, true);
  }

  @Test
  public void should_throw_error_if_end_date_is_null() {
    thrown.expectNullPointerException(endDateToCompareActualWithIsNull());
    Date start = parseDate("2011-09-01");
    dates.assertIsBetween(someInfo(), actual, start, null, true, true);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    Date start = parseDate("2011-09-01");
    Date end = parseDate("2011-09-30");
    dates.assertIsBetween(someInfo(), null, start, end, true, true);
  }

  @Test
  public void should_pass_if_actual_is_strictly_between_given_period() {
    Date start = parseDate("2011-09-01");
    Date end = parseDate("2011-09-30");
    dates.assertIsBetween(someInfo(), actual, start, end, false, false);
  }

  @Test
  public void should_pass_if_actual_is_equals_to_start_of_given_period_and_start_is_included_in_given_period() {
    actual = parseDate("2011-09-01");
    Date start = parseDate("2011-09-01");
    Date end = parseDate("2011-09-30");
    dates.assertIsBetween(someInfo(), actual, start, end, true, false);
    dates.assertIsBetween(someInfo(), actual, start, end, true, true);
  }

  @Test
  public void should_pass_if_actual_is_equals_to_end_of_given_period_and_end_is_included_in_given_period() {
    actual = parseDate("2011-09-30");
    Date start = parseDate("2011-09-01");
    Date end = parseDate("2011-09-30");
    dates.assertIsBetween(someInfo(), actual, start, end, false, true);
    dates.assertIsBetween(someInfo(), actual, start, end, true, true);
  }

  @Test
  public void should_fail_if_actual_is_not_between_given_period_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    actual = parseDate("2011-10-01");
    Date start = parseDate("2011-09-01");
    Date end = parseDate("2011-09-30");
    boolean inclusiveStart = true;
    boolean inclusiveEnd = true;
    try {
      datesWithCustomComparisonStrategy.assertIsBetween(info, actual, start, end, inclusiveStart, inclusiveEnd);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldBeBetween(actual, start, end, inclusiveStart, inclusiveEnd, yearAndMonthComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_equals_to_start_of_given_period_and_start_is_not_included_in_given_period_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    actual = parseDate("2011-09-01");
    Date start = parseDate("2011-09-30"); // = 2011-09-01 according to comparison strategy
    Date end = parseDate("2011-10-30");
    boolean inclusiveStart = false;
    boolean inclusiveEnd = true;
    try {
      datesWithCustomComparisonStrategy.assertIsBetween(info, actual, start, end, inclusiveStart, inclusiveEnd);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldBeBetween(actual, start, end, inclusiveStart, inclusiveEnd, yearAndMonthComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_equals_to_end_of_given_period_and_end_is_not_included_in_given_period_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    actual = parseDate("2011-09-01");
    Date start = parseDate("2011-08-01");
    Date end = parseDate("2011-09-30"); // = 2011-09-01 according to comparison strategy
    boolean inclusiveStart = true;
    boolean inclusiveEnd = false;
    try {
      datesWithCustomComparisonStrategy.assertIsBetween(info, actual, start, end, inclusiveStart, inclusiveEnd);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldBeBetween(actual, start, end, inclusiveStart, inclusiveEnd, yearAndMonthComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_throw_error_if_start_date_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectNullPointerException(startDateToCompareActualWithIsNull());
    Date end = parseDate("2011-09-30");
    datesWithCustomComparisonStrategy.assertIsBetween(someInfo(), actual, null, end, true, true);
  }

  @Test
  public void should_throw_error_if_end_date_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectNullPointerException(endDateToCompareActualWithIsNull());
    Date start = parseDate("2011-09-01");
    datesWithCustomComparisonStrategy.assertIsBetween(someInfo(), actual, start, null, true, true);
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    Date start = parseDate("2011-09-01");
    Date end = parseDate("2011-09-30");
    datesWithCustomComparisonStrategy.assertIsBetween(someInfo(), null, start, end, true, true);
  }

  @Test
  public void should_pass_if_actual_is_strictly_between_given_period_according_to_custom_comparison_strategy() {
    Date start = parseDate("2011-08-30");
    Date end = parseDate("2011-10-01");
    datesWithCustomComparisonStrategy.assertIsBetween(someInfo(), actual, start, end, false, false);
  }

  @Test
  public void should_pass_if_actual_is_equals_to_start_of_given_period_and_start_is_included_in_given_period_according_to_custom_comparison_strategy() {
    actual = parseDate("2011-09-01");
    Date start = parseDate("2011-09-10"); // = 2011-09-01 according to comparison strategy
    Date end = parseDate("2011-10-01");
    datesWithCustomComparisonStrategy.assertIsBetween(someInfo(), actual, start, end, true, false);
    datesWithCustomComparisonStrategy.assertIsBetween(someInfo(), actual, start, end, true, true);
  }

  @Test
  public void should_pass_if_actual_is_equals_to_end_of_given_period_and_end_is_included_in_given_period_according_to_custom_comparison_strategy() {
    actual = parseDate("2011-09-15");
    Date start = parseDate("2011-08-30");
    Date end = parseDate("2011-09-30"); // = 2011-09-01 according to comparison strategy
    datesWithCustomComparisonStrategy.assertIsBetween(someInfo(), actual, start, end, false, true);
    datesWithCustomComparisonStrategy.assertIsBetween(someInfo(), actual, start, end, true, true);
  }

}
