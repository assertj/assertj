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
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;
import org.assertj.core.api.StringAssert;
import org.assertj.core.api.StringAssertBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * @author Emanuel Trandafir
 */
class StringAssert_bytes_Test extends StringAssertBaseTest {

  @Override
  protected StringAssert invoke_api_method() {
    // Tested below.
    return null;
  }

  @Override
  protected void verify_internal_effects() {
    // Verify disabled as the bytes() cast have no internal effect.
  }

  @Override
  public void should_return_this() {
    // Test disabled as the assertion does not return this.
  }

  @ParameterizedTest
  @MethodSource
  void should_encode_string_to_byte_array_according_to_the_default_charset(String actualString, byte[] expectedBytes) {
    assertThat(actualString).bytes()
                            .isEqualTo(expectedBytes);
  }

  static Stream<Arguments> should_encode_string_to_byte_array_according_to_the_default_charset() {
    return Stream.of(arguments("abc", new byte[] { 'a', 'b', 'c' }),
                     arguments("ABC", new byte[] { 'A', 'B', 'C' }),
                     arguments("&%ç", "&%ç".getBytes()));
  }

  @ParameterizedTest
  @MethodSource
  void should_encode_string_to_byte_array_according_to_the_given_charset(String actualString, byte[] expectedBytes,
                                                                         String charset) {
    assertThat(actualString).bytes(charset)
                            .isEqualTo(expectedBytes);
  }

  static Stream<Arguments> should_encode_string_to_byte_array_according_to_the_given_charset() throws UnsupportedEncodingException {
    return Stream.of(arguments("abc", new byte[] { 'a', 'b', 'c' }, "windows-1254"),
                     arguments("çüş", "çüş".getBytes("windows-1254"), "windows-1254"));
  }

  @Test
  void should_not_throw_exception_for_empty_string() {
    assertThat("").bytes().isEmpty();
  }

  @Test
  void should_throw_assertion_error_for_null_input() {
    // WHEN
    AssertionError assertionError = expectAssertionError(assertThat((String) null)::bytes);
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_encode_string_to_byte_array_for_specific_charset_and_valid_input() {
    // GIVEN
    String actual = "abc";
    // WHEN/THEN
    then(actual).bytes(StandardCharsets.US_ASCII)
                .isEqualTo(actual.getBytes(StandardCharsets.US_ASCII));
  }

  @Test
  void should_not_throw_exception_for_specific_charset_and_empty_string() {
    // GIVEN
    String actual = "";
    // WHEN/THEN
    then(actual).bytes(StandardCharsets.US_ASCII).isEmpty();
  }

  @Test
  void should_throw_assertion_error_for_specific_charset_and_null_input() {
    // GIVEN
    String actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).bytes(StandardCharsets.US_ASCII));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_throw_exception_for_null_charset() {
    assertThatNullPointerException().isThrownBy(() -> assertThat("abc").bytes((Charset) null))
                                    .withMessage("The charset must not be null");
  }

  @Test
  void should_encode_string_to_byte_array_for_specific_charset_name_and_valid_input() {
    // GIVEN
    String actual = "abc";
    // WHEN/THEN
    then(actual).bytes("UTF-8").isEqualTo(actual.getBytes(StandardCharsets.UTF_8));
  }

  @Test
  void should_not_throw_exception_for_specific_charset_name_and_empty_string() {
    // GIVEN
    String actual = "";
    // WHEN/THEN
    then(actual).bytes("UTF-8").isEmpty();
  }

  @Test
  void should_throw_assertion_error_for_specific_charset_name_and_null_input() {
    // GIVEN
    String actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).bytes("UTF-8"));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_throw_exception_for_null_charset_name() {
    assertThatNullPointerException().isThrownBy(() -> assertThat("abc").bytes((String) null))
                                    .withMessage("The charsetName must not be null");
  }

  @Test
  void should_throw_AssertionError_for_invalid_charset_name() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat("abc").bytes("UNSUPPORTED_CHARSET"));
    // THEN
    then(assertionError).hasMessage("UNSUPPORTED_CHARSET is not a supported Charset");
  }

}
