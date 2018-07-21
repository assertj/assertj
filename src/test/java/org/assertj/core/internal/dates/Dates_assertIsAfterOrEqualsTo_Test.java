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
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.error.ShouldBeAfterOrEqualsTo.shouldBeAfterOrEqualsTo;
import static org.assertj.core.internal.ErrorMessages.dateToCompareActualWithIsNull;
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
 * Tests for <code>{@link Dates#assertIsAfterOrEqualsTo(AssertionInfo, Date, Date)}</code>.
 * 
 * @author Joel Costigliola
 */
public class Dates_assertIsAfterOrEqualsTo_Test extends DatesBaseTest {

  @Test
  public void should_fail_if_actual_is_not_strictly_after_given_date() {
    AssertionInfo info = someInfo();
    Date other = parseDate("2022-01-01");
    try {
      dates.assertIsAfterOrEqualsTo(info, actual, other);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeAfterOrEqualsTo(actual, other));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_throw_error_if_given_date_is_null() {
    assertThatNullPointerException().isThrownBy(() -> dates.assertIsAfterOrEqualsTo(someInfo(), actual, null))
                                    .withMessage(dateToCompareActualWithIsNull());
  }

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> dates.assertIsAfterOrEqualsTo(someInfo(), null, parseDate("2010-01-01")))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_pass_if_actual_is_strictly_after_given_date() {
    dates.assertIsAfterOrEqualsTo(someInfo(), actual, parseDate("2000-01-01"));
  }

  @Test
  public void should_pass_if_actual_is_equals_to_given_date() {
    dates.assertIsAfterOrEqualsTo(someInfo(), actual, parseDate("2011-01-01"));
  }

  @Test
  public void should_fail_if_actual_is_not_strictly_after_given_date_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Date other = parseDate("2022-01-01");
    try {
      datesWithCustomComparisonStrategy.assertIsAfterOrEqualsTo(info, actual, other);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeAfterOrEqualsTo(actual, other, yearAndMonthComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_throw_error_if_given_date_is_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> datesWithCustomComparisonStrategy.assertIsAfterOrEqualsTo(someInfo(),
                                                                                                                actual,
                                                                                                                null))
                                    .withMessage(dateToCompareActualWithIsNull());
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> datesWithCustomComparisonStrategy.assertIsAfterOrEqualsTo(someInfo(), null, parseDate("2010-01-01")))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_pass_if_actual_is_strictly_after_given_date_according_to_custom_comparison_strategy() {
    datesWithCustomComparisonStrategy.assertIsAfterOrEqualsTo(someInfo(), actual, parseDate("2000-01-01"));
  }

  @Test
  public void should_pass_if_actual_is_equals_to_given_date_according_to_custom_comparison_strategy() {
    datesWithCustomComparisonStrategy.assertIsAfterOrEqualsTo(someInfo(), actual, parseDate("2011-01-31"));
  }

}
