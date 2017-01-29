/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api.offsetdatetime;

import static java.time.OffsetDateTime.of;
import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.AbstractOffsetDateTimeAssert.NULL_OFFSET_DATE_TIME_PARAMETER_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.OffsetDateTime;

import org.assertj.core.api.BaseTest;
import org.junit.Test;

public class OffsetDateTimeAssert_isEqualToIgnoringNanoseconds_Test extends BaseTest {

  private final OffsetDateTime refOffsetDateTime = of(2000, 1, 1, 0, 0, 1, 0, UTC);

  @Test
  public void should_pass_if_actual_is_equal_to_other_ignoring_nanosecond_fields() {
    assertThat(refOffsetDateTime).isEqualToIgnoringNanos(refOffsetDateTime.withNano(55));
    assertThat(refOffsetDateTime).isEqualToIgnoringNanos(refOffsetDateTime.plusNanos(1));
  }

  @Test
  public void should_fail_if_actual_is_not_equal_to_given_offsetdatetime_with_nanoseconds_ignored() {
    thrown.expectAssertionError("%nExpecting:%n" +
                                "  <2000-01-01T00:00:01Z>%n" +
                                "to have same year, month, day, hour, minute and second as:%n" +
                                "  <2000-01-01T00:00:02Z>%nb" +
                                "ut had not.");
    assertThat(refOffsetDateTime).isEqualToIgnoringNanos(refOffsetDateTime.plusSeconds(1));
  }

  @Test
  public void should_fail_as_seconds_fields_are_different() {
    thrown.expectAssertionError("%n" +
                                "Expecting:%n" +
                                "  <2000-01-01T00:00:01Z>%n" +
                                "to have same year, month, day, hour, minute and second as:%n" +
                                "  <2000-01-01T00:00:00.999999999Z>%n" +
                                "but had not.");
    assertThat(refOffsetDateTime).isEqualToIgnoringNanos(refOffsetDateTime.minusNanos(1));
  }

  @Test
  public void should_fail_if_actual_is_null() {
    expectException(AssertionError.class, actualIsNull());
    OffsetDateTime actual = null;
    assertThat(actual).isEqualToIgnoringNanos(OffsetDateTime.now());
  }

  @Test
  public void should_throw_error_if_given_offsetdatetime_is_null() {
    expectIllegalArgumentException(NULL_OFFSET_DATE_TIME_PARAMETER_MESSAGE);
    assertThat(refOffsetDateTime).isEqualToIgnoringNanos(null);
  }

}
