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
 * Tests for <code>{@link Dates#assertHasMillisecond(AssertionInfo, Date, int)}</code>.
 * 
 * @author Joel Costigliola
 */
public class Dates_assertHasMillisecond_Test extends DatesBaseTest {

  @Override
  protected void initActualDate() {
    actual = new Date(parseDatetime("2011-01-01T03:49:17").getTime() + 13);
  }

  @Test
  public void should_fail_if_actual_has_not_given_millisecond() {
    AssertionInfo info = someInfo();
    int millisecond = 5;
    try {
      dates.assertHasMillisecond(info, actual, millisecond);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveDateField(actual, "millisecond", millisecond));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    dates.assertHasMillisecond(someInfo(), null, 13);
  }

  @Test
  public void should_pass_if_actual_has_given_millisecond() {
    dates.assertHasMillisecond(someInfo(), actual, 13);
  }

  @Test
  public void should_fail_if_actual_has_not_given_millisecond_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();
    int millisecond = 5;
    try {
      datesWithCustomComparisonStrategy.assertHasMillisecond(info, actual, millisecond);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveDateField(actual, "millisecond", millisecond));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    datesWithCustomComparisonStrategy.assertHasMillisecond(someInfo(), null, 13);
  }

  @Test
  public void should_pass_if_actual_has_given_millisecond_whatever_custom_comparison_strategy_is() {
    datesWithCustomComparisonStrategy.assertHasMillisecond(someInfo(), actual, 13);
  }

}
