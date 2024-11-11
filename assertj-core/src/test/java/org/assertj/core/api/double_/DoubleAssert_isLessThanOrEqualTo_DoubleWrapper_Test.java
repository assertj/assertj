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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api.double_;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import org.assertj.core.api.DoubleAssert;
import org.assertj.core.api.DoubleAssertBaseTest;
import org.junit.jupiter.api.Test;

class DoubleAssert_isLessThanOrEqualTo_DoubleWrapper_Test extends DoubleAssertBaseTest {

  private final Double other = 11.0D;

  @Override
  protected DoubleAssert invoke_api_method() {
    return assertions.isLessThanOrEqualTo(other);
  }

  @Override
  protected void verify_internal_effects() {
    verify(comparables).assertLessThanOrEqualTo(getInfo(assertions), getActual(assertions), other);
    verifyNoInteractions(doubles);
  }

  @Test
  void should_pass_when_comparing_negative_zero_to_positive_zero() {
    // GIVEN
    final Double positiveZero = 0.0d;
    final double negativeZero = -0.0d;
    // THEN
    assertThat(negativeZero).isLessThanOrEqualTo(positiveZero);
  }

  @Test
  void should_fail_when_comparing_positive_zero_to_negative_zero() {
    // GIVEN
    final Double positiveZero = 0.0d;
    final Double negativeZero = -0.0d;
    // THEN
    expectAssertionError(() -> assertThat(positiveZero).isLessThanOrEqualTo(negativeZero));
    verifyNoInteractions(doubles);
  }

  @Test
  void should_honor_user_specified_comparator() {
    // GIVEN
    final Double one = 1.0d;
    // THEN
    assertThat(-one).usingComparator(ALWAY_EQUAL_DOUBLE)
                    .isLessThanOrEqualTo(one);
  }

}
