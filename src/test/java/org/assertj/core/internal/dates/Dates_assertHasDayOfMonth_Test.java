/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.internal.dates;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.ShouldHaveDateField.shouldHaveDateField;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;


import static org.mockito.Mockito.verify;

import java.util.Date;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Dates;
import org.assertj.core.internal.DatesBaseTest;
import org.junit.jupiter.api.Test;


/**
 * Tests for <code>{@link Dates#assertHasDayOfMonth(AssertionInfo, Date, int)}</code>.
 * 
 * @author Joel Costigliola
 */
public class Dates_assertHasDayOfMonth_Test extends DatesBaseTest {

  @Test
  public void should_fail_if_actual_has_not_given_day_of_month() {
    AssertionInfo info = someInfo();
    int day_of_month = 5;
    try {
      dates.assertHasDayOfMonth(info, actual, day_of_month);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveDateField(actual, "day of month", day_of_month));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> dates.assertHasDayOfMonth(someInfo(), null, 1))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_pass_if_actual_has_given_day_of_month() {
    dates.assertHasDayOfMonth(someInfo(), actual, 1);
  }

  @Test
  public void should_fail_if_actual_has_not_given_day_of_month_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();
    int day_of_month = 5;
    try {
      datesWithCustomComparisonStrategy.assertHasDayOfMonth(info, actual, day_of_month);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveDateField(actual, "day of month", day_of_month));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> datesWithCustomComparisonStrategy.assertHasDayOfMonth(someInfo(), null, 1))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_pass_if_actual_has_given_day_of_month_whatever_custom_comparison_strategy_is() {
    datesWithCustomComparisonStrategy.assertHasDayOfMonth(someInfo(), actual, 1);
  }

}
