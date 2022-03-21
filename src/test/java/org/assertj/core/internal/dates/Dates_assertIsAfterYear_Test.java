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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.internal.dates;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldBeAfterYear.shouldBeAfterYear;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.util.Date;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Dates;
import org.assertj.core.internal.DatesBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Dates#assertIsAfterYear(AssertionInfo, Date, int)}</code>.
 * 
 * @author Joel Costigliola
 */
class Dates_assertIsAfterYear_Test extends DatesBaseTest {

  @Test
  void should_fail_if_actual_is_not_strictly_after_given_year() {
    AssertionInfo info = someInfo();
    int year = 2020;

    Throwable error = catchThrowable(() -> dates.assertIsAfterYear(info, actual, year));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeAfterYear(actual, year));
  }

  @Test
  void should_fail_if_actual_year_is_equals_to_given_year() {
    AssertionInfo info = someInfo();
    parseDate("2011-01-01");
    int year = 2011;

    Throwable error = catchThrowable(() -> dates.assertIsAfterYear(info, actual, year));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeAfterYear(actual, year));
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> dates.assertIsAfterYear(someInfo(), null, 2010))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_is_strictly_after_given_year() {
    dates.assertIsAfterYear(someInfo(), actual, 2010);
  }

  @Test
  void should_fail_if_actual_is_not_strictly_after_given_year_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();
    int year = 2020;

    Throwable error = catchThrowable(() -> datesWithCustomComparisonStrategy.assertIsAfterYear(info, actual, year));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeAfterYear(actual, year));
  }

  @Test
  void should_fail_if_actual_year_is_equals_to_given_year_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();
    parseDate("2011-01-01");
    int year = 2011;

    Throwable error = catchThrowable(() -> datesWithCustomComparisonStrategy.assertIsAfterYear(info, actual, year));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeAfterYear(actual, year));
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> datesWithCustomComparisonStrategy.assertIsAfterYear(someInfo(),
                                                                                                                         null,
                                                                                                                         2010))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_is_strictly_after_given_year_whatever_custom_comparison_strategy_is() {
    datesWithCustomComparisonStrategy.assertIsAfterYear(someInfo(), actual, 2000);
  }

}
