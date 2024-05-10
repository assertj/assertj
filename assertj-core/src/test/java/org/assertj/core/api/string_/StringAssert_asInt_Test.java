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
package org.assertj.core.api.string_;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeNumeric.shouldBeNumeric;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.StringAssert;
import org.assertj.core.api.StringAssertBaseTest;
import org.assertj.core.error.ShouldBeNumeric.NumericType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for <code>{@link StringAssert#asInt()}</code>.
 *
 * @author hezean
 */
class StringAssert_asInt_Test extends StringAssertBaseTest {

  @Override
  protected StringAssert invoke_api_method() {
    // Verify disabled as the asInt cast throws an AssertionError when the assertion's string is not a valid integer.
    // Tested below.
    return null;
  }

  @Override
  protected void verify_internal_effects() {
    // Verify disabled as the asInt cast have no internal effect.
  }

  @Override
  public void should_return_this() {
    // Test disabled as the assertion does not return this.
  }

  @ParameterizedTest
  @CsvSource({ "123, 123", "0, 0", Integer.MIN_VALUE + ", " + Integer.MIN_VALUE, Integer.MAX_VALUE + ", " + Integer.MAX_VALUE })
  void should_parse_string_as_long_for_valid_input(String string, int expectedInt) {
    assertThat(string).asInt().isEqualTo(expectedInt);
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = { "1e100", "foo" })
  void should_throw_AssertionError_when_null_or_not_a_valid_int(String string) {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(string).asInt());
    // WHEN/THEN
    then(assertionError).hasMessage(shouldBeNumeric(string, NumericType.INTEGER).create());
  }

  @Test
  void should_work_with_soft_assertions() {
    // GIVEN
    SoftAssertions softAssertions = new SoftAssertions();
    // WHEN
    softAssertions.assertThat("123").asInt()
                  .isEqualTo(124)
                  .isEqualTo(125);
    // THEN
    List<Throwable> errorsCollected = softAssertions.errorsCollected();
    then(errorsCollected).hasSize(2);
    then(errorsCollected.get(0)).hasMessageContaining("124");
    then(errorsCollected.get(1)).hasMessageContaining("125");
  }
}
