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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.api.string_;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeNumeric.shouldBeNumeric;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.StringAssert;
import org.assertj.core.api.StringAssertBaseTest;
import org.assertj.core.data.Percentage;
import org.assertj.core.error.ShouldBeNumeric;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for <code>{@link StringAssert#asDouble()}</code>.
 *
 * @author hezean
 */
class StringAssert_asDouble_Test extends StringAssertBaseTest {

  @Override
  protected StringAssert invoke_api_method() {
    // Verify disabled as the asDouble cast throws an AssertionError when the assertion's string is not a valid double.
    // Tested below.
    return null;
  }

  @Override
  protected void verify_internal_effects() {
    // Verify disabled as the asDouble cast have no internal effect.
  }

  @Override
  public void should_return_this() {
    // Test disabled as the assertion does not return this.
  }

  @Test
  void should_parse_string_as_float_when_parseable() {
    // GIVEN
    String string = "1.2e308";
    // THEN
    then(string).asDouble().isCloseTo(1.2e308, Percentage.withPercentage(0.001));
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = {"foo", "", "0xffg"})
  void should_throw_AssertionError_when_null_or_not_numeric(String string) {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(string).asDouble());
    // THEN
    then(assertionError).hasMessage(shouldBeNumeric(string, ShouldBeNumeric.NumericType.DOUBLE).create());
  }
}
