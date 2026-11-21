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
package org.assertj.tests.core.api.path;

import static java.nio.charset.Charset.defaultCharset;
import static java.nio.file.Files.createDirectory;
import static java.nio.file.Files.createFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeReadable.shouldBeReadable;
import static org.assertj.core.error.ShouldExist.shouldExist;
import static org.assertj.core.error.ShouldHaveContent.shouldHaveContent;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.junit.jupiter.api.condition.OS.WINDOWS;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import org.assertj.core.internal.Diff;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PathAssert_hasContent_Test {

  private static final Charset CHARSET = defaultCharset();

  @TempDir
  Path tempDir;

  @Test
  void should_fail_if_expected_is_null() throws IOException {
    // GIVEN
    Path actual = createFile(tempDir.resolve("actual"));
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(actual).hasContent(null));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage("The text to compare to should not be null");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Path actual = null;
    String expected = "expected";
    // WHEN
    var error = expectAssertionError(() -> assertThat(actual).hasContent(expected));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_exist() {
    // GIVEN
    Path actual = tempDir.resolve("non-existent");
    String expected = "expected";
    // WHEN
    var error = expectAssertionError(() -> assertThat(actual).hasContent(expected));
    // THEN
    then(error).hasMessage(shouldExist(actual).create());
  }

  @Test
  @DisabledOnOs(value = WINDOWS, disabledReason = "gh-2312")
  void should_fail_if_actual_is_not_readable() throws IOException {
    // GIVEN
    Path actual = createFile(tempDir.resolve("actual"));
    actual.toFile().setReadable(false);
    String expected = "expected";
    // WHEN
    var error = expectAssertionError(() -> assertThat(actual).hasContent(expected));
    // THEN
    then(error).hasMessage(shouldBeReadable(actual).create());
  }

  @Test
  void should_pass_if_actual_has_expected_textual_content() throws IOException {
    // GIVEN
    Path actual = Files.write(tempDir.resolve("actual"), "Content".getBytes(CHARSET));
    String expected = "Content";
    // WHEN/THEN
    then(actual).hasContent(expected);
  }

  @Test
  void should_pass_if_actual_has_expected_textual_content_read_with_given_charset() throws IOException {
    // GIVEN
    Charset charset = Charset.forName("UTF-16");
    Path actual = Files.write(tempDir.resolve("actual"), "Content".getBytes(charset));
    String expected = "Content";
    // WHEN/THEN
    then(actual).usingCharset(charset)
                .hasContent(expected);
  }

  @Test
  void should_fail_if_actual_does_not_have_expected_textual_content() throws IOException {
    // GIVEN
    Path actual = Files.write(tempDir.resolve("actual"), "Content".getBytes(CHARSET));
    String expected = "Another content";
    var diffs = new Diff().diff(actual, expected, CHARSET);
    // WHEN
    var error = expectAssertionError(() -> assertThat(actual).hasContent(expected));
    // THEN
    then(error).hasMessage(shouldHaveContent(actual, CHARSET, diffs).create());
  }

  @Test
  void should_fail_if_actual_does_not_have_expected_textual_content_read_with_given_charset() throws IOException {
    // GIVEN
    Charset charset = Charset.forName("UTF-16");
    Path actual = Files.write(tempDir.resolve("actual"), "Content".getBytes(charset));
    String expected = "Another content";
    var diffs = new Diff().diff(actual, expected, charset);
    // WHEN
    var error = expectAssertionError(() -> assertThat(actual).usingCharset(charset)
                                                             .hasContent(expected));
    // THEN
    then(error).hasMessage(shouldHaveContent(actual, charset, diffs).create());
  }

  @ParameterizedTest(name = "actual={0}, expected={1}")
  @CsvSource(delimiter = '|', value = {
      "Hello|Hello",
      "Hello\\n|Hello\\n",
      "Hello\\r\\n|Hello\\n",
      "Hello\\n|Hello\\r",
      "\\nHello|\\nHello",
      "Hello\\n\\n|Hello\\r\\n\\r\\n",
  })
  void should_pass_if_actual_has_expected_textual_content_including_newlines(String actualContent,
                                                                             String expected) throws IOException {
    // GIVEN
    Path actual = Files.write(tempDir.resolve("actual"), unescape(actualContent).getBytes(CHARSET));
    // WHEN/THEN
    then(actual).hasContent(unescape(expected));
  }

  // gh-2993
  @ParameterizedTest(name = "actual={0}, expected={1}")
  @CsvSource(delimiter = '|', value = {
      "Hello|Hello\\n",
      "Hello\\n|Hello",
      "Hello\\r\\n|Hello",
      "Hello\\n|Hello\\n\\n",
      "Hello\\n\\n|Hello\\n",
      "Hello|\\nHello",
  })
  void should_fail_if_actual_and_expected_textual_content_differ_by_newlines(String actualContent,
                                                                             String expectedContent) throws IOException {
    // GIVEN
    Path actual = Files.write(tempDir.resolve("actual"), unescape(actualContent).getBytes(CHARSET));
    String expected = unescape(expectedContent);
    var diffs = new Diff().diff(actual, expected, CHARSET);
    // WHEN
    var error = expectAssertionError(() -> assertThat(actual).hasContent(expected));
    // THEN
    then(diffs).isNotEmpty();
    then(error).hasMessage(shouldHaveContent(actual, CHARSET, diffs).create());
  }

  @Test
  void should_rethrow_IOException_as_UncheckedIOException_if_actual_content_cannot_be_read() throws IOException {
    // GIVEN
    Path actual = createDirectory(tempDir.resolve("actual"));
    String expected = "expected";
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(actual).hasContent(expected));
    // THEN
    then(thrown).isInstanceOf(UncheckedIOException.class)
                .hasMessage("Unable to verify text contents of path:<%s>", actual)
                .hasCauseInstanceOf(IOException.class);
  }

  private static String unescape(String value) {
    return value.replace("\\r", "\r").replace("\\n", "\n");
  }

}
