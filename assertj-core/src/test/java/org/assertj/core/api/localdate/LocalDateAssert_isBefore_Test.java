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
package org.assertj.core.api.localdate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.error.ShouldBeBefore.shouldBeBefore;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.LocalDate;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author Paweł Stawicki
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
@DisplayName("LocalDateAssert isBefore")
class LocalDateAssert_isBefore_Test extends LocalDateAssertBaseTest {

  @Test
  void should_pass_if_actual_is_before_date_parameter() {
    assertThat(BEFORE).isBefore(REFERENCE);
  }

  @Test
  void should_pass_if_actual_is_before_date_as_string_parameter() {
    assertThat(BEFORE).isBefore(REFERENCE.toString());
  }

  @Test
  void should_fail_if_actual_is_after_date_parameter() {
    // WHEN
    ThrowingCallable code = () -> assertThat(AFTER).isBefore(REFERENCE);
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(shouldBeBefore(AFTER, REFERENCE).create());
  }

  @Test
  void should_fail_if_actual_is_after_date_as_string_parameter() {
    // WHEN
    ThrowingCallable code = () -> assertThat(AFTER).isBefore(REFERENCE.toString());
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(shouldBeBefore(AFTER, REFERENCE).create());
  }

  @Test
  void should_fail_if_actual_is_equal_to_date_parameter() {
    // WHEN
    ThrowingCallable code = () -> assertThat(REFERENCE).isBefore(REFERENCE);
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(shouldBeBefore(REFERENCE, REFERENCE).create());
  }

  @Test
  void should_fail_if_actual_is_equal_to_date_as_string_parameter() {
    // WHEN
    ThrowingCallable code = () -> assertThat(REFERENCE).isBefore(REFERENCE.toString());
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(shouldBeBefore(REFERENCE, REFERENCE).create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    LocalDate localDate = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(localDate).isBefore(LocalDate.now());
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_date_parameter_is_null() {
    // GIVEN
    LocalDate otherLocalDate = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(LocalDate.now()).isBefore(otherLocalDate);
    // THEN
    assertThatIllegalArgumentException().isThrownBy(code)
                                        .withMessage("The LocalDate to compare actual with should not be null");
  }

  @Test
  void should_fail_if_date_as_string_parameter_is_null() {
    // GIVEN
    String otherLocalDateAsString = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(LocalDate.now()).isBefore(otherLocalDateAsString);
    // THEN
    assertThatIllegalArgumentException().isThrownBy(code)
                                        .withMessage("The String representing the LocalDate to compare actual with should not be null");
  }

}
