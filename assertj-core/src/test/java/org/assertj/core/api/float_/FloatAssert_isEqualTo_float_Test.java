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
import static org.assertj.core.testkit.ErrorMessagesForTest.shouldBeEqualMessage;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.Comparator;
import org.assertj.core.api.FloatAssert;
import org.assertj.core.api.FloatAssertBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests for <code>{@link FloatAssert#isEqualTo(float)}</code>.
 *
 * @author Alex Ruiz
 */
@DisplayName("FloatAssert isEqualTo with float")
class FloatAssert_isEqualTo_float_Test extends FloatAssertBaseTest {

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  protected FloatAssert invoke_api_method() {
    // trick to simulate a custom comparator
    given(floats.getComparator()).willReturn((Comparator) ALWAY_EQUAL_FLOAT);
    return assertions.isEqualTo(8f);
  }

  @Override
  protected void verify_internal_effects() {
    // verify we delegate when using a custom comparator
    verify(floats).getComparator();
    verify(floats).assertEqual(getInfo(assertions), getActual(assertions), 8f);
    verifyNoMoreInteractions(floats);
  }

  @ParameterizedTest
  @CsvSource({ "1.0f, 1.0f", "0.0f, 0.0f", "0.0f, -0.0f", "-0.0f, 0.0f" })
  void should_pass_using_primitive_comparison(float actual, float expected) {
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void should_honor_user_specified_comparator() {
    // GIVEN
    final float one = 1.0f;
    // THEN
    then(-one).usingComparator(ALWAY_EQUAL_FLOAT)
              .isEqualTo(one);
  }

  @Test
  void should_fail_if_floats_are_not_equal() {
    // GIVEN
    float actual = 6f;
    float expected = 7f;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isEqualTo(expected));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualMessage("6.0f", "7.0f"));
  }

  @Test
  void should_fail_with_clear_error_message_when_both_floats_are_NaN() {
    // GIVEN
    float actual = Float.NaN;
    float expected = Float.NaN;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isEqualTo(expected));
    // THEN
    then(assertionError).hasMessage(format("Actual and expected values were compared with == because expected was a primitive float, the assertion failed as both were Float.NaN and Float.NaN != Float.NaN (as per Float#equals javadoc)"));
  }
}
