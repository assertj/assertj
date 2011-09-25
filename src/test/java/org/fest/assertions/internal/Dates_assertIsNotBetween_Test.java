/*
 * Created on Dec 24, 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.internal;

import static org.fest.assertions.error.ShouldNotBeBetween.shouldNotBeBetween;
import static org.fest.assertions.test.ErrorMessages.*;
import static org.fest.assertions.test.FailureMessages.actualIsNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.assertions.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;

import static org.mockito.Mockito.verify;

import java.util.Date;

import org.junit.*;

import org.fest.assertions.core.AssertionInfo;

/**
 * Tests for <code>{@link Dates#assertIsNotBetween(AssertionInfo, Date, Date, Date, boolean, boolean)}</code>.
 *
 * @author Joel Costigliola
 */
public class Dates_assertIsNotBetween_Test extends AbstractDatesTest {

  @Override
  @Before
  public void setUp() {
    super.setUp();
    actual = parseDate("2011-09-27");
  }

  @Test
  public void should_fail_if_actual_is_between_given_period() {
    AssertionInfo info = someInfo();
    Date start = parseDate("2011-09-01");
    Date end = parseDate("2011-09-30");
    boolean inclusiveStart = true;
    boolean inclusiveEnd = true;
    try {
      dates.assertIsNotBetween(info, actual, start, end, inclusiveStart, inclusiveEnd);
    } catch (AssertionError e) {
      verifyFailureThrownWhenActualIsNotBetweenGivenPeriod(info, actual, start, end, inclusiveStart, inclusiveEnd);
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_equals_to_start_of_given_period_and_start_is_included_in_given_period() {
    AssertionInfo info = someInfo();
    actual = parseDate("2011-09-01");
    Date start = parseDate("2011-09-01");
    Date end = parseDate("2011-09-30");
    boolean inclusiveStart = true;
    boolean inclusiveEnd = false;
    try {
      dates.assertIsNotBetween(info, actual, start, end, inclusiveStart, inclusiveEnd);
    } catch (AssertionError e) {
      verifyFailureThrownWhenActualIsNotBetweenGivenPeriod(info, actual, start, end, inclusiveStart, inclusiveEnd);
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_equals_to_end_of_given_period_and_end_is_included_in_given_period() {
    AssertionInfo info = someInfo();
    actual = parseDate("2011-09-30");
    Date start = parseDate("2011-09-01");
    Date end = parseDate("2011-09-30");
    boolean inclusiveStart = false;
    boolean inclusiveEnd = true;
    try {
      dates.assertIsNotBetween(info, actual, start, end, inclusiveStart, inclusiveEnd);
    } catch (AssertionError e) {
      verifyFailureThrownWhenActualIsNotBetweenGivenPeriod(info, actual, start, end, inclusiveStart, inclusiveEnd);
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_throw_error_if_start_date_is_null() {
    thrown.expectNullPointerException(startDateToCompareActualWithIsNull());
    Date end = parseDate("2011-09-30");
    dates.assertIsNotBetween(someInfo(), actual, null, end, true, true);
  }

  @Test
  public void should_throw_error_if_end_date_is_null() {
    thrown.expectNullPointerException(endDateToCompareActualWithIsNull());
    Date start = parseDate("2011-09-01");
    dates.assertIsNotBetween(someInfo(), actual, start, null, true, true);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    Date start = parseDate("2011-09-01");
    Date end = parseDate("2011-09-30");
    dates.assertIsNotBetween(someInfo(), null, start, end, true, true);
  }

  @Test
  public void should_pass_if_actual_is_not_between_given_period() {
    actual = parseDate("2011-12-31");
    Date start = parseDate("2011-09-01");
    Date end = parseDate("2011-09-30");
    dates.assertIsNotBetween(someInfo(), actual, start, end, true, true);
  }

  @Test
  public void should_pass_if_actual_is_equals_to_start_of_given_period_and_start_is_not_included_in_given_period() {
    actual = parseDate("2011-09-01");
    Date start = parseDate("2011-09-01");
    Date end = parseDate("2011-09-30");
    dates.assertIsNotBetween(someInfo(), actual, start, end, false, false);
    dates.assertIsNotBetween(someInfo(), actual, start, end, false, true);
  }

  @Test
  public void should_pass_if_actual_is_equals_to_end_of_given_period_and_end_is_not_included_in_given_period() {
    actual = parseDate("2011-09-30");
    Date start = parseDate("2011-09-01");
    Date end = parseDate("2011-09-30");
    dates.assertIsNotBetween(someInfo(), actual, start, end, false, false);
    dates.assertIsNotBetween(someInfo(), actual, start, end, true, false);
  }

  private void verifyFailureThrownWhenActualIsNotBetweenGivenPeriod(AssertionInfo info, Date actualDate, Date start,
      Date end, boolean inclusiveStart, boolean inclusiveEnd) {
    verify(failures).failure(info, shouldNotBeBetween(actualDate, start, end, inclusiveStart, inclusiveEnd));
  }

}
