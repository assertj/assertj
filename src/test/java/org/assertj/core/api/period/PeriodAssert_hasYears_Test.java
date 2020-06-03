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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api.period;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Period;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldHavePeriod.shouldHaveYears;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.core.util.FailureMessages.actualIsNull;

/**
 * @author Hayden Meloche
 */
@DisplayName("PeriodAssert hasYears")
class PeriodAssert_hasYears_Test {

  @Test
  void should_pass_if_period_has_matching_years() {
    // GIVEN
    Period period = Period.ofYears(10);
    // WHEN
    assertThat(period).hasYears(10);
  }

  @Test
  void should_fail_when_period_is_null() {
    // GIVEN
    Period period = null;
    // WHEN
    ThrowableAssert.ThrowingCallable code = () -> assertThat(period).hasYears(5);
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_period_does_not_have_matching_years() {
    // GIVEN
    Period period = Period.ofYears(10);
    // WHEN
    ThrowableAssert.ThrowingCallable code = () -> assertThat(period).hasYears(15);
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(shouldHaveYears(period, 10, 15).create());
  }
}
