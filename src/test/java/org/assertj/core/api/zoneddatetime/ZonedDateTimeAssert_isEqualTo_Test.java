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
package org.assertj.core.api.zoneddatetime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.chrono.JapaneseChronology;
import java.time.chrono.JapaneseDate;
import java.time.format.DateTimeParseException;

import org.assertj.core.api.AbstractZonedDateTimeAssertBaseTest;
import org.assertj.core.api.ZonedDateTimeAssert;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

/**
 * Tests specific to {@link org.assertj.core.api.ZonedDateTimeAssert#isEqualTo(ZonedDateTime)} that can't be
 * done in {@link org.assertj.core.api.AbstractAssert#isEqualTo(Object)} tests.
 *
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
public class ZonedDateTimeAssert_isEqualTo_Test extends AbstractZonedDateTimeAssertBaseTest {

  private Object otherType = new Object();

  @Override
  public ZonedDateTimeAssert invoke_api_method() {
    return assertions
      .isEqualTo(now)
      .isEqualTo(yesterday.toString())
      .isEqualTo((ZonedDateTime) null)
      .isEqualTo(otherType);
  }

  @Override
  protected void verify_internal_effects() {
    verify(comparables).assertEqual(getInfo(assertions), getActual(assertions), now);
    verify(comparables).assertEqual(getInfo(assertions), getActual(assertions), yesterday);
    verify(objects).assertEqual(getInfo(assertions), getActual(assertions), null);
    verify(objects).assertEqual(getInfo(assertions), getActual(assertions), otherType);
  }

  @Test
  public void should_pass_if_both_are_null() {
    assertThat((ZonedDateTime) null).isEqualTo((ZonedDateTime) null);
  }

  @Test
  public void should_fail_if_given_string_parameter_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(now).isEqualTo((String) null))
      .withMessage("The String representing the ZonedDateTime to compare actual with should not be null");
  }

  @Test
  public void should_fail_if_given_string_parameter_cant_be_parsed() {
    assertThatThrownBy(() -> assertions.isEqualTo("not a ZonedDateTime")).isInstanceOf(DateTimeParseException.class);
  }

  @Test
  public void should_fail_if_given_would_be_equal_to_actual_with_default_comparator() {
    ChronoZonedDateTime<JapaneseDate> inJapan = JapaneseChronology.INSTANCE.zonedDateTime(now);
    assertThatThrownBy(() -> assertThat(now).isEqualTo(inJapan)).isInstanceOf(AssertionFailedError.class);
  }
}
