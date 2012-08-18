/*
 * Created on Jul 20,2012
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

import static org.fest.assertions.error.ShouldHaveTime.shouldHaveTime;
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
 * Tests for <code>{@link Dates#assertHasTime(AssertionInfo, Date, long)}</code>.
 * 
 * @author Guillaume Girou
 * @author Nicolas François
 */
public class Dates_assertHasTime_Test extends DatesBaseTest {

  @Override
  protected void initActualDate() {
    actual = new Date(42);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    dates.assertHasTime(someInfo(), null, 1);
  }

  @Test
  public void should_pass_if_actual_has_same_time() {
    dates.assertHasTime(someInfo(), actual, 42L);
  }

  @Test()
  public void should_fail_if_actual_has_not_same_time() {
    AssertionInfo info = someInfo();
    try {
      dates.assertHasTime(someInfo(), actual, 24L);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveTime(actual, 24L));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
