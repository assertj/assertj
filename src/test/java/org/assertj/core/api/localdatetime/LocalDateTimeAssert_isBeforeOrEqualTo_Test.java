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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api.localdatetime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.error.ShouldBeBeforeOrEqualTo.shouldBeBeforeOrEqualTo;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.LocalDateTime;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author Paweł Stawicki
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
@DisplayName("LocalDateTimeAssert isBeforeOrEqualTo")
public class LocalDateTimeAssert_isBeforeOrEqualTo_Test extends LocalDateTimeAssertBaseTest {

  @Test
  public void should_pass_if_actual_is_before_dateTime_parameter() {
    assertThat(BEFORE).isBeforeOrEqualTo(REFERENCE);
  }

  @Test
  public void should_pass_if_actual_is_before_dateTime_as_string_parameter() {
    assertThat(BEFORE).isBeforeOrEqualTo(REFERENCE.toString());
  }

  @Test
  public void should_pass_if_actual_is_equal_to_dateTime_parameter() {
    assertThat(REFERENCE).isBeforeOrEqualTo(REFERENCE);
  }

  @Test
  public void should_pass_if_actual_is_equal_to_dateTime_as_string_parameter() {
    assertThat(REFERENCE).isBeforeOrEqualTo(REFERENCE.toString());
  }

  @Test
  public void should_fail_if_actual_is_after_dateTime_parameter() {
    // WHEN
    ThrowingCallable code = () -> assertThat(AFTER).isBeforeOrEqualTo(REFERENCE);
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(shouldBeBeforeOrEqualTo(AFTER, REFERENCE).create());
  }

  @Test
  public void should_fail_if_actual_is_after_dateTime_as_string_parameter() {
    // WHEN
    ThrowingCallable code = () -> assertThat(AFTER).isBeforeOrEqualTo(REFERENCE.toString());
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(shouldBeBeforeOrEqualTo(AFTER, REFERENCE).create());
  }

  @Test
  public void should_fail_if_actual_is_null() {
    // GIVEN
    LocalDateTime dateTime = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(dateTime).isBeforeOrEqualTo(LocalDateTime.now());
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_dateTime_parameter_is_null() {
    // GIVEN
    LocalDateTime otherDateTime = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(LocalDateTime.now()).isBeforeOrEqualTo(otherDateTime);
    // THEN
    assertThatIllegalArgumentException().isThrownBy(code)
                                        .withMessage("The LocalDateTime to compare actual with should not be null");
  }

  @Test
  public void should_fail_if_dateTime_as_string_parameter_is_null() {
    // GIVEN
    String otherDateTimeAsString = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(LocalDateTime.now()).isBeforeOrEqualTo(otherDateTimeAsString);
    // THEN
    assertThatIllegalArgumentException().isThrownBy(code)
                                        .withMessage("The String representing the LocalDateTime to compare actual with should not be null");
  }

}
