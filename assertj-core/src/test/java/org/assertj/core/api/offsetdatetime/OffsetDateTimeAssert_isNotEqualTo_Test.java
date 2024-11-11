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
package org.assertj.core.api.offsetdatetime;

import static java.time.OffsetDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenIllegalArgumentException;
import static org.assertj.core.error.ShouldNotBeEqual.shouldNotBeEqual;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

import org.assertj.core.api.AbstractOffsetDateTimeAssertBaseTest;
import org.assertj.core.api.OffsetDateTimeAssert;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

/**
 * Only test String based assertion (tests with {@link OffsetDateTime} are already defined in assertj-core)
 *
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
class OffsetDateTimeAssert_isNotEqualTo_Test extends AbstractOffsetDateTimeAssertBaseTest {

  private final Object otherType = new Object();

  @Override
  protected OffsetDateTimeAssert invoke_api_method() {
    return assertions.isNotEqualTo(REFERENCE)
                     .isNotEqualTo(BEFORE.toString())
                     .isNotEqualTo((OffsetDateTime) null)
                     .isNotEqualTo(otherType);
  }

  @Override
  protected void verify_internal_effects() {
    verify(comparables).assertNotEqual(getInfo(assertions), getActual(assertions), REFERENCE);
    verify(comparables).assertNotEqual(getInfo(assertions), getActual(assertions), BEFORE);
    verify(objects).assertNotEqual(getInfo(assertions), getActual(assertions), null);
    verify(comparables).assertNotEqual(getInfo(assertions), getActual(assertions), otherType);
  }

  @Test
  void should_fail_if_actual_is_at_same_instant_as_offsetDateTime_with_different_offset() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(REFERENCE).isNotEqualTo(REFERENCE_WITH_DIFFERENT_OFFSET));
    // THEN
    String errorMesssage = shouldNotBeEqual(REFERENCE, REFERENCE_WITH_DIFFERENT_OFFSET, COMPARISON_STRATEGY).create();
    assertThat(assertionError).hasMessage(errorMesssage);
  }

  @Test
  void should_fail_if_both_are_null() {
    // GIVEN
    OffsetDateTime nullActual = null;
    OffsetDateTime nullExpected = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(nullActual).isNotEqualTo(nullExpected));
    // THEN
    then(assertionError).hasMessage(shouldNotBeEqual(nullActual, nullExpected).create());
  }

  @Test
  void should_fail_if_offsetDateTime_as_string_parameter_is_null() {
    // GIVEN
    String otherOffsetDateTimeAsString = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(now()).isNotEqualTo(otherOffsetDateTimeAsString);
    // THEN
    thenIllegalArgumentException().isThrownBy(code)
                                  .withMessage("The String representing the OffsetDateTime to compare actual with should not be null");
  }

  @Test
  void should_fail_if_given_string_parameter_cant_be_parsed() {
    assertThatThrownBy(() -> assertions.isNotEqualTo("not an OffsetDateTime")).isInstanceOf(DateTimeParseException.class);
  }

}
