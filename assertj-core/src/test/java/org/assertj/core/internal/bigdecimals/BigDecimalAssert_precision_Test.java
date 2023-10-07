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
package org.assertj.core.internal.bigdecimals;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.BigDecimalAssert;
import org.assertj.core.api.NavigationMethodBaseTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

class BigDecimalAssert_precision_Test implements NavigationMethodBaseTest<BigDecimalAssert> {

  @Test
  void should_fail_if_big_decimal_is_null() {
    // GIVEN
    BigDecimal bigDecimal = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(bigDecimal).precision());
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_big_decimal_does_not_have_expected_precision() {
    // GIVEN
    BigDecimal bigDecimal = BigDecimal.TEN;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(bigDecimal).precision().isEqualTo(123));
    // THEN
    then(assertionError).hasMessage(format("%nexpected: 123%n but was: 2"));
  }

  @Test
  void should_pass_if_big_decimal_has_expected_precision() {
    // GIVEN
    BigDecimal bigDecimal = BigDecimal.TEN;
    // THEN
    then(bigDecimal).precision()
                    .isEqualTo(2)
                    .returnToBigDecimal()
                    .hasPrecisionOf(2);
  }

  @Override
  public BigDecimalAssert getAssertion() {
    return new BigDecimalAssert(BigDecimal.TEN);
  }

  @Override
  public AbstractAssert<?, ?> invoke_navigation_method(BigDecimalAssert assertion) {
    return assertion.precision();
  }
}
