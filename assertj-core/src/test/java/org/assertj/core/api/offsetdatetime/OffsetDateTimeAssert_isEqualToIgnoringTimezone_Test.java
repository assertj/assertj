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

import static java.lang.String.format;
import static java.time.OffsetDateTime.of;
import static java.time.ZoneOffset.MAX;
import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.AbstractOffsetDateTimeAssert.NULL_OFFSET_DATE_TIME_PARAMETER_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.OffsetDateTime;
import org.junit.jupiter.api.Test;

class OffsetDateTimeAssert_isEqualToIgnoringTimezone_Test {

  private final OffsetDateTime actual = OffsetDateTime.of(2000, 1, 5, 12, 0, 0, 0, MAX);

  @Test
  void should_pass_if_actual_is_equal_to_other_ignoring_timezone_fields() {
    // GIVEN
    OffsetDateTime offsetDateTime = of(2000, 1, 5, 12, 0, 0, 0, UTC);
    // THEN
    assertThat(actual).isEqualToIgnoringTimezone(offsetDateTime);
  }

  @Test
  void should_fail_if_actual_is_not_equal_to_given_OffsetDateTime_with_timezone_ignored() {
    // GIVEN
    OffsetDateTime offsetDateTime = OffsetDateTime.of(2000, 1, 5, 12, 1, 0, 0, UTC);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isEqualToIgnoringTimezone(offsetDateTime));
    // THEN
    then(assertionError).hasMessage(format("%nExpecting actual:%n" +
                                           "  2000-01-05T12:00+18:00 (java.time.OffsetDateTime)%n" +
                                           "to have same time fields except timezone as:%n" +
                                           "  2000-01-05T12:01Z (java.time.OffsetDateTime)%n" +
                                           "but had not."));
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    OffsetDateTime actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isEqualToIgnoringTimezone(OffsetDateTime.now()));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_throw_error_if_given_OffsetDateTimetime_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(actual).isEqualToIgnoringTimezone(null))
                                        .withMessage(NULL_OFFSET_DATE_TIME_PARAMETER_MESSAGE);
  }

}
