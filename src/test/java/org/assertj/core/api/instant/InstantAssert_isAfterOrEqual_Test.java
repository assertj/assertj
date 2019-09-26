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
package org.assertj.core.api.instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.error.ShouldBeAfterOrEqualTo.shouldBeAfterOrEqualTo;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.Instant;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("InstantAssert isAfterOrEqual")
public class InstantAssert_isAfterOrEqual_Test extends InstantAssertBaseTest {

  @Test
  public void should_pass_if_actual_is_after_date_parameter() {
    assertThat(AFTER).isAfterOrEqualTo(REFERENCE);
  }

  @Test
  public void should_pass_if_actual_is_after_date_as_string_parameter() {
    assertThat(AFTER).isAfterOrEqualTo(REFERENCE.toString());
  }

  @Test
  public void should_pass_if_actual_is_equal_to_date_parameter() {
    assertThat(REFERENCE).isAfterOrEqualTo(REFERENCE);
  }

  @Test
  public void should_pass_if_actual_is_equal_to_date_as_string_parameter() {
    assertThat(REFERENCE).isAfterOrEqualTo(REFERENCE.toString());
  }

  @Test
  public void should_fail_if_actual_is_before_date_parameter() {
    // WHEN
    ThrowingCallable code = () -> assertThat(BEFORE).isAfterOrEqualTo(REFERENCE);
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(shouldBeAfterOrEqualTo(BEFORE, REFERENCE).create());
  }

  @Test
  public void should_fail_if_actual_is_before_date_as_string_parameter() {
    // WHEN
    ThrowingCallable code = () -> assertThat(BEFORE).isAfterOrEqualTo(REFERENCE.toString());
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(shouldBeAfterOrEqualTo(BEFORE, REFERENCE).create());
  }

  @Test
  public void should_fail_if_actual_is_null() {
    // GIVEN
    Instant instant = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(instant).isAfterOrEqualTo(Instant.now());
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_date_parameter_is_null() {
    // GIVEN
    Instant otherInstant = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(Instant.now()).isAfterOrEqualTo(otherInstant);
    // THEN
    assertThatIllegalArgumentException().isThrownBy(code)
                                        .withMessage("The Instant to compare actual with should not be null");
  }

  @Test
  public void should_fail_if_date_as_string_parameter_is_null() {
    // GIVEN
    String otherInstantAsString = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(Instant.now()).isAfterOrEqualTo(otherInstantAsString);
    // THEN
    assertThatIllegalArgumentException().isThrownBy(code)
                                        .withMessage("The String representing the Instant to compare actual with should not be null");
  }

}
