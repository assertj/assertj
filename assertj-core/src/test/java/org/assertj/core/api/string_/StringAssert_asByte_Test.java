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
  @CsvSource({ "127, 127", "-128, -128", "0, 0" })
  void should_parse_string_as_byte_for_valid_input(String string, byte expectedByte) {
    assertThat(string).asByte().isEqualTo(expectedByte);
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = { "1024", "1L", "foo" })
  void should_throw_AssertionError_for_null_or_invalid_string(String string) {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(string).asByte());
    // WHEN/
    then(assertionError).hasMessage(shouldBeNumeric(string, NumericType.BYTE).create());
  }

  @Test
  void should_work_with_soft_assertions() {
    // GIVEN
    SoftAssertions softAssertions = new SoftAssertions();
    // WHEN
    softAssertions.assertThat("127").asByte()
                  .isEqualTo(124)
                  .isEqualTo(125);
    // THEN
    List<Throwable> errorsCollected = softAssertions.errorsCollected();
    then(errorsCollected).hasSize(2);
    then(errorsCollected.get(0)).hasMessageContaining("124");
    then(errorsCollected.get(1)).hasMessageContaining("125");
  }
}
