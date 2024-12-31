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
package org.assertj.tests.core.api.inputstream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import org.assertj.core.api.AbstractStringAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class InputStreamAssert_asString_with_Charset_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    InputStream actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).asString(UTF_8));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_given_charset_is_null() {
    // GIVEN
    InputStream actual = new ByteArrayInputStream(new byte[0]);
    // WHEN
    Exception exception = catchException(() -> assertThat(actual).asString(null));
    // THEN
    then(exception).isInstanceOf(NullPointerException.class)
                   .hasMessage(shouldNotBeNull("charset").create());
  }

  @ParameterizedTest
  @CsvSource({
      "value, UTF-8",
      "Gerçek, windows-1254" // Turkish
  })
  void should_return_string_assertions_resetting_actual_if_actual_supports_marking(String input,
                                                                                   Charset charset) throws Exception {
    // GIVEN
    InputStream actual = new ByteArrayInputStream(input.getBytes(charset));
    // WHEN
    AbstractStringAssert<?> result = assertThat(actual).asString(charset);
    // THEN
    result.isNotBlank();
    then(actual.read()).isEqualTo(input.charAt(0));
  }

  @ParameterizedTest
  @CsvSource({
      "value, UTF-8",
      "Gerçek, windows-1254" // Turkish
  })
  void should_return_string_assertions_without_resetting_actual_if_actual_does_not_support_marking(String input,
                                                                                                   Charset charset) throws Exception {
    // GIVEN
    InputStream actual = new UnmarkableByteArrayInputStream(input.getBytes(charset));
    // WHEN
    AbstractStringAssert<?> result = assertThat(actual).asString(charset);
    // THEN
    result.isNotBlank();
    then(actual).isEmpty();
  }

  @Test
  void should_rethrow_IOException() throws Exception {
    // GIVEN
    @SuppressWarnings("resource")
    InputStream actual = mock();
    IOException cause = new IOException();
    given(actual.read(any())).willThrow(cause);
    // WHEN
    Exception exception = catchException(() -> assertThat(actual).asString(UTF_8));
    // WHEN/THEN
    then(exception).isInstanceOf(UncheckedIOException.class)
                   .hasCause(cause);
  }

}
