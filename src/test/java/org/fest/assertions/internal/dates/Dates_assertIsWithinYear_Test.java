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

import static org.fest.assertions.error.ShouldBeWithin.shouldBeWithin;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;

import static org.mockito.Mockito.verify;

import java.util.Date;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.Dates;
import org.fest.assertions.internal.DatesBaseTest;

/**
 * Tests for <code>{@link Dates#assertIsWithinYear(AssertionInfo, Date, int)}</code>.
 * 
 * @author Joel Costigliola
 */
public class Dates_assertIsWithinYear_Test extends DatesBaseTest {

  @Test
  public void should_fail_if_actual_is_not_within_given_year() {
    AssertionInfo info = someInfo();
    int year = 2010;
    try {
      dates.assertIsWithinYear(info, actual, year);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeWithin(actual, "year", year));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    dates.assertIsWithinYear(someInfo(), null, 2010);
  }

  @Test
  public void should_pass_if_actual_is_within_given_year() {
    dates.assertIsWithinYear(someInfo(), actual, 2011);
  }

  @Test
  public void should_fail_if_actual_is_not_within_given_year_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();
    int year = 2010;
    try {
      datesWithCustomComparisonStrategy.assertIsWithinYear(info, actual, year);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeWithin(actual, "year", year));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    datesWithCustomComparisonStrategy.assertIsWithinYear(someInfo(), null, 2010);
  }

  @Test
  public void should_pass_if_actual_is_within_given_year_whatever_custom_comparison_strategy_is() {
    datesWithCustomComparisonStrategy.assertIsWithinYear(someInfo(), actual, 2011);
  }

}
