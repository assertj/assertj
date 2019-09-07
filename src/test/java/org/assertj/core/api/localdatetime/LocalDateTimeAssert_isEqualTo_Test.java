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
import org.junit.jupiter.api.Test;

/**
 * Only test String based assertion (tests with {@link java.time.LocalDateTime} are already defined in assertj-core)
 *
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
public class LocalDateTimeAssert_isEqualTo_Test extends AbstractLocalDateTimeAssertBaseTest {

  private Object otherType;

  @Override
  public LocalDateTimeAssert invoke_api_method() {
    otherType = new Object();
    return assertions.isEqualTo(now)
                     .isEqualTo(yesterday.toString())
                     .isEqualTo((LocalDateTime) null)
                     .isEqualTo(otherType);
  }

  @Override
  protected void verify_internal_effects() {
    verify(comparables).assertEqual(getInfo(assertions), getActual(assertions), now);
    verify(comparables).assertEqual(getInfo(assertions), getActual(assertions), yesterday);
    verify(objects).assertEqual(getInfo(assertions), getActual(assertions), null);
    verify(comparables).assertEqual(getInfo(assertions), getActual(assertions), otherType);
  }

  @Test
  public void should_fail_if_given_string_parameter_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertions.isEqualTo((String) null))
                                        .withMessage("The String representing the LocalDateTime to compare actual with should not be null");
  }

  @Test
  public void should_fail_if_given_string_parameter_cant_be_parsed() {
    assertThatThrownBy(() -> assertions.isEqualTo("not a LocalDateTime")).isInstanceOf(DateTimeParseException.class);
  }

  @Test
  public void should_fail_if_given_would_be_equal_to_actual_with_default_comparator() {
    // GIVEN
    ChronoLocalDateTime<JapaneseDate> inJapan = JapaneseChronology.INSTANCE.localDateTime(now);
    // THEN
    assertThat(now.equals(inJapan)).isFalse();
    assertThat(now.isEqual(inJapan)).isTrue();
    assertThat(now).isEqualTo(inJapan);
    Object inJapanObject = inJapan;
    assertThat(now).isEqualTo(inJapanObject);
  }
}
