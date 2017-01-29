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

import static java.util.Calendar.*;

import static org.assertj.core.error.ShouldHaveDateField.shouldHaveDateField;
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
 * Tests for <code>{@link Dates#assertHasDayOfWeek(AssertionInfo, Date, int)}</code>.
 * 
 * @author Joel Costigliola
 */
public class Dates_assertHasDayOfWeek_Test extends DatesBaseTest {

  @Test
  public void should_fail_if_actual_has_not_given_day_of_week() {
    AssertionInfo info = someInfo();
    int day_of_week = SUNDAY;
    try {
      dates.assertHasDayOfWeek(info, actual, day_of_week);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveDateField(actual, "day of week", day_of_week));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    dates.assertHasDayOfWeek(someInfo(), null, 1);
  }

  @Test
  public void should_pass_if_actual_has_given_day_of_week() {
    dates.assertHasDayOfWeek(someInfo(), actual, SATURDAY);
  }

  @Test
  public void should_fail_if_actual_has_not_given_day_of_week_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();
    int day_of_week = SUNDAY;
    try {
      datesWithCustomComparisonStrategy.assertHasDayOfWeek(info, actual, day_of_week);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveDateField(actual, "day of week", day_of_week));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    datesWithCustomComparisonStrategy.assertHasDayOfWeek(someInfo(), null, 1);
  }

  @Test
  public void should_pass_if_actual_has_given_day_of_week_whatever_custom_comparison_strategy_is() {
    datesWithCustomComparisonStrategy.assertHasDayOfWeek(someInfo(), actual, SATURDAY);
  }

}
