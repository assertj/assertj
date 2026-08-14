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

import java.math.BigInteger;

import org.assertj.core.api.AbstractBigIntegerAssert;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

class WithAssertions_assertThatBigInteger_Test {

  static WithAssertions withAssertions = mock(CALLS_REAL_METHODS);

  @Test
  void should_accept_BigInteger() {
    // GIVEN
    BigInteger actual = BigInteger.TEN;
    // WHEN
    AbstractBigIntegerAssert<?> result = withAssertions.assertThatBigInteger(actual);
    // THEN
    result.isEqualTo(BigInteger.TEN);
  }

  @Test
  void should_accept_bounded_generic_BigInteger_subtype() {
    // WHEN
    AbstractBigIntegerAssert<?> result = withAssertions.assertThatBigInteger(getBigIntegerSubtype());
    // THEN
    result.isNotNull();
  }

  @SuppressWarnings("unchecked")
  private static <T extends BigInteger> T getBigIntegerSubtype() {
    return (T) BigInteger.TEN;
  }

}
