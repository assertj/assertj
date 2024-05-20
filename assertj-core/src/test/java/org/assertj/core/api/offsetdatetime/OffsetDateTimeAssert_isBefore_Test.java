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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenIllegalArgumentException;
import static org.assertj.core.error.ShouldBeBefore.shouldBeBefore;
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
class OffsetDateTimeAssert_isBefore_Test extends AbstractOffsetDateTimeAssertBaseTest {

  @Override
  protected OffsetDateTimeAssert invoke_api_method() {
    return assertions.isBefore(REFERENCE)
                     .isBefore(AFTER.toString());
  }

  @Override
  protected void verify_internal_effects() {
    verify(comparables).assertIsBefore(getInfo(assertions), getActual(assertions), REFERENCE);
    verify(comparables).assertIsBefore(getInfo(assertions), getActual(assertions), AFTER);
  }

  @Test
  void should_pass_if_actual_is_before_offsetDateTime_parameter_with_different_offset() {
    assertThat(BEFORE_WITH_DIFFERENT_OFFSET).isBefore(REFERENCE);
  }

  @Test
  void should_fail_if_actual_is_after_offsetDateTime_parameter_with_different_offset() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(AFTER_WITH_DIFFERENT_OFFSET).isBefore(REFERENCE));
    // THEN
    then(assertionError).hasMessage(shouldBeBefore(AFTER_WITH_DIFFERENT_OFFSET, REFERENCE, COMPARISON_STRATEGY).create());
  }

  @Test
  void should_fail_if_actual_is_equal_to_offsetDateTime_parameter_with_different_offset() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(REFERENCE_WITH_DIFFERENT_OFFSET).isBefore(REFERENCE));
    // THEN
    then(assertionError).hasMessage(shouldBeBefore(REFERENCE_WITH_DIFFERENT_OFFSET, REFERENCE, COMPARISON_STRATEGY).create());
  }

  @Test
  void should_fail_if_offsetDateTime_parameter_is_null() {
    // GIVEN
    OffsetDateTime otherOffsetDateTime = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(OffsetDateTime.now()).isBefore(otherOffsetDateTime);
    // THEN
    thenIllegalArgumentException().isThrownBy(code)
                                  .withMessage("The OffsetDateTime to compare actual with should not be null");
  }

  @Test
  void should_fail_if_offsetDateTime_as_string_parameter_is_null() {
    // GIVEN
    String otherOffsetDateTimeAsString = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(OffsetDateTime.now()).isBefore(otherOffsetDateTimeAsString);
    // THEN
    thenIllegalArgumentException().isThrownBy(code)
                                  .withMessage("The String representing the OffsetDateTime to compare actual with should not be null");
  }

  @Test
  void should_fail_if_given_string_parameter_cant_be_parsed() {
    assertThatThrownBy(() -> assertions.isBefore("not an OffsetDateTime")).isInstanceOf(DateTimeParseException.class);
  }
}
