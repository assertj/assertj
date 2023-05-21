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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.api.localdatetime;

import static java.time.Clock.systemUTC;
import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

/**
 * @author Nikolaos Georgiou
 */
class LocalDateTimeAssert_isCloseToUtcNow_Test extends LocalDateTimeAssertBaseTest {

  @Test
  void should_pass_when_executed_within_time_offset() {
    // GIVEN
    LocalDateTime now = now(systemUTC());
    // THEN
    assertThat(now).isCloseToUtcNow(within(1, SECONDS));
  }

  @Test
  void should_fail_when_executed_after_time_offset() {
    // GIVEN
    LocalDateTime now = now(systemUTC()).minusSeconds(2);
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(now).isCloseToUtcNow(within(1, SECONDS)));
    // THEN
    assertThat(error).hasMessageContaining("within 1 Seconds but difference was 2 Seconds");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    LocalDateTime actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).isCloseToUtcNow(within(1, SECONDS)));
    // THEN
    assertThat(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_offset_parameter_is_null() {
    assertThatNullPointerException().isThrownBy(() -> assertThat(now()).isCloseToUtcNow(null))
                                    .withMessage("The offset should not be null");
  }
}
