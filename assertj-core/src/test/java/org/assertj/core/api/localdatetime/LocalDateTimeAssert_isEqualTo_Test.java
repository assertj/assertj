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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.api.localdatetime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.JapaneseChronology;
import java.time.chrono.JapaneseDate;
import java.time.format.DateTimeParseException;

import org.assertj.core.api.AbstractLocalDateTimeAssertBaseTest;
import org.assertj.core.api.LocalDateTimeAssert;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

/**
 * Only test String based assertion (tests with {@link java.time.LocalDateTime} are already defined in assertj-core)
 *
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
class LocalDateTimeAssert_isEqualTo_Test extends AbstractLocalDateTimeAssertBaseTest {

  private final Object otherType = new Object();

  @Override
  public LocalDateTimeAssert invoke_api_method() {
    return assertions.isEqualTo(NOW)
                     .isEqualTo(YESTERDAY.toString())
                     .isEqualTo((LocalDateTime) null)
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
  void should_fail_if_localDateTime_as_string_parameter_is_null() {
    // GIVEN
    String otherDateTimeAsString = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(NOW).isEqualTo(otherDateTimeAsString);
    // THEN
    assertThatIllegalArgumentException().isThrownBy(code)
                                        .withMessage("The String representing the LocalDateTime to compare actual with should not be null");
  }

  @Test
  void should_fail_if_given_localDateTime_as_string_parameter_cant_be_parsed() {
    assertThatThrownBy(() -> assertions.isEqualTo("not a LocalDateTime")).isInstanceOf(DateTimeParseException.class);
  }

  @Test
  void should_pass_if_actual_is_the_same_point_on_the_local_time_than_given_localDateTime_in_another_chronology() {
    // GIVEN
    ChronoLocalDateTime<JapaneseDate> nowInJapaneseChronology = JapaneseChronology.INSTANCE.localDateTime(NOW);
    // WHEN/THEN
    // isEqualTo is consistent with LocalDateTime.isEqual ...
    assertThat(NOW.isEqual(nowInJapaneseChronology)).isTrue();
    assertThat(NOW).isEqualTo(nowInJapaneseChronology);
    // ... but not LocalDateTime.equals
    assertThat(NOW.equals(nowInJapaneseChronology)).isFalse();
  }

  @Test
  void should_pass_if_given_localDateTime_passed_as_Object() {
    // GIVEN
    Object nowInJapaneseChronology = JapaneseChronology.INSTANCE.localDateTime(NOW);
    // WHEN/THEN
    assertThat(NOW).isEqualTo(nowInJapaneseChronology);
  }
}
