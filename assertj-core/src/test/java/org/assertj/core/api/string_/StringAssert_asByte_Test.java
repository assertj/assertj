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
import org.assertj.core.error.ShouldBeNumeric;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for <code>{@link StringAssert#asByte()}</code>.
 *
 * @author hezean
 */
class StringAssert_asByte_Test extends StringAssertBaseTest {

  @Override
  protected StringAssert invoke_api_method() {
    // Verify disabled as the asByte cast throws an AssertionError when the assertion's string is not a valid byte.
    // Tested below.
    return null;
  }

  @Override
  protected void verify_internal_effects() {
    // Verify disabled as the asByte cast have no internal effect.
  }

  @Override
  public void should_return_this() {
    // Test disabled as the assertion does not return this.
  }

  @ParameterizedTest
  @CsvSource({"127, 127", "-128, -128", "0, 0"})
  void should_parse_string_as_byte_for_valid_input(String string, byte byte_) {
    // THEN
    then(string).asByte().isEqualTo(byte_);
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = {"1024", "1L", "foo"})
  void should_throw_AssertionError_for_null_or_invalid_string(String string) {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(string).asByte());
    // THEN
    then(assertionError).hasMessage(shouldBeNumeric(string, ShouldBeNumeric.NumericType.BYTE).create());
  }
}
