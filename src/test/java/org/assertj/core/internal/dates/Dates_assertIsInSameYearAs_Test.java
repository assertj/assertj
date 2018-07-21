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
import static org.assertj.core.error.ShouldBeInSameYear.shouldBeInSameYear;
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
 * Tests for <code>{@link Dates#assertIsInSameYearAs(AssertionInfo, Date, Date)}</code>.
 * 
 * @author Joel Costigliola
 */
public class Dates_assertIsInSameYearAs_Test extends DatesBaseTest {

  @Test
  public void should_fail_if_actual_is_not_in_same_year_as_given_date() {
    AssertionInfo info = someInfo();
    Date other = parseDate("2000-01-01");
    try {
      dates.assertIsInSameYearAs(info, actual, other);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeInSameYear(actual, other));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> dates.assertIsInSameYearAs(someInfo(), null, new Date()))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_throw_error_if_given_date_is_null() {
    assertThatNullPointerException().isThrownBy(() -> dates.assertIsInSameYearAs(someInfo(), actual, null))
                                                         .withMessage(dateToCompareActualWithIsNull());
  }

  @Test
  public void should_pass_if_actual_is_in_same_year_as_given_date() {
    dates.assertIsInSameYearAs(someInfo(), actual, parseDate("2011-05-11"));
  }

  @Test
  public void should_fail_if_actual_is_not_in_same_year_as_given_date_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();
    Date other = parseDate("2000-01-01");
    try {
      datesWithCustomComparisonStrategy.assertIsInSameYearAs(info, actual, other);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeInSameYear(actual, other));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> datesWithCustomComparisonStrategy.assertIsInSameYearAs(someInfo(), null, new Date()))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_throw_error_if_given_date_is_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> datesWithCustomComparisonStrategy.assertIsInSameYearAs(someInfo(),
                                                                                                             actual,
                                                                                                             null))
                                    .withMessage(dateToCompareActualWithIsNull());
  }

  @Test
  public void should_pass_if_actual_is_in_same_year_as_given_date_whatever_custom_comparison_strategy_is() {
    datesWithCustomComparisonStrategy.assertIsInSameYearAs(someInfo(), actual, parseDate("2011-05-11"));
  }

}
