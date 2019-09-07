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

import static java.time.OffsetDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenIllegalArgumentException;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.Temporal;

import org.assertj.core.api.AbstractOffsetDateTimeAssertBaseTest;
import org.assertj.core.api.OffsetDateTimeAssert;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
@DisplayName("OffsetDateTimeAssert isEqualTo")
public class OffsetDateTimeAssert_isEqualTo_Test extends AbstractOffsetDateTimeAssertBaseTest {

  private Object otherType = new Object();

  @Override
  protected OffsetDateTimeAssert invoke_api_method() {
    return assertions.isEqualTo(REFERENCE)
                     .isEqualTo(BEFORE.toString())
                     .isEqualTo((OffsetDateTime) null)
                     .isEqualTo(otherType);
  }

  @Override
  protected void verify_internal_effects() {
    verify(comparables).assertEqual(getInfo(assertions), getActual(assertions), REFERENCE);
    verify(comparables).assertEqual(getInfo(assertions), getActual(assertions), BEFORE);
    verify(objects).assertEqual(getInfo(assertions), getActual(assertions), null);
    verify(comparables).assertEqual(getInfo(assertions), getActual(assertions), otherType);
  }

  @Test
  public void should_pass_if_actual_is_equal_to_offsetDateTime_with_different_offset() {
    assertThat(REFERENCE_WITH_DIFFERENT_OFFSET).isEqualTo(REFERENCE);
  }

  @Test
  public void should_pass_if_actual_is_equal_to_offsetDateTime_with_different_offset_as_a_Temporal() {
    // GIVEN
    Temporal reference = REFERENCE;
    // WHEN/THEN
    assertThat(REFERENCE_WITH_DIFFERENT_OFFSET).isEqualTo(reference);
  }

  @Test
  public void should_fail_if_actual_is_not_equal_to_offsetDateTime_with_different_offset() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(AFTER_WITH_DIFFERENT_OFFSET).isEqualTo(REFERENCE));
    // THEN
    then(assertionError).hasMessage("%nExpecting:%n <%s>%nto be equal to:%n <%s>%nwhen comparing values using 'OffsetDateTime.timeLineOrder()'%nbut was not.",
                                    AFTER_WITH_DIFFERENT_OFFSET, REFERENCE);
  }

  @Test
  public void should_fail_if_offsetDateTime_as_string_parameter_is_null() {
    // GIVEN
    String otherOffsetDateTimeAsString = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(now()).isEqualTo(otherOffsetDateTimeAsString);
    // THEN
    thenIllegalArgumentException().isThrownBy(code)
                                  .withMessage("The String representing the OffsetDateTime to compare actual with should not be null");
  }

  @Test
  public void should_pass_if_both_are_null() {
    // GIVEN
    OffsetDateTime nullActual = null;
    OffsetDateTime nullExpected = null;
    // WHEN/THEN
    then(nullActual).isEqualTo(nullExpected);
  }

  @Test
  public void should_fail_if_given_string_parameter_cant_be_parsed() {
    assertThatThrownBy(() -> assertions.isEqualTo("not an OffsetDateTime")).isInstanceOf(DateTimeParseException.class);
  }
}
