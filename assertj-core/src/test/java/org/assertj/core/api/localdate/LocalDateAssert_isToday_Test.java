/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api.localdate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldBeToday.shouldBeToday;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.LocalDate;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("LocalDateAssert isToday")
class LocalDateAssert_isToday_Test extends LocalDateAssertBaseTest {

  @Test
  void should_pass_if_actual_is_today() {
    assertThat(REFERENCE).isToday();
  }

  @Test
  void should_fail_if_actual_is_before_today() {
    // WHEN
    ThrowingCallable code = () -> assertThat(BEFORE).isToday();
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(shouldBeToday(BEFORE).create());
  }

  @Test
  void should_fail_if_actual_is_after_today() {
    // WHEN
    ThrowingCallable code = () -> assertThat(AFTER).isToday();
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(shouldBeToday(AFTER).create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    LocalDate actual = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(actual).isToday();
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(actualIsNull());
  }

}
