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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.api.bigdecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHavePrecision.shouldHavePrecision;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

class BigDecimalsAssert_assertHasPrecisionOf_Test {

  @ParameterizedTest
  @ValueSource(strings = { "0", "-1.01", "1234", "999.00000009" })
  void should_pass_when_actual_has_expected_precision(String value) {
    BigDecimal actual = new BigDecimal(value);

    assertThat(actual).hasPrecisionOf(actual.precision());
  }

  @ParameterizedTest
  @ValueSource(strings = { "1", "0.1234", "123456" })
  void should_fail_when_actual_does_not_have_expected_precision(String value) {
    // GIVEN
    BigDecimal actual = new BigDecimal(value);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).hasPrecisionOf(123));
    // THEN
    then(assertionError).hasMessage(shouldHavePrecision(actual, 123).create());
  }

  @Test
  void should_fail_when_actual_is_null() {
    // GIVEN
    BigDecimal actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).hasPrecisionOf(1));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }
}
