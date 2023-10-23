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
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.assertj.core.api.StringAssert;
import org.assertj.core.api.StringAssertBaseTest;
import org.junit.jupiter.api.Test;

/**
 * @author Emanuel Trandafir
 */
class StringAssert_asBytes_Test extends StringAssertBaseTest {

  @Override
  protected StringAssert invoke_api_method() {
    // Tested below.
    return null;
  }

  @Override
  protected void verify_internal_effects() {
    // Verify disabled as the asBytes cast have no internal effect.
  }

  @Override
  public void should_return_this() {
    // Test disabled as the assertion does not return this.
  }

  @Test
  void should_map_string_to_byte_array_for_valid_input() {
    assertThat("abc").asBytes().isEqualTo(new byte[] { 'a', 'b', 'c' });
  }

  @Test
  void should_map_to_empty_byte_array_for_empty_string() {
    assertThat("").asBytes().isEqualTo(new byte[0]);
  }

  @Test
  void should_not_throw_exception_for_null_input() {
    assertThat((String) null).asBytes().isNull();
  }

  @Test
  void should_map_string_to_byte_array_for_specific_charset_and_valid_input() {
    assertThat("abc").asBytes(StandardCharsets.US_ASCII).isEqualTo("abc".getBytes(StandardCharsets.US_ASCII));
  }

  @Test
  void should_map_to_empty_byte_array_for_specific_charset_and_empty_string() {
    assertThat("").asBytes(StandardCharsets.US_ASCII).isEqualTo(new byte[] {});
  }

  @Test
  void should_not_throw_exception_for_specific_charset_and_null_input() {
    assertThat((String) null).asBytes(StandardCharsets.US_ASCII).isNull();
  }

  @Test
  void should_throw_exception_for_null_charset() {
    assertThatNullPointerException()
      .isThrownBy(() -> assertThat("abc").asBytes((Charset) null))
      .withMessage("The charset must not be null");
  }

  @Test
  void should_map_string_to_byte_array_for_specific_charset_name_and_valid_input() {
    assertThat("abc").asBytes("UTF-8").isEqualTo("abc".getBytes(StandardCharsets.UTF_8));
  }

  @Test
  void should_map_to_empty_byte_array_for_specific_charset_name_and_empty_string() {
    assertThat("").asBytes("UTF-8").isEqualTo(new byte[] {});
  }

  @Test
  void should_not_throw_exception_for_specific_charset_name_and_null_input() {
    assertThat((String) null).asBytes("UTF-8").isNull();
  }

  @Test
  void should_throw_exception_for_null_charset_name() {
    assertThatNullPointerException()
      .isThrownBy(() -> assertThat("abc").asBytes((String) null))
      .withMessage("The charsetName must not be null");
  }

  @Test
  void should_throw_AssertionError_for_invalid_charset_name() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat("abc").asBytes("UNSUPPORTED_CHARSET"));
    // THEN
    then(assertionError).hasMessage("UNSUPPORTED_CHARSET is not a supported Charset");
  }

}
