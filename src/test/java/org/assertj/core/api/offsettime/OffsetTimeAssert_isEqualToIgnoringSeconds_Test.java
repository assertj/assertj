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
 * Copyright 2012-2014 the original author or authors.
 */
package org.assertj.core.api.offsettime;

import org.assertj.core.api.BaseTest;
import org.junit.Test;

import java.time.OffsetTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.AbstractOffsetTimeAssert.NULL_OFFSET_TIME_PARAMETER_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.FailureMessages.actualIsNull;

public class OffsetTimeAssert_isEqualToIgnoringSeconds_Test extends BaseTest {

  private final OffsetTime refOffsetTime = OffsetTime.of(23, 51, 0, 0, ZoneOffset.UTC);

  @Test
  public void should_pass_if_actual_is_equal_to_other_ignoring_second_fields() {
	assertThat(refOffsetTime).isEqualToIgnoringSeconds(refOffsetTime.plusSeconds(1));
  }

  @Test
  public void should_fail_if_actual_is_not_equal_to_given_offsettime_with_second_ignored() {
	try {
	  assertThat(refOffsetTime).isEqualToIgnoringSeconds(refOffsetTime.plusMinutes(1));
	} catch (AssertionError e) {
	  assertThat(e).hasMessage("\nExpecting:\n" +
		                       "  <23:51Z>\n" +
		                       "to have same hour and minute as:\n" +
		                       "  <23:52Z>\n" +
		                       "but had not.");
	  return;
	}
	failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_as_seconds_fields_are_different_even_if_time_difference_is_less_than_a_second() {
	try {
	  assertThat(refOffsetTime).isEqualToIgnoringSeconds(refOffsetTime.minusNanos(1));
	} catch (AssertionError e) {
	  assertThat(e).hasMessage("\nExpecting:\n" +
		                       "  <23:51Z>\n" +
		                       "to have same hour and minute as:\n" +
		                       "  <23:50:59.999999999Z>\n" +
		                       "but had not.");
	  return;
	}
	failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null() {
	expectException(AssertionError.class, actualIsNull());
	OffsetTime actual = null;
	assertThat(actual).isEqualToIgnoringSeconds(OffsetTime.now());
  }

  @Test
  public void should_throw_error_if_given_offsettime_is_null() {
	expectIllegalArgumentException(NULL_OFFSET_TIME_PARAMETER_MESSAGE);
	assertThat(refOffsetTime).isEqualToIgnoringSeconds(null);
  }

}
