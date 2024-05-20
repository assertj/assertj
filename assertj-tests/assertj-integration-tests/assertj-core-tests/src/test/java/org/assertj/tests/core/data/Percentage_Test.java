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
package org.assertj.tests.core.data;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.internal.ErrorMessages.percentageValueIsInRange;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link Percentage}.
 *
 * @author Alexander Bischof
 */
class Percentage_Test {

  @Test
  void should_honor_equals_contract() {
    // WHEN/THEN
    EqualsVerifier.forClass(Percentage.class)
                  .verify();
  }

  @ParameterizedTest
  @ValueSource(doubles = { 0.0, 0.8, 200 })
  void withPercentage_should_succeed(double value) {
    // WHEN
    Percentage percentage = Percentage.withPercentage(value);
    // THEN
    then(percentage.value).isEqualTo(value);
  }

  @ParameterizedTest
  @ValueSource(doubles = { -0.8, -200 })
  void withPercentage_should_fail_if_value_is_negative(double value) {
    // WHEN
    Throwable thrown = catchThrowable(() -> Percentage.withPercentage(value));
    // THEN
    then(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(percentageValueIsInRange(value));
  }

  @ParameterizedTest
  @CsvSource({
      "0.0,   0%",
      "10,    10%",
      "10.0,  10%",
      "0.1,   0.1%",
      "0.103, 0.103%"
  })
  void toString_should_display_fractional_part_when_present(double value, String expected) {
    // GIVEN
    Percentage underTest = Percentage.withPercentage(value);
    // WHEN
    String result = underTest.toString();
    // THEN
    then(result).isEqualTo(expected);
  }

}
