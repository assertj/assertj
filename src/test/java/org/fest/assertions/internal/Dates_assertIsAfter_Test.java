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

import static org.fest.assertions.error.ShouldBeAfter.shouldBeAfter;
import static org.fest.assertions.test.ErrorMessages.dateToCompareActualWithIsNull;
import static org.fest.assertions.test.FailureMessages.actualIsNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.assertions.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;

import static org.mockito.Mockito.verify;

import java.util.Date;

import org.junit.*;

import org.fest.assertions.core.AssertionInfo;

/**
 * Tests for <code>{@link Dates#assertIsAfter(AssertionInfo, java.util.Date, java.util.Date)}</code>.
 *
 * @author Joel Costigliola
 */
public class Dates_assertIsAfter_Test extends AbstractDatesTest {

  @Override
  @Before
  public void setUp() {
    super.setUp();
    actual = parseDate("2011-01-01");
  }

  @Test
  public void should_fail_if_actual_is_not_strictly_after_given_date() {
    AssertionInfo info = someInfo();
    Date other = parseDate("2022-01-01");
    try {
      dates.assertIsAfter(info, actual, other);
    } catch (AssertionError e) {
      verifyFailureThrownWhenActualIsNotStrictlyAfterOtherDate(info, actual, other);
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_equals_to_given_date() {
    AssertionInfo info = someInfo();
    Date other = parseDate("2011-01-01");
    try {
      dates.assertIsAfter(info, actual, other);
    } catch (AssertionError e) {
      verifyFailureThrownWhenActualIsNotStrictlyAfterOtherDate(info, actual, other);
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_throw_error_if_given_date_is_null() {
    thrown.expectNullPointerException(dateToCompareActualWithIsNull());
    dates.assertIsAfter(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    dates.assertIsAfter(someInfo(), null, parseDate("2010-01-01"));
  }

  @Test
  public void should_pass_if_actual_is_strictly_after_given_date() {
    dates.assertIsAfter(someInfo(), actual, parseDate("2000-01-01"));
  }

  private void verifyFailureThrownWhenActualIsNotStrictlyAfterOtherDate(AssertionInfo info, Date actualDate,
      Date otherDate) {
    verify(failures).failure(info, shouldBeAfter(actualDate, otherDate));
  }

}
