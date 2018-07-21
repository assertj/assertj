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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api.offsetdatetime;

import static java.lang.String.format;
import static java.time.OffsetDateTime.of;
import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.AbstractOffsetDateTimeAssert.NULL_OFFSET_DATE_TIME_PARAMETER_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.OffsetDateTime;

import org.assertj.core.api.BaseTest;
import org.junit.jupiter.api.Test;

public class OffsetDateTimeAssert_isEqualToIgnoringMinutes_Test extends BaseTest {

  private final OffsetDateTime refOffsetDateTime = of(2000, 1, 1, 23, 0, 0, 0, UTC);

  @Test
  public void should_pass_if_actual_is_equal_to_other_ignoring_minute_fields() {
    assertThat(refOffsetDateTime).isEqualToIgnoringMinutes(refOffsetDateTime.plusMinutes(1));
  }

  @Test
  public void should_fail_if_actual_is_not_equal_to_given_offsetdatetime_with_minute_ignored() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(refOffsetDateTime).isEqualToIgnoringMinutes(refOffsetDateTime.minusMinutes(1)))
                                                   .withMessage(format("%n" +
                                                                       "Expecting:%n" +
                                                                       "  <2000-01-01T23:00Z>%n" +
                                                                       "to have same year, month, day and hour as:%n" +
                                                                       "  <2000-01-01T22:59Z>%nbut had not."));
  }

  @Test
  public void should_fail_as_minutes_fields_are_different_even_if_time_difference_is_less_than_a_minute() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(refOffsetDateTime).isEqualToIgnoringMinutes(refOffsetDateTime.minusNanos(1)))
                                                   .withMessage(format("%n" +
                                                                       "Expecting:%n" +
                                                                       "  <2000-01-01T23:00Z>%n" +
                                                                       "to have same year, month, day and hour as:%n" +
                                                                       "  <2000-01-01T22:59:59.999999999Z>%nbut had not."));
  }

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
      OffsetDateTime actual = null;
      assertThat(actual).isEqualToIgnoringMinutes(OffsetDateTime.now());
    }).withMessage(actualIsNull());
  }

  @Test
  public void should_throw_error_if_given_offsetdatetime_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(refOffsetDateTime).isEqualToIgnoringMinutes(null))
                                        .withMessage(NULL_OFFSET_DATE_TIME_PARAMETER_MESSAGE);
  }

}
