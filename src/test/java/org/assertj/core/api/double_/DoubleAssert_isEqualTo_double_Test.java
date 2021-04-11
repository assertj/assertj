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
import static org.assertj.core.test.ErrorMessagesForTest.shouldBeEqualMessage;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.Comparator;

import org.assertj.core.api.DoubleAssert;
import org.assertj.core.api.DoubleAssertBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests for <code>{@link DoubleAssert#isEqualTo(double)}</code>.
 *
 * @author Alex Ruiz
 */
@DisplayName("DoubleAssert isEqualTo with double")
class DoubleAssert_isEqualTo_double_Test extends DoubleAssertBaseTest {

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  protected DoubleAssert invoke_api_method() {
    // trick to simulate a custom comparator
    given(doubles.getComparator()).willReturn((Comparator) ALWAY_EQUAL_DOUBLE);
    return assertions.isEqualTo(8.0);
  }

  @Override
  protected void verify_internal_effects() {
    // verify we delegate to assertEqual when using a custom comparator
    verify(doubles).getComparator();
    verify(doubles).assertEqual(getInfo(assertions), getActual(assertions), 8.0);
    verifyNoMoreInteractions(doubles);
  }

  @ParameterizedTest
  @CsvSource({ "1.0d, 1.0d", "0.0d, 0.0d", "0.0d, -0.0d", "-0.0d, 0.0d" })
  void should_pass_using_primitive_comparison(double actual, double expected) {
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void should_honor_user_specified_comparator() {
    // GIVEN
    final double one = 1.0d;
    // THEN
    assertThat(-one).usingComparator(ALWAY_EQUAL_DOUBLE)
                    .isEqualTo(one);
  }

  @Test
  void should_fail_if_doubles_are_not_equal() {
    // GIVEN
    double actual = 6.0;
    double expected = 7.0;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isEqualTo(expected));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualMessage("6.0", "7.0"));
  }

  @Test
  void should_fail_with_clear_error_message_when_both_doubles_are_NaN() {
    // GIVEN
    double actual = Double.NaN;
    double expected = Double.NaN;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isEqualTo(expected));
    // THEN
    then(assertionError).hasMessage(format("Actual and expected values were compared with == because expected was a primitive double, the assertion failed as both were Double.NaN and Double.NaN != Double.NaN (as per Double#equals javadoc)"));
  }

  @Test
  void should_fail_when_actual_null_expected_primitive() {
    // GIVEN
    Double actual = null;
    double expected = 1.0d;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isEqualTo(expected));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }
}
