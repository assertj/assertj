/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.api;

import static org.mockito.Answers.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;

import java.math.BigDecimal;

import org.assertj.core.api.AbstractBigDecimalAssert;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

class WithAssertions_assertThatBigDecimal_Test {

  static WithAssertions withAssertions = mock(CALLS_REAL_METHODS);

  @Test
  void should_accept_BigDecimal() {
    // GIVEN
    BigDecimal actual = BigDecimal.TEN;
    // WHEN
    AbstractBigDecimalAssert<?> result = withAssertions.assertThatBigDecimal(actual);
    // THEN
    result.isEqualTo(BigDecimal.TEN);
  }

  @Test
  void should_accept_bounded_generic_BigDecimal_subtype() {
    // WHEN
    AbstractBigDecimalAssert<?> result = withAssertions.assertThatBigDecimal(getBigDecimalSubtype());
    // THEN
    result.isNotNull();
  }

  @SuppressWarnings("unchecked")
  private static <T extends BigDecimal> T getBigDecimalSubtype() {
    return (T) BigDecimal.TEN;
  }

}
