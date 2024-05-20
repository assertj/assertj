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

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.testkit.AlwaysDifferentComparator.ALWAY_DIFFERENT;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.Comparator;
import org.assertj.core.api.FloatAssert;
import org.assertj.core.api.FloatAssertBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests for <code>{@link FloatAssert#isNotEqualTo(float)}</code>.
 *
 * @author Alex Ruiz
 */
class FloatAssert_isNotEqualTo_float_primitive_Test extends FloatAssertBaseTest {

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  protected FloatAssert invoke_api_method() {
    // trick to simulate a custom comparator
    given(floats.getComparator()).willReturn((Comparator) ALWAY_EQUAL_FLOAT);
    return assertions.isNotEqualTo(8f);
  }

  @Override
  protected void verify_internal_effects() {
    verify(floats).getComparator();
    verify(floats).assertNotEqual(getInfo(assertions), getActual(assertions), 8f);
    verifyNoMoreInteractions(floats);
  }

  @ParameterizedTest
  @CsvSource({ "1.0f, -1.0f", "NaN, NaN" })
  void should_pass_using_primitive_comparison(float actual, float expected) {
    assertThat(actual).isNotEqualTo(expected);
  }

  @Test
  void should_honor_user_specified_comparator() {
    // GIVEN
    final float one = 1.0f;
    // THEN
    assertThat(one).usingComparator(ALWAY_DIFFERENT)
                   .isNotEqualTo(one);
  }

  @Test
  void should_fail_if_floats_are_equal() {
    // GIVEN
    float actual = 0.0f;
    float expected = -0.0f;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isNotEqualTo(expected));
    // THEN
    then(assertionError).hasMessage(format("%nExpecting actual:%n" +
                                           "  0.0f%n" +
                                           "not to be equal to:%n" +
                                           "  -0.0f%n"));
  }
}
