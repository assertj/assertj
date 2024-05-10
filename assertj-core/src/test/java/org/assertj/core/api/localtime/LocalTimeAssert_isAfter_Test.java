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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api.localtime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.error.ShouldBeAfter.shouldBeAfter;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.LocalTime;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("LocalTimeAssert isAfter")
class LocalTimeAssert_isAfter_Test extends LocalTimeAssertBaseTest {

  @Test
  void should_pass_if_actual_is_after_localTime_parameter() {
    assertThat(AFTER).isAfter(REFERENCE);
  }

  @Test
  void should_pass_if_actual_is_after_localTime_parameter_as_string() {
    assertThat(AFTER).isAfter(REFERENCE.toString());
  }

  @Test
  void should_fail_if_actual_is_equal_to_localTime_parameter() {
    // WHEN
    ThrowingCallable code = () -> assertThat(REFERENCE).isAfter(REFERENCE);
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(shouldBeAfter(REFERENCE, REFERENCE).create());
  }

  @Test
  void should_fail_if_actual_is_equal_to_localTime_as_string_parameter() {
    // WHEN
    ThrowingCallable code = () -> assertThat(REFERENCE).isAfter(REFERENCE.toString());
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(shouldBeAfter(REFERENCE, REFERENCE).create());
  }

  @Test
  void should_fail_if_actual_is_before_localTime_parameter() {
    // WHEN
    ThrowingCallable code = () -> assertThat(BEFORE).isAfter(REFERENCE);
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(shouldBeAfter(BEFORE, REFERENCE).create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    LocalTime actual = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(actual).isAfter(LocalTime.now());
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_localTime_parameter_is_null() {
    // GIVEN
    LocalTime otherLocalTime = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(LocalTime.now()).isAfter(otherLocalTime);
    // THEN
    assertThatIllegalArgumentException().isThrownBy(code)
                                        .withMessage("The LocalTime to compare actual with should not be null");
  }

  @Test
  void should_fail_if_localTime_as_string_parameter_is_null() {
    // GIVEN
    String otherLocalTimeAsString = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(LocalTime.now()).isAfter(otherLocalTimeAsString);
    // THEN
    assertThatIllegalArgumentException().isThrownBy(code)
                                        .withMessage("The String representing the LocalTime to compare actual with should not be null");
  }

}
