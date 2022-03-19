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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api.float_;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeGreaterOrEqual.shouldBeGreaterOrEqual;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Comparator;

import org.assertj.core.api.FloatAssert;
import org.assertj.core.api.FloatAssertBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests for <code>{@link FloatAssert#isGreaterThanOrEqualTo(float)}</code>.
 *
 * @author Alex Ruiz
 */
@DisplayName("FloatAssert isGreaterThanOrEqualTo with float")
class FloatAssert_isGreaterThanOrEqualTo_float_Test extends FloatAssertBaseTest {

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  protected FloatAssert invoke_api_method() {
    // trick to simulate a custom comparator
    given(floats.getComparator()).willReturn((Comparator) ALWAY_EQUAL_FLOAT);
    return assertions.isGreaterThanOrEqualTo(6.0f);
  }

  @Override
  protected void verify_internal_effects() {
    // verify we delegate to assertGreaterThanOrEqualTo when using a custom comparator
    verify(floats).getComparator();
    verify(floats).assertGreaterThanOrEqualTo(getInfo(assertions), getActual(assertions), 6.0f);
  }

  @ParameterizedTest(name = "verify {0} >= {1} assertion succeeds")
  @CsvSource({ "1.0d, 1.0d", "0.0d, -0.0d", "-0.0d, 0.0d", "1.0d, 0.0d" })
  void should_pass_using_primitive_comparison(float actual, float expected) {
    assertThat(actual).isGreaterThanOrEqualTo(expected);
  }

  @Test
  void should_honor_user_specified_comparator() {
    // GIVEN
    final float one = 1.0f;
    // THEN
    assertThat(-one).usingComparator(ALWAY_EQUAL_FLOAT)
                    .isGreaterThanOrEqualTo(one);
  }

  @Test
  void should_fail_if_actual_is_less_than_expected() {
    // GIVEN
    float actual = 6.0f;
    float expected = 7.0f;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isGreaterThanOrEqualTo(expected));
    // THEN
    then(assertionError).hasMessage(shouldBeGreaterOrEqual(actual, expected).create());
  }
}
