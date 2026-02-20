/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.api.inputstream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveSameContent.shouldHaveSameContent;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.assertj.core.internal.Diff;
import org.assertj.core.util.diff.Delta;
import org.junit.jupiter.api.Test;

/**
 * @author Jaeung Ha
 */
class InputStreamAssert_hasSameTextualContentAs_Test {

  private static List<Delta<String>> diff(String actual, String expected) {
    try {
      return new Diff().diff(new ByteArrayInputStream(actual.getBytes()), new ByteArrayInputStream(expected.getBytes()));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private static List<Delta<String>> diff(InputStream actual, Charset actualCharset, InputStream expected,
                                          Charset expectedCharset) {
    try {
      return new Diff().diff(actual, actualCharset, expected, expectedCharset);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    InputStream actual = null;
    InputStream expected = new ByteArrayInputStream(new byte[0]);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasSameTextualContentAs(expected, StandardCharsets.UTF_8));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_expected_is_null() {
    // GIVEN
    InputStream actual = new ByteArrayInputStream(new byte[0]);
    InputStream expected = null;
    // WHEN
    Exception exception = catchException(() -> assertThat(actual).hasSameTextualContentAs(expected, StandardCharsets.UTF_8));
    // THEN
    then(exception).isInstanceOf(NullPointerException.class)
                   .hasMessage(shouldNotBeNull("expected").create());
  }

  @Test
  void should_rethrow_IOException() throws Exception {
    // GIVEN
    @SuppressWarnings("resource")
    InputStream actual = mock();
    InputStream expected = new ByteArrayInputStream(new byte[0]);
    IOException cause = new IOException();
    given(actual.read(any(), anyInt(), anyInt())).willThrow(cause);
    // WHEN
    Exception exception = catchException(() -> assertThat(actual).hasSameTextualContentAs(expected, StandardCharsets.UTF_8));
    // THEN
    then(exception).isInstanceOf(UncheckedIOException.class)
                   .hasCause(cause);
  }

  @Test
  void should_pass_resetting_actual_and_expected_if_actual_has_expected_content_and_both_support_marking() {
    // GIVEN
    InputStream actual = new ByteArrayInputStream("12345".getBytes());
    InputStream expected = new ByteArrayInputStream("12345".getBytes());
    // WHEN
    assertThat(actual).hasSameTextualContentAs(expected, StandardCharsets.UTF_8);
    // THEN
    then(actual).isNotEmpty();
    then(expected).isNotEmpty();
  }

  @Test
  void should_pass_resetting_actual_if_actual_has_expected_content_and_actual_supports_marking() {
    // GIVEN
    InputStream actual = new ByteArrayInputStream("12345".getBytes());
    InputStream expected = new UnmarkableByteArrayInputStream("12345".getBytes());
    // WHEN
    assertThat(actual).hasSameTextualContentAs(expected, StandardCharsets.UTF_8);
    // THEN
    then(actual).isNotEmpty();
    then(expected).isEmpty();
  }

  @Test
  void should_pass_resetting_expected_if_actual_has_expected_content_and_expected_supports_marking() {
    // GIVEN
    InputStream actual = new UnmarkableByteArrayInputStream("12345".getBytes());
    InputStream expected = new ByteArrayInputStream("12345".getBytes());
    // WHEN
    assertThat(actual).hasSameTextualContentAs(expected, StandardCharsets.UTF_8);
    // THEN
    then(actual).isEmpty();
    then(expected).isNotEmpty();
  }

  @Test
  void should_pass_without_resetting_if_actual_has_expected_content_and_none_supports_marking() {
    // GIVEN
    InputStream actual = new UnmarkableByteArrayInputStream("12345".getBytes());
    InputStream expected = new UnmarkableByteArrayInputStream("12345".getBytes());
    // WHEN
    assertThat(actual).hasSameTextualContentAs(expected, StandardCharsets.UTF_8);
    // THEN
    then(actual).isEmpty();
    then(expected).isEmpty();
  }

  @Test
  void should_fail_resetting_actual_and_expected_if_actual_does_not_have_expected_content_and_both_support_marking() {
    // GIVEN
    InputStream actual = new ByteArrayInputStream("12345".getBytes());
    InputStream expected = new ByteArrayInputStream("67890".getBytes());
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasSameTextualContentAs(expected, StandardCharsets.UTF_8));
    // THEN
    then(assertionError).hasMessage(shouldHaveSameContent(actual, expected, diff("12345", "67890")).create());
    then(actual).isNotEmpty();
    then(expected).isNotEmpty();
  }

  @Test
  void should_fail_resetting_actual_if_actual_does_not_have_expected_content_and_actual_supports_marking() {
    // GIVEN
    InputStream actual = new ByteArrayInputStream("12345".getBytes());
    InputStream expected = new UnmarkableByteArrayInputStream("67890".getBytes());
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasSameTextualContentAs(expected, StandardCharsets.UTF_8));
    // THEN
    then(assertionError).hasMessage(shouldHaveSameContent(actual, expected, diff("12345", "67890")).create());
    then(actual).isNotEmpty();
    then(expected).isEmpty();
  }

  @Test
  void should_fail_resetting_expected_if_actual_does_not_have_expected_content_and_expected_supports_marking() {
    // GIVEN
    InputStream actual = new UnmarkableByteArrayInputStream("12345".getBytes());
    InputStream expected = new ByteArrayInputStream("67890".getBytes());
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasSameTextualContentAs(expected, StandardCharsets.UTF_8));
    // THEN
    then(assertionError).hasMessage(shouldHaveSameContent(actual, expected, diff("12345", "67890")).create());
    then(actual).isEmpty();
    then(expected).isNotEmpty();
  }

  @Test
  void should_fail_without_resetting_if_actual_does_not_have_expected_content_and_none_supports_marking() {
    // GIVEN
    InputStream actual = new UnmarkableByteArrayInputStream("12345".getBytes());
    InputStream expected = new ByteArrayInputStream("67890".getBytes());
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasSameTextualContentAs(expected, StandardCharsets.UTF_8));
    // THEN
    then(assertionError).hasMessage(shouldHaveSameContent(actual, expected, diff("12345", "67890")).create());
    then(actual).isEmpty();
    then(expected).isNotEmpty();
  }

  @Test
  void should_pass_when_inputStream_use_diff_charset() {
    // GIVEN
    InputStream actual = new ByteArrayInputStream("12345".getBytes(StandardCharsets.UTF_8));
    InputStream expected = new ByteArrayInputStream("12345".getBytes(StandardCharsets.ISO_8859_1));
    // WHEN
    assertThat(actual)
                      .usingCharset(StandardCharsets.UTF_8)
                      .hasSameTextualContentAs(expected, StandardCharsets.ISO_8859_1);
    // THEN
    then(actual).isNotEmpty();
    then(expected).isNotEmpty();
  }

  @Test
  void should_fail_when_use_wrong_charset() {
    // GIVEN
    String text = "é";
    InputStream actual = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
    InputStream expected = new ByteArrayInputStream(text.getBytes(StandardCharsets.ISO_8859_1));
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual)
                                                                      .usingCharset(StandardCharsets.ISO_8859_1)
                                                                      .hasSameTextualContentAs(expected,
                                                                                               StandardCharsets.ISO_8859_1));
    // THEN
    // @format:off
    List<Delta<String>> diff = diff(new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8)), StandardCharsets.ISO_8859_1,
                                    new ByteArrayInputStream(text.getBytes(StandardCharsets.ISO_8859_1)), StandardCharsets.ISO_8859_1);
    // @format:on
    then(assertionError).isInstanceOf(AssertionError.class);
    then(assertionError).hasMessage(shouldHaveSameContent(actual, expected, diff).create());
  }

  @Test
  void should_fail_when_given_unsupported_charset_string() {
    // GIVEN
    String charsetName = "foo";
    String text = "foo";
    InputStream actual = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
    InputStream expected = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
    // WHEN
    Exception exception = catchException(() -> assertThat(actual)
                                                                 .usingCharset(charsetName)
                                                                 .hasSameTextualContentAs(expected,
                                                                                          StandardCharsets.UTF_8));
    // THEN
    then(exception).isInstanceOf(IllegalArgumentException.class);
    then(exception).hasMessage("Charset:<'foo'> is not supported on this system");
  }

  @Test
  void should_fail_when_given_actual_charset_string_is_null() {
    // GIVEN
    String charsetName = null;
    String text = "foo";
    InputStream actual = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
    InputStream expected = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
    // WHEN
    Exception exception = catchException(() -> assertThat(actual)
                                                                 .usingCharset(charsetName)
                                                                 .hasSameTextualContentAs(expected,
                                                                                          StandardCharsets.UTF_8));
    // THEN
    then(exception).isInstanceOf(NullPointerException.class);
    then(exception).hasMessage(shouldNotBeNull("charsetName").create());
  }

  @Test
  void should_fail_when_given_actual_charset_instance_is_null() {
    // GIVEN
    Charset charset = null;
    String text = "foo";
    InputStream actual = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
    InputStream expected = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
    // WHEN
    Exception exception = catchException(() -> assertThat(actual)
                                                                 .usingCharset(charset)
                                                                 .hasSameTextualContentAs(expected,
                                                                                          StandardCharsets.UTF_8));
    // THEN
    then(exception).isInstanceOf(NullPointerException.class);
    then(exception).hasMessage(shouldNotBeNull("charset").create());
  }

  @Test
  void should_fail_when_given_expected_charset_instance_is_null() {
    // GIVEN
    Charset charset = null;
    String text = "foo";
    InputStream actual = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
    InputStream expected = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
    // WHEN
    Exception exception = catchException(() -> assertThat(actual)
                                                                 .usingCharset(StandardCharsets.UTF_8)
                                                                 .hasSameTextualContentAs(expected, charset));
    // THEN
    then(exception).isInstanceOf(NullPointerException.class);
    then(exception).hasMessage(shouldNotBeNull("charset").create());
  }

  @Test
  void should_pass_if_both_are_empty() {
    // GIVEN
    InputStream actual = new ByteArrayInputStream(new byte[0]);
    InputStream expected = new ByteArrayInputStream(new byte[0]);

    // WHEN & THEN
    then(actual).hasSameTextualContentAs(expected, StandardCharsets.UTF_8);
  }

  @Test
  void should_pass_if_line_endings_differ() {
    // GIVEN
    InputStream actual = new ByteArrayInputStream("line1\nline2".getBytes(StandardCharsets.UTF_8));
    InputStream expected = new ByteArrayInputStream("line1\r\nline2".getBytes(StandardCharsets.UTF_8));
    // WHEN
    assertThat(actual).hasSameTextualContentAs(expected, StandardCharsets.UTF_8);
    // THEN
    then(actual).isNotEmpty();
    then(expected).isNotEmpty();
  }

}
