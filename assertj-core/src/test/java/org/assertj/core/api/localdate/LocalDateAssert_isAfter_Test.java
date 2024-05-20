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
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.error.ShouldBeAfter.shouldBeAfter;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.LocalDate;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class LocalDateAssert_isAfter_Test extends LocalDateAssertBaseTest {

  @Test
  void should_pass_if_actual_is_after_date_parameter() {
    assertThat(AFTER).isAfter(REFERENCE);
  }

  @Test
  void should_pass_if_actual_is_after_date_as_string_parameter() {
    assertThat(AFTER).isAfter(REFERENCE.toString());
  }

  @Test
  void should_fail_if_actual_is_equal_to_date_parameter() {
    // WHEN
    ThrowingCallable code = () -> assertThat(REFERENCE).isAfter(REFERENCE);
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(shouldBeAfter(REFERENCE, REFERENCE).create());
  }

  @Test
  void should_fail_if_actual_is_equal_to_date_as_string_parameter() {
    // WHEN
    ThrowingCallable code = () -> assertThat(REFERENCE).isAfter(REFERENCE.toString());
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(shouldBeAfter(REFERENCE, REFERENCE).create());
  }

  @Test
  void should_fail_if_actual_is_before_date_parameter() {
    // WHEN
    ThrowingCallable code = () -> assertThat(BEFORE).isAfter(REFERENCE);
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(shouldBeAfter(BEFORE, REFERENCE).create());
  }

  @Test
  void should_fail_if_actual_is_before_date_as_string_parameter() {
    // WHEN
    ThrowingCallable code = () -> assertThat(BEFORE).isAfter(REFERENCE.toString());
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(shouldBeAfter(BEFORE, REFERENCE).create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    LocalDate localDate = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(localDate).isAfter(LocalDate.now());
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_date_parameter_is_null() {
    // GIVEN
    LocalDate otherLocalDate = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(LocalDate.now()).isAfter(otherLocalDate);
    // THEN
    assertThatIllegalArgumentException().isThrownBy(code)
                                        .withMessage("The LocalDate to compare actual with should not be null");
  }

  @Test
  void should_fail_if_date_as_string_parameter_is_null() {
    // GIVEN
    String otherLocalDateAsString = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(LocalDate.now()).isAfter(otherLocalDateAsString);
    // THEN
    assertThatIllegalArgumentException().isThrownBy(code)
                                        .withMessage("The String representing the LocalDate to compare actual with should not be null");
  }

}
