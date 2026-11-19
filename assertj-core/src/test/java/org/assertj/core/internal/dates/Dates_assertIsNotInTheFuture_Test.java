/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.internal.dates;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeNotInTheFuture.shouldBeNotInTheFuture;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.util.Date;

import org.assertj.core.internal.DatesBaseTest;
import org.junit.jupiter.api.Test;

class Dates_assertIsNotInTheFuture_Test extends DatesBaseTest {

  @Test
  void should_pass_if_actual_is_in_the_past() {
    // GIVEN
    actual = parseDate("2000-01-01");
    // WHEN/THEN
    dates.assertIsNotInTheFuture(someInfo(), actual);
  }

  @Test
  void should_pass_if_actual_is_now() {
    // GIVEN
    actual = new Date();
    // WHEN/THEN
    dates.assertIsNotInTheFuture(someInfo(), actual);
  }

  @Test
  void should_fail_if_actual_is_in_the_future() {
    // GIVEN
    var info = someInfo();
    actual = parseDate("2111-01-01");
    // WHEN
    var error = catchThrowable(() -> dates.assertIsNotInTheFuture(info, actual));
    // THEN
    then(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeNotInTheFuture(actual));
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> dates.assertIsNotInTheFuture(someInfo(), null))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_is_now_according_to_custom_comparison_strategy() {
    // GIVEN the custom comparison strategy compares only year and month
    actual = new Date();
    // WHEN/THEN
    datesWithCustomComparisonStrategy.assertIsNotInTheFuture(someInfo(), actual);
  }

  @Test
  void should_fail_if_actual_is_in_the_future_according_to_custom_comparison_strategy() {
    // GIVEN
    var info = someInfo();
    actual = parseDate("2111-01-01");
    // WHEN
    var error = catchThrowable(() -> datesWithCustomComparisonStrategy.assertIsNotInTheFuture(info, actual));
    // THEN
    then(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeNotInTheFuture(actual, yearAndMonthComparisonStrategy));
  }

}
