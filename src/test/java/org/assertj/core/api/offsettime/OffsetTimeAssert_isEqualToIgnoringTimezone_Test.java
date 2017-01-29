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
package org.assertj.core.api.offsettime;

import static org.assertj.core.api.AbstractOffsetTimeAssert.NULL_OFFSET_TIME_PARAMETER_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.OffsetTime;
import java.time.ZoneOffset;

import org.assertj.core.api.BaseTest;
import org.junit.Test;

public class OffsetTimeAssert_isEqualToIgnoringTimezone_Test extends BaseTest {

  private final OffsetTime actual = OffsetTime.of(12, 0, 0, 0, ZoneOffset.MAX);

  @Test
  public void should_pass_if_actual_is_equal_to_other_ignoring_timezone_fields() {
    assertThat(actual).isEqualToIgnoringTimezone(OffsetTime.of(12, 0, 0, 0, ZoneOffset.UTC));
  }

  @Test
  public void should_fail_if_actual_is_not_equal_to_given_OffsetTime_with_timezone_ignored() {
    thrown.expectAssertionError("%nExpecting:%n  " +
                                "<12:00+18:00>%n" +
                                "to have same time fields except timezone as:%n" +
                                "  <12:01Z>%n" +
                                "but had not.");
    assertThat(actual).isEqualToIgnoringTimezone(OffsetTime.of(12, 1, 0, 0, ZoneOffset.UTC));
  }

  @Test
  public void should_fail_if_actual_is_null() {
    expectException(AssertionError.class, actualIsNull());
    OffsetTime actual = null;
    assertThat(actual).isEqualToIgnoringTimezone(OffsetTime.now());
  }

  @Test
  public void should_throw_error_if_given_offsetTime_is_null() {
    expectIllegalArgumentException(NULL_OFFSET_TIME_PARAMETER_MESSAGE);
    assertThat(actual).isEqualToIgnoringTimezone(null);
  }

}
