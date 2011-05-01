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

import static org.fest.assertions.error.ShouldBeBetween.shouldBeBetween;
import static org.fest.assertions.test.ErrorMessages.*;
import static org.fest.assertions.test.FailureMessages.actualIsNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.assertions.test.TestFailures.expectedAssertionErrorNotThrown;

import static org.mockito.Mockito.verify;

import java.util.Date;

import org.junit.*;

import org.fest.assertions.core.AssertionInfo;

/**
 * Tests for <code>{@link Dates#assertIsBetween(AssertionInfo, Date, Date, Date, boolean, boolean)}</code>.
 *
 * @author Joel Costigliola
 */
public class Dates_assertIsBetween_Test extends AbstractDatesTest {

  @Override
  @Before
  public void setUp() {
    super.setUp();
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
      verifyFailureThrownWhenActualIsBetweenGivenPeriod(info, actual, start, end, inclusiveStart, inclusiveEnd);
      return;
    }
    throw expectedAssertionErrorNotThrown();
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
      verifyFailureThrownWhenActualIsBetweenGivenPeriod(info, actual, start, end, inclusiveStart, inclusiveEnd);
      return;
    }
    throw expectedAssertionErrorNotThrown();
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
      verifyFailureThrownWhenActualIsBetweenGivenPeriod(info, actual, start, end, inclusiveStart, inclusiveEnd);
      return;
    }
    throw expectedAssertionErrorNotThrown();
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
    dates.assertIsBetween(someInfo(), actual, start, end, true, true);
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

  private void verifyFailureThrownWhenActualIsBetweenGivenPeriod(AssertionInfo info, Date actualDate, Date start,
      Date end, boolean inclusiveStart, boolean inclusiveEnd) {
    verify(failures).failure(info, shouldBeBetween(actualDate, start, end, inclusiveStart, inclusiveEnd));
  }

}
