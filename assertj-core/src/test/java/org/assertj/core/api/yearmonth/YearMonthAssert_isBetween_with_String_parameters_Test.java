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
package org.assertj.core.api.yearmonth;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.verify;

import java.time.YearMonth;
import java.time.format.DateTimeParseException;

import org.assertj.core.api.YearMonthAssert;
import org.assertj.core.api.YearMonthAssertBaseTest;
import org.junit.jupiter.api.Test;

class YearMonthAssert_isBetween_with_String_parameters_Test extends YearMonthAssertBaseTest {

  private YearMonth before = now.minusMonths(1);
  private YearMonth after = now.plusMonths(1);

  @Override
  protected YearMonthAssert invoke_api_method() {
    return assertions.isBetween(before.toString(), after.toString());
  }

  @Override
  protected void verify_internal_effects() {
    verify(comparables).assertIsBetween(getInfo(assertions), getActual(assertions), before, after, true, true);
  }

  @Test
  void should_throw_a_DateTimeParseException_if_start_String_parameter_cannot_be_converted() {
    // GIVEN
    String abc = "abc";
    // WHEN
    Throwable thrown = catchThrowable(() -> assertions.isBetween(abc, after.toString()));
    // THEN
    then(thrown).isInstanceOf(DateTimeParseException.class);
  }

  @Test
  void should_throw_a_DateTimeParseException_if_end_String_parameter_cannot_be_converted() {
    // GIVEN
    String abc = "abc";
    // WHEN
    Throwable thrown = catchThrowable(() -> assertions.isBetween(before.toString(), abc));
    // THEN
    then(thrown).isInstanceOf(DateTimeParseException.class);
  }

}
