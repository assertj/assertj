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
package org.assertj.core.internal.dates;

import static org.assertj.core.error.ShouldBeWithin.shouldBeWithin;
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
 * Tests for <code>{@link Dates#assertIsWithinHourOfDay(AssertionInfo, Date, int)}</code>.
 * 
 * @author Joel Costigliola
 */
public class Dates_assertIsWithinHourOfDay_Test extends DatesBaseTest {

  @Override
  protected void initActualDate() {
    actual = parseDatetime("2011-01-01T03:01:02");
  }

  @Test
  public void should_fail_if_actual_is_not_within_given_hour_of_day() {
    AssertionInfo info = someInfo();
    int hour_of_day = 5;
    try {
      dates.assertIsWithinHourOfDay(info, actual, hour_of_day);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeWithin(actual, "hour", hour_of_day));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    dates.assertIsWithinHourOfDay(someInfo(), null, 3);
  }

  @Test
  public void should_pass_if_actual_is_within_given_hour_of_day() {
    dates.assertIsWithinHourOfDay(someInfo(), actual, 3);
  }

  @Test
  public void should_fail_if_actual_is_not_within_given_hour_of_day_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();
    int hour_of_day = 5;
    try {
      datesWithCustomComparisonStrategy.assertIsWithinHourOfDay(info, actual, hour_of_day);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeWithin(actual, "hour", hour_of_day));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    datesWithCustomComparisonStrategy.assertIsWithinHourOfDay(someInfo(), null, 3);
  }

  @Test
  public void should_pass_if_actual_is_within_given_hour_of_day_whatever_custom_comparison_strategy_is() {
    datesWithCustomComparisonStrategy.assertIsWithinHourOfDay(someInfo(), actual, 3);
  }

}
