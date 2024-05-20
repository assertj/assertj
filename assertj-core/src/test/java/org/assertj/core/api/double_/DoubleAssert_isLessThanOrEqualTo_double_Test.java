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
package org.assertj.core.api.double_;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeLessOrEqual.shouldBeLessOrEqual;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Comparator;
import org.assertj.core.api.DoubleAssert;
import org.assertj.core.api.DoubleAssertBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests for <code>{@link DoubleAssert#isLessThanOrEqualTo(double)}</code>.
 *
 * @author Alex Ruiz
 */
class DoubleAssert_isLessThanOrEqualTo_double_Test extends DoubleAssertBaseTest {

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  protected DoubleAssert invoke_api_method() {
    // trick to simulate a custom comparator
    given(doubles.getComparator()).willReturn((Comparator) ALWAY_EQUAL_DOUBLE);
    return assertions.isLessThanOrEqualTo(6.0);
  }

  @Override
  protected void verify_internal_effects() {
    // verify we delegate to assertLessThanOrEqualTo when using a custom comparator
    verify(doubles).getComparator();
    verify(doubles).assertLessThanOrEqualTo(getInfo(assertions), getActual(assertions), 6.0);
  }

  @ParameterizedTest(name = "verify {0} <= {1} assertion succeeds")
  @CsvSource({ "1.0d, 1.0d", "0.0d, -0.0d", "-0.0d, 0.0d", "0.0d, 1.0d", "-1.0d, 0.0d" })
  void should_pass_using_primitive_comparison(double actual, double expected) {
    assertThat(actual).isLessThanOrEqualTo(expected);
  }

  @Test
  void should_honor_user_specified_comparator() {
    // GIVEN
    final double one = 1.0d;
    // THEN
    assertThat(one).usingComparator(ALWAY_EQUAL_DOUBLE)
                   .isLessThanOrEqualTo(-one);
  }

  @Test
  void should_fail_if_actual_is_greater_than_expected() {
    // GIVEN
    double actual = 8.0;
    double expected = 7.0;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isLessThanOrEqualTo(expected));
    // THEN
    then(assertionError).hasMessage(shouldBeLessOrEqual(actual, expected).create());
  }
}
