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
package org.assertj.core.internal.dates;

import static org.assertj.core.error.ShouldBeEqualWithTimePrecision.shouldBeEqual;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.mockito.Mockito.verify;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.DatesBaseTest;
import org.junit.Test;


/**
 * Tests for <code>{@link org.assertj.core.internal.Dates#assertIsEqualWithPrecision(org.assertj.core.api.AssertionInfo, java.util.Date, java.util.Date, java.util.concurrent.TimeUnit)}</code>.
 *
 * @author William Delanoue
 */
public class Dates_assertIsEqualWithPrecision_Test extends DatesBaseTest {

  @Override
  protected void initActualDate() {
    actual = parseDatetimeWithMs("2011-09-27T12:23:35.999");
  }

  @Test
  public void should_pass_if_ms_not_equal() {
    AssertionInfo info = someInfo();
    Date other = parseDatetimeWithMs("2011-09-27T12:23:35.998");
    dates.assertIsEqualWithPrecision(info, actual, other, TimeUnit.MILLISECONDS);
  }

  @Test
  public void should_pass_if_seconds_not_equal() {
    AssertionInfo info = someInfo();
    Date other = parseDatetimeWithMs("2011-09-27T12:23:36.999");
    dates.assertIsEqualWithPrecision(info, actual, other, TimeUnit.SECONDS);
  }

  @Test
  public void should_pass_if_minutes_not_equal() {
    AssertionInfo info = someInfo();
    Date other = parseDatetimeWithMs("2011-09-27T12:24:35.999");
    dates.assertIsEqualWithPrecision(info, actual, other, TimeUnit.MINUTES);
  }

  @Test
  public void should_pass_if_hour_not_equal() {
    AssertionInfo info = someInfo();
    Date other = parseDatetimeWithMs("2011-09-27T13:23:35.999");
    dates.assertIsEqualWithPrecision(info, actual, other, TimeUnit.HOURS);
  }

  @Test
  public void should_pass_if_day_not_equal() {
    AssertionInfo info = someInfo();
    Date other = parseDatetimeWithMs("2011-09-28T12:23:35.999");
    dates.assertIsEqualWithPrecision(info, actual, other, TimeUnit.DAYS);
  }

  @Test
  public void should_fail_if_ms_not_equal() {
    AssertionInfo info = someInfo();
    Date other = parseDatetimeWithMs("2011-09-27T12:23:35.998");
    try {
      dates.assertIsEqualWithPrecision(info, actual, other, TimeUnit.MICROSECONDS);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeEqual(actual, other, TimeUnit.MICROSECONDS));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_seconds_not_equal() {
    AssertionInfo info = someInfo();
    Date other = parseDatetimeWithMs("2011-09-27T12:23:36.999");
    try {
      dates.assertIsEqualWithPrecision(info, actual, other, TimeUnit.MILLISECONDS);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeEqual(actual, other, TimeUnit.MILLISECONDS));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_minutes_not_equal() {
    AssertionInfo info = someInfo();
    Date other = parseDatetimeWithMs("2011-09-27T12:24:35.999");
    try {
      dates.assertIsEqualWithPrecision(info, actual, other, TimeUnit.SECONDS);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeEqual(actual, other, TimeUnit.SECONDS));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_hour_not_equal() {
    AssertionInfo info = someInfo();
    Date other = parseDatetimeWithMs("2011-09-27T13:23:35.999");
    try {
      dates.assertIsEqualWithPrecision(info, actual, other, TimeUnit.MINUTES);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeEqual(actual, other, TimeUnit.MINUTES));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_day_not_equal() {
    AssertionInfo info = someInfo();
    Date other = parseDatetimeWithMs("2011-09-28T12:23:35.999");
    try {
      dates.assertIsEqualWithPrecision(info, actual, other, TimeUnit.HOURS);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeEqual(actual, other, TimeUnit.HOURS));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
