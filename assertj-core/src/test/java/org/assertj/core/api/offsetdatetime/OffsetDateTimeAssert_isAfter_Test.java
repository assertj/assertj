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
package org.assertj.core.api.offsetdatetime;

import static java.time.OffsetDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenIllegalArgumentException;
import static org.assertj.core.error.ShouldBeAfter.shouldBeAfter;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import org.assertj.core.api.AbstractOffsetDateTimeAssertBaseTest;
import org.assertj.core.api.OffsetDateTimeAssert;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

/**
 * @author Paweł Stawicki
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
class OffsetDateTimeAssert_isAfter_Test extends AbstractOffsetDateTimeAssertBaseTest {

  @Override
  protected OffsetDateTimeAssert invoke_api_method() {
    return assertions.isAfter(REFERENCE)
                     .isAfter(BEFORE.toString());
  }

  @Override
  protected void verify_internal_effects() {
    verify(comparables).assertIsAfter(getInfo(assertions), getActual(assertions), REFERENCE);
    verify(comparables).assertIsAfter(getInfo(assertions), getActual(assertions), BEFORE);
  }

  @Test
  void should_pass_if_actual_is_after_offsetDateTime_parameter_with_different_offset() {
    assertThat(AFTER_WITH_DIFFERENT_OFFSET).isAfter(REFERENCE);
  }

  @Test
  void should_fail_if_actual_is_equal_to_offsetDateTime_parameter_with_different_offset() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(REFERENCE_WITH_DIFFERENT_OFFSET).isAfter(REFERENCE));
    // THEN
    then(assertionError).hasMessage(shouldBeAfter(REFERENCE_WITH_DIFFERENT_OFFSET, REFERENCE, COMPARISON_STRATEGY).create());
  }

  @Test
  void should_fail_if_actual_is_before_offsetDateTime_parameter_with_different_offset() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(BEFORE_WITH_DIFFERENT_OFFSET).isAfter(REFERENCE));
    // THEN
    then(assertionError).hasMessage(shouldBeAfter(BEFORE_WITH_DIFFERENT_OFFSET, REFERENCE, COMPARISON_STRATEGY).create());
  }

  @Test
  void should_fail_if_offsetDateTime_parameter_is_null() {
    // GIVEN
    OffsetDateTime otherOffsetDateTime = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(now()).isAfter(otherOffsetDateTime);
    // THEN
    thenIllegalArgumentException().isThrownBy(code)
                                  .withMessage("The OffsetDateTime to compare actual with should not be null");
  }

  @Test
  void should_fail_if_offsetDateTime_as_string_parameter_is_null() {
    // GIVEN
    String otherOffsetDateTimeAsString = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(now()).isAfter(otherOffsetDateTimeAsString);
    // THEN
    thenIllegalArgumentException().isThrownBy(code)
                                  .withMessage("The String representing the OffsetDateTime to compare actual with should not be null");
  }

  @Test
  void should_fail_if_given_string_parameter_cant_be_parsed() {
    assertThatThrownBy(() -> assertions.isAfter("not a OffsetDateTime")).isInstanceOf(DateTimeParseException.class);
  }
}
