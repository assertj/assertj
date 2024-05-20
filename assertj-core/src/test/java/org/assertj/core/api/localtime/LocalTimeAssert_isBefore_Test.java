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
package org.assertj.core.api.localtime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.error.ShouldBeBefore.shouldBeBefore;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.LocalTime;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author Paweł Stawicki
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
@DisplayName("LocalTimeAssert isBefore")
class LocalTimeAssert_isBefore_Test extends LocalTimeAssertBaseTest {

  @Test
  void should_pass_if_actual_is_before_localTime_parameter() {
    assertThat(BEFORE).isBefore(REFERENCE);
  }

  @Test
  void should_pass_if_actual_is_before_localTime_as_string_parameter() {
    assertThat(BEFORE).isBefore(REFERENCE.toString());
  }

  @Test
  void should_fail_if_actual_is_after_localTime_parameter() {
    // WHEN
    ThrowingCallable code = () -> assertThat(AFTER).isBefore(REFERENCE);
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(shouldBeBefore(AFTER, REFERENCE).create());
  }

  @Test
  void should_fail_if_actual_is_after_localTime_as_string_parameter() {
    // WHEN
    ThrowingCallable code = () -> assertThat(AFTER).isBefore(REFERENCE.toString());
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(shouldBeBefore(AFTER, REFERENCE).create());
  }

  @Test
  void should_fail_if_actual_is_equal_to_localTime_parameter() {
    // WHEN
    ThrowingCallable code = () -> assertThat(REFERENCE).isBefore(REFERENCE);
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(shouldBeBefore(REFERENCE, REFERENCE).create());
  }

  @Test
  void should_fail_if_actual_is_equal_to_localTime_as_string_parameter() {
    // WHEN
    ThrowingCallable code = () -> assertThat(REFERENCE).isBefore(REFERENCE.toString());
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(shouldBeBefore(REFERENCE, REFERENCE).create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    LocalTime actual = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(actual).isBefore(LocalTime.now());
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_localTime_parameter_is_null() {
    // GIVEN
    LocalTime otherLocalTime = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(LocalTime.now()).isBefore(otherLocalTime);
    // THEN
    assertThatIllegalArgumentException().isThrownBy(code)
                                        .withMessage("The LocalTime to compare actual with should not be null");
  }

  @Test
  void should_fail_if_localTime_as_string_parameter_is_null() {
    // GIVEN
    String otherLocalTimeAsString = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(LocalTime.now()).isBefore(otherLocalTimeAsString);
    // THEN
    assertThatIllegalArgumentException().isThrownBy(code)
                                        .withMessage("The String representing the LocalTime to compare actual with should not be null");
  }

}
