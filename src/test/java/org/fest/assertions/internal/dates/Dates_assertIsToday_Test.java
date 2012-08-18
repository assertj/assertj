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

import static org.fest.assertions.error.ShouldBeToday.shouldBeToday;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.fest.util.Dates.*;

import static org.mockito.Mockito.verify;

import java.util.Date;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.Dates;
import org.fest.assertions.internal.DatesBaseTest;

/**
 * Tests for <code>{@link Dates#assertIsToday(AssertionInfo, Date)}</code>.
 * 
 * @author Joel Costigliola
 */
public class Dates_assertIsToday_Test extends DatesBaseTest {

  @Test
  public void should_fail_if_actual_is_not_today() {
    AssertionInfo info = someInfo();
    try {
      actual = parseDate("2111-01-01");
      dates.assertIsToday(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeToday(actual));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    dates.assertIsToday(someInfo(), null);
  }

  @Test
  public void should_pass_if_actual_is_today() {
    dates.assertIsToday(someInfo(), new Date());
  }

  @Test
  public void should_fail_if_actual_is_not_today_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    try {
      actual = parseDate("2111-01-01");
      datesWithCustomComparisonStrategy.assertIsToday(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeToday(actual, yearAndMonthComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    datesWithCustomComparisonStrategy.assertIsToday(someInfo(), null);
  }

  @Test
  public void should_pass_if_actual_is_today_according_to_custom_comparison_strategy() {
    // we want actual to be different from today but still in the same month so that it is equal to today
    // according to our comparison strategy (that compares only month and year).
    // => if we are at the end of the month we subtract one day instead of adding one
    actual = monthOf(tomorrow()) == monthOf(new Date()) ? tomorrow() : yesterday();
    datesWithCustomComparisonStrategy.assertIsToday(someInfo(), actual);
  }

}
