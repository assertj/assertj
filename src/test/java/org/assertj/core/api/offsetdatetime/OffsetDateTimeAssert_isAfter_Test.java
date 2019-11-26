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
package org.assertj.core.api.offsetdatetime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.error.ShouldBeAfter.shouldBeAfter;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.OffsetDateTime;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author Paweł Stawicki
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
@DisplayName("OffsetDateTimeAssert")
public class OffsetDateTimeAssert_isAfter_Test extends OffsetDateTimeAssertBaseTest {

  @Test
  public void should_pass_if_actual_is_after_offsetDateTime_parameter() {
    assertThat(AFTER).isAfter(REFERENCE);
  }

  @Test
  public void should_pass_if_actual_is_after_offsetDateTime_as_string_parameter() {
    assertThat(AFTER).isAfter(REFERENCE.toString());
  }

  @Test
  public void should_pass_if_actual_is_after_offsetDateTime_parameter_with_different_offset() {
    assertThat(AFTER_WITH_DIFFERENT_OFFSET).isAfter(REFERENCE);
  }

  @Test
  public void should_fail_if_actual_is_equal_to_offsetDateTime_parameter() {
    // WHEN
    ThrowingCallable code = () -> assertThat(REFERENCE).isAfter(REFERENCE);
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(shouldBeAfter(REFERENCE, REFERENCE).create());
  }

  @Test
  public void should_fail_if_actual_is_equal_to_offsetDateTime_parameter_with_different_offset() {
    // WHEN
    ThrowingCallable code = () -> assertThat(REFERENCE_WITH_DIFFERENT_OFFSET).isAfter(REFERENCE);
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(shouldBeAfter(REFERENCE_WITH_DIFFERENT_OFFSET, REFERENCE).create());
  }

  @Test
  public void should_fail_if_actual_is_equal_to_offsetDateTime_as_string_parameter() {
    // WHEN
    ThrowingCallable code = () -> assertThat(REFERENCE).isAfter(REFERENCE.toString());
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(shouldBeAfter(REFERENCE, REFERENCE).create());
  }

  @Test
  public void should_fail_if_actual_is_before_offsetDateTime_parameter() {
    // WHEN
    ThrowingCallable code = () -> assertThat(BEFORE).isAfter(REFERENCE);
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(shouldBeAfter(BEFORE, REFERENCE).create());
  }

  @Test
  public void should_fail_if_actual_is_before_offsetDateTime_parameter_with_different_offset() {
    // WHEN
    ThrowingCallable code = () -> assertThat(BEFORE_WITH_DIFFERENT_OFFSET).isAfter(REFERENCE);
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(shouldBeAfter(BEFORE_WITH_DIFFERENT_OFFSET, REFERENCE).create());
  }

  @Test
  public void should_fail_if_actual_is_before_offsetDateTime_as_string_parameter() {
    // WHEN
    ThrowingCallable code = () -> assertThat(BEFORE).isAfter(REFERENCE.toString());
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(shouldBeAfter(BEFORE, REFERENCE).create());
  }

  @Test
  public void should_fail_if_actual_is_null() {
    // GIVEN
    OffsetDateTime offsetDateTime = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(offsetDateTime).isAfter(OffsetDateTime.now());
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_offsetDateTime_parameter_is_null() {
    // GIVEN
    OffsetDateTime otherOffsetDateTime = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(OffsetDateTime.now()).isAfter(otherOffsetDateTime);
    // THEN
    assertThatIllegalArgumentException().isThrownBy(code)
                                        .withMessage("The OffsetDateTime to compare actual with should not be null");
  }

  @Test
  public void should_fail_if_offsetDateTime_as_string_parameter_is_null() {
    // GIVEN
    String otherOffsetDateTimeAsString = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(OffsetDateTime.now()).isAfter(otherOffsetDateTimeAsString);
    // THEN
    assertThatIllegalArgumentException().isThrownBy(code)
                                        .withMessage("The String representing the OffsetDateTime to compare actual with should not be null");
  }

}
