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
package org.assertj.core.api.optionaldouble;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.OptionalShouldContain.shouldContain;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.OptionalDouble;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.opentest4j.AssertionFailedError;

class OptionalDoubleAssert_hasValue_Test {

  @ParameterizedTest
  @ValueSource(doubles = { 10.0, -10.0, 0.0, -0.0, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY })
  void should_pass_if_optionalDouble_has_the_expected_value(double value) {
    assertThat(OptionalDouble.of(value)).hasValue(value);
  }

  @Test
  void should_pass_if_optionalDouble_has_the_expected_value_as_Double() {
    assertThat(OptionalDouble.of(10.0)).hasValue(Double.valueOf(10.0));
  }

  @Test
  void should_fail_when_optionalDouble_is_null() {
    // GIVEN
    OptionalDouble nullActual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(nullActual).hasValue(10.0));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_optionalDouble_does_not_have_the_expected_value() {
    // GIVEN
    OptionalDouble actual = OptionalDouble.of(5.0);
    double expectedValue = 10.0;
    // WHEN
    AssertionFailedError error = catchThrowableOfType(() -> assertThat(actual).hasValue(expectedValue),
                                                      AssertionFailedError.class);
    // THEN
    then(error).hasMessage(shouldContain(actual, expectedValue).create());
    then(error.getActual().getStringRepresentation()).isEqualTo(String.valueOf(actual.getAsDouble()));
    then(error.getExpected().getStringRepresentation()).isEqualTo(String.valueOf(expectedValue));
  }

  @Test
  void should_fail_if_optionalDouble_is_empty() {
    // GIVEN
    double expectedValue = 10.0;
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(OptionalDouble.empty()).hasValue(expectedValue));
    // THEN
    then(error).hasMessage(shouldContain(expectedValue).create());
  }
}
