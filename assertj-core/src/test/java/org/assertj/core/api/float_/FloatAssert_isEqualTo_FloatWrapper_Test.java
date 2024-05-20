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
package org.assertj.core.api.float_;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import org.assertj.core.api.FloatAssert;
import org.assertj.core.api.FloatAssertBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FloatAssert_isEqualTo_FloatWrapper_Test extends FloatAssertBaseTest {

  @Override
  protected FloatAssert invoke_api_method() {
    return assertions.isEqualTo(Float.valueOf(getActual(assertions)));
  }

  @Override
  protected void verify_internal_effects() {
    verify(objects).assertEqual(getInfo(assertions), getActual(assertions), Float.valueOf(getActual(assertions)));
    verifyNoInteractions(floats);
    verifyNoInteractions(comparables);
  }

  @ParameterizedTest
  @CsvSource({ "1.0f, 1.0f", "0.0f, 0.0f" })
  void should_pass_using_primitive_comparison(Float actual, Float expected) {
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void should_fail_when_comparing_negative_zero_to_positive_zero() {
    // GIVEN
    final Float positiveZero = 0.0f;
    final float negativeZero = -0.0f;
    // THEN
    expectAssertionError(() -> assertThat(negativeZero).isEqualTo(positiveZero));
  }

  @Test
  void should_honor_user_specified_comparator() {
    // GIVEN
    final Float one = 1.0f;
    // THEN
    assertThat(-one).usingComparator(ALWAY_EQUAL_FLOAT)
                    .isEqualTo(one);
  }

}
