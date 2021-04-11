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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.api.double_;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.test.AlwaysDifferentComparator.ALWAY_DIFFERENT;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.Comparator;

import org.assertj.core.api.DoubleAssert;
import org.assertj.core.api.DoubleAssertBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests for <code>{@link DoubleAssert#isNotEqualTo(double)}</code>.
 *
 * @author Alex Ruiz
 */
class DoubleAssert_isNotEqualTo_double_Test extends DoubleAssertBaseTest {

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  protected DoubleAssert invoke_api_method() {
    // trick to simulate a custom comparator
    given(doubles.getComparator()).willReturn((Comparator) ALWAY_EQUAL_DOUBLE);
    return assertions.isNotEqualTo(8d);
  }

  @Override
  protected void verify_internal_effects() {
    verify(doubles).getComparator();
    verify(doubles).assertNotEqual(getInfo(assertions), getActual(assertions), 8d);
    verifyNoMoreInteractions(doubles);
  }

  @ParameterizedTest
  @CsvSource({ "1.0, -1.0", "NaN, NaN" })
  void should_pass_using_primitive_comparison(double actual, double expected) {
    assertThat(actual).isNotEqualTo(expected);
  }

  @Test
  void should_honor_user_specified_comparator() {
    // GIVEN
    final double one = 1.0d;
    // THEN
    assertThat(one).usingComparator(ALWAY_DIFFERENT)
                   .isNotEqualTo(one);
  }

  @Test
  void should_fail_if_doubles_are_equal() {
    // GIVEN
    double actual = 0.0;
    double expected = -0.0;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isNotEqualTo(expected));
    // THEN
    then(assertionError).hasMessage(format("%nExpecting actual:%n" +
                                           "  0.0%n" +
                                           "not to be equal to:%n" +
                                           "  -0.0%n"));
  }

  @Test
  void should_fail_when_actual_null_expected_primitive() {
    // GIVEN
    Double actual = null;
    double expected = 1.0d;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isNotEqualTo(expected));
    // THEN
    then(assertionError).hasMessageContaining(shouldNotBeNull().create());
  }
}
