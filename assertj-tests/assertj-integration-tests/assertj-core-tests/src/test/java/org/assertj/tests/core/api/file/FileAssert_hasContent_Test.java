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
package org.assertj.tests.core.api.file;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeFile.shouldBeFile;
import static org.assertj.core.error.ShouldHaveContent.shouldHaveContent;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.assertj.core.internal.Diff;
import org.assertj.tests.core.testkit.Charsets;
import org.assertj.tests.core.testkit.PathFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileAssert_hasContent_Test {

  @TempDir
  Path tempDir;

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    File actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasContent("content"));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_expected_is_null() throws IOException {
    // GIVEN
    File actual = PathFactory.createFile(tempDir).toFile();
    String expected = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).hasContent(expected));
    // THEN
    then(npe).hasMessage("The text to compare to should not be null");
  }

  @Test
  void should_fail_if_actual_does_not_exist() {
    // GIVEN
    File actual = PathFactory.createPath(tempDir).toFile();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasContent("content"));
    // THEN
    then(assertionError).hasMessage(shouldBeFile(actual).create());
  }

  @Test
  void should_fail_if_actual_is_a_directory() throws IOException {
    // GIVEN
    File actual = Files.createDirectory(PathFactory.createPath(tempDir)).toFile();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasContent("content"));
    // THEN
    then(assertionError).hasMessage(shouldBeFile(actual).create());
  }

  @Test
  void should_fail_if_actual_does_not_have_expected_content() throws IOException {
    // GIVEN
    File actual = createFile("actual", Charset.defaultCharset());
    String expected = "expected";
    var diffs = new Diff().diff(actual, expected, Charset.defaultCharset());
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasContent(expected));
    // THEN
    then(assertionError).hasMessage(shouldHaveContent(actual, Charset.defaultCharset(), diffs).create());
  }

  @Test
  void should_fail_if_actual_content_read_with_given_charset_differs() throws IOException {
    // GIVEN
    File actual = createFile("Touché", UTF_8);
    String expected = "Touché";
    var diffs = new Diff().diff(actual, expected, Charsets.TURKISH);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingCharset(Charsets.TURKISH).hasContent(expected));
    // THEN
    then(assertionError).hasMessage(shouldHaveContent(actual, Charsets.TURKISH, diffs).create());
  }

  @Test
  void should_fail_if_actual_has_extra_trailing_line_terminator() throws IOException {
    // GIVEN
    File actual = createFile("actual%n".formatted(), Charset.defaultCharset());
    String expected = "actual";
    var diffs = new Diff().diff(actual, expected, Charset.defaultCharset());
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasContent(expected));
    // THEN
    then(assertionError).hasMessage(shouldHaveContent(actual, Charset.defaultCharset(), diffs).create());
  }

  @Test
  void should_throw_error_wrapping_caught_IOException() throws IOException {
    // GIVEN
    File actual = Files.write(PathFactory.createPath(tempDir), new byte[] { (byte) 0xFE }).toFile();
    // WHEN
    var uncheckedIOException = catchThrowableOfType(UncheckedIOException.class,
                                                    () -> assertThat(actual).usingCharset(UTF_8).hasContent("content"));
    // THEN
    then(uncheckedIOException).hasMessage("Unable to verify text contents of file:<%s>", actual)
                              .hasCauseInstanceOf(MalformedInputException.class);
  }

  @Test
  void should_pass_if_actual_has_expected_content() throws IOException {
    // GIVEN
    File actual = createFile("actual", Charset.defaultCharset());
    // WHEN/THEN
    then(actual).hasContent("actual");
  }

  @Test
  void should_pass_if_actual_has_expected_multiline_content_whatever_the_line_terminators() throws IOException {
    // GIVEN
    File actual = createFile("line1\r\nline2\rline3\n", Charset.defaultCharset());
    // WHEN/THEN
    then(actual).hasContent("line1\nline2\nline3\n");
  }

  @Test
  void should_pass_if_actual_has_expected_content_read_with_given_charset() throws IOException {
    // GIVEN
    File actual = createFile("Gerçek", Charsets.TURKISH);
    // WHEN/THEN
    then(actual).usingCharset(Charsets.TURKISH).hasContent("Gerçek");
  }

  private File createFile(String content, Charset charset) throws IOException {
    return Files.writeString(PathFactory.createPath(tempDir), content, charset).toFile();
  }

}
