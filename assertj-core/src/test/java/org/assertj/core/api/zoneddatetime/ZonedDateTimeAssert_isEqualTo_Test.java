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
package org.assertj.core.api.zoneddatetime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenIllegalArgumentException;
import static org.mockito.Mockito.verify;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.chrono.JapaneseChronology;
import java.time.chrono.JapaneseDate;
import java.time.format.DateTimeParseException;
import org.assertj.core.api.AbstractZonedDateTimeAssertBaseTest;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.api.ZonedDateTimeAssert;
import org.junit.jupiter.api.Test;

/**
 * Tests specific to {@link org.assertj.core.api.ZonedDateTimeAssert#isEqualTo(ZonedDateTime)} that can't be
 * done in {@link org.assertj.core.api.AbstractAssert#isEqualTo(Object)} tests.
 *
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
class ZonedDateTimeAssert_isEqualTo_Test extends AbstractZonedDateTimeAssertBaseTest {

  private Object otherType = new Object();

  @Override
  public ZonedDateTimeAssert invoke_api_method() {
    return assertions.isEqualTo(NOW)
                     .isEqualTo(YESTERDAY.toString())
                     .isEqualTo((ZonedDateTime) null)
                     .isEqualTo(otherType);
  }

  @Override
  protected void verify_internal_effects() {
    verify(comparables).assertEqual(getInfo(assertions), getActual(assertions), NOW);
    verify(comparables).assertEqual(getInfo(assertions), getActual(assertions), YESTERDAY);
    verify(objects).assertEqual(getInfo(assertions), getActual(assertions), null);
    verify(comparables).assertEqual(getInfo(assertions), getActual(assertions), otherType);
  }

  @Test
  void should_pass_if_both_are_null() {
    // GIVEN
    ZonedDateTime nullActual = null;
    ZonedDateTime nullExpected = null;
    // WHEN/THEN
    then(nullActual).isEqualTo(nullExpected);
  }

  @Test
  void should_fail_if_offsetDateTime_as_string_parameter_is_null() {
    // GIVEN
    String otherZonedDateTimeAsString = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(NOW).isEqualTo(otherZonedDateTimeAsString);
    // THEN
    thenIllegalArgumentException().isThrownBy(code)
                                  .withMessage("The String representing the ZonedDateTime to compare actual with should not be null");
  }

  @Test
  void should_fail_if_given_string_parameter_cant_be_parsed() {
    assertThatThrownBy(() -> assertions.isEqualTo("not a ZonedDateTime")).isInstanceOf(DateTimeParseException.class);
  }

  @Test
  void should_pass_if_actual_is_the_same_point_in_time_than_given_zonedDateTime_in_another_chronology() {
    // GIVEN
    ChronoZonedDateTime<JapaneseDate> nowInJapaneseChronology = JapaneseChronology.INSTANCE.zonedDateTime(NOW);
    // WHEN/THEN
    then(NOW).isEqualTo(nowInJapaneseChronology);
  }

  @Test
  void should_pass_if_given_zonedDateTime_passed_as_Object() {
    // GIVEN
    Object nowInJapaneseChronology = JapaneseChronology.INSTANCE.zonedDateTime(NOW);
    // WHEN/THEN
    assertThat(NOW).isEqualTo(nowInJapaneseChronology);
  }

  @Test
  void should_pass_if_actual_and_expected_correspond_to_the_same_instant_in_different_time_zones() {
    // GIVEN
    ZonedDateTime nowInParis = NOW.withZoneSameInstant(ZoneId.of("Europe/Paris"));
    ZonedDateTime nowInLA = NOW.withZoneSameInstant(ZoneId.of("America/Los_Angeles"));
    // WHEN/THEN
    assertThat(NOW).isEqualTo(nowInParis);
    assertThat(NOW).isEqualTo(nowInLA);
  }
}
