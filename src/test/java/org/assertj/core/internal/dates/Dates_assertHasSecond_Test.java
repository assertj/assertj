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
 * Tests for <code>{@link Dates#assertHasSecond(AssertionInfo, Date, int)}</code>.
 * 
 * @author Joel Costigliola
 */
public class Dates_assertHasSecond_Test extends DatesBaseTest {

  @Override
  protected void initActualDate() {
    actual = parseDatetime("2011-01-01T03:49:17");
  }

  @Test
  public void should_fail_if_actual_has_not_given_second() {
    AssertionInfo info = someInfo();
    int second = 5;
    try {
      dates.assertHasSecond(info, actual, second);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveDateField(actual, "second", second));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    dates.assertHasSecond(someInfo(), null, 17);
  }

  @Test
  public void should_pass_if_actual_has_given_second() {
    dates.assertHasSecond(someInfo(), actual, 17);
  }

  @Test
  public void should_fail_if_actual_has_not_given_second_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();
    int second = 5;
    try {
      datesWithCustomComparisonStrategy.assertHasSecond(info, actual, second);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveDateField(actual, "second", second));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    datesWithCustomComparisonStrategy.assertHasSecond(someInfo(), null, 17);
  }

  @Test
  public void should_pass_if_actual_has_given_second_whatever_custom_comparison_strategy_is() {
    datesWithCustomComparisonStrategy.assertHasSecond(someInfo(), actual, 17);
  }

}
