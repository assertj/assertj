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
import static org.assertj.core.api.Assertions.catchIllegalArgumentException;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeFile.shouldBeFile;
import static org.assertj.core.error.ShouldHaveBinaryContent.shouldHaveBinaryContent;
import static org.assertj.core.error.ShouldHaveSameContent.shouldHaveSameContent;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.assertj.core.internal.BinaryDiff;
import org.assertj.core.internal.Diff;
import org.assertj.tests.core.testkit.Charsets;
import org.assertj.tests.core.testkit.PathFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.opentest4j.AssertionFailedError;
import org.opentest4j.FileInfo;

class FileAssert_hasSameTextualContentAs_Test {

  // a 0x00 byte followed by 0xFE, which is not valid UTF-8
  private static final byte[] NON_UTF8_CONTENT = { 0x00, (byte) 0xFE };

  @TempDir
  Path tempDir;

  @Test
  void should_fail_if_actual_is_null() throws IOException {
    // GIVEN
    File actual = null;
    File expected = createFile("expected", Charset.defaultCharset());
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasSameTextualContentAs(expected));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_expected_is_null() throws IOException {
    // GIVEN
    File actual = createFile("actual", Charset.defaultCharset());
    File expected = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).hasSameTextualContentAs(expected));
    // THEN
    then(npe).hasMessage("The file to compare to should not be null");
  }

  @Test
  void should_fail_if_expected_does_not_exist() throws IOException {
    // GIVEN
    File actual = createFile("actual", Charset.defaultCharset());
    File expected = PathFactory.createPath(tempDir).toFile();
    // WHEN
    var iae = catchIllegalArgumentException(() -> assertThat(actual).hasSameTextualContentAs(expected));
    // THEN
    then(iae).hasMessage("Expected file:<'%s'> should be an existing file", expected);
  }

  @Test
  void should_fail_if_expected_is_a_directory() throws IOException {
    // GIVEN
    File actual = createFile("actual", Charset.defaultCharset());
    File expected = Files.createDirectory(PathFactory.createPath(tempDir)).toFile();
    // WHEN
    var iae = catchIllegalArgumentException(() -> assertThat(actual).hasSameTextualContentAs(expected));
    // THEN
    then(iae).hasMessage("Expected file:<'%s'> should be an existing file", expected);
  }

  @Test
  void should_fail_if_actual_does_not_exist() throws IOException {
    // GIVEN
    File actual = PathFactory.createPath(tempDir).toFile();
    File expected = createFile("expected", Charset.defaultCharset());
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasSameTextualContentAs(expected));
    // THEN
    then(assertionError).hasMessage(shouldBeFile(actual).create());
  }

  @Test
  void should_fail_if_actual_is_a_directory() throws IOException {
    // GIVEN
    File actual = Files.createDirectory(PathFactory.createPath(tempDir)).toFile();
    File expected = createFile("expected", Charset.defaultCharset());
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasSameTextualContentAs(expected));
    // THEN
    then(assertionError).hasMessage(shouldBeFile(actual).create());
  }

  @Test
  void should_fail_if_actual_and_expected_do_not_have_same_content() throws IOException {
    // GIVEN
    File actual = createFile("actual%n".formatted(), Charset.defaultCharset());
    File expected = createFile("expected%n".formatted(), Charset.defaultCharset());
    var diffs = new Diff().diff(actual, Charset.defaultCharset(), expected, Charset.defaultCharset());
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasSameTextualContentAs(expected));
    // THEN
    then(assertionError).hasMessage(shouldHaveSameContent(actual, expected, diffs).create());
  }

  @Test
  void should_fail_with_file_info_of_actual_and_expected() throws IOException {
    // GIVEN
    File actual = createFile("actual%n".formatted(), Charset.defaultCharset());
    File expected = createFile("expected%n".formatted(), Charset.defaultCharset());
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasSameTextualContentAs(expected));
    // THEN
    thenHasFileInfoOfActualAndExpected(assertionError, actual, expected);
  }

  @Test
  void should_fail_with_binary_diff_if_actual_cannot_be_read_with_given_charset() throws IOException {
    // GIVEN
    File actual = createFile(NON_UTF8_CONTENT);
    File expected = PathFactory.createFile(tempDir).toFile();
    var binaryDiff = new BinaryDiff().diff(actual, new byte[0]);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingCharset(UTF_8)
                                                                      .hasSameTextualContentAs(expected, UTF_8));
    // THEN
    then(assertionError).hasMessage(shouldHaveBinaryContent(actual, binaryDiff).create());
    thenHasFileInfoOfActualAndExpected(assertionError, actual, expected);
  }

  @Test
  void should_throw_error_if_files_cannot_be_read_with_given_charsets_even_if_binary_identical() throws IOException {
    // GIVEN
    File actual = createFile(NON_UTF8_CONTENT);
    File expected = createFile(NON_UTF8_CONTENT);
    // WHEN
    var uncheckedIOException = catchThrowableOfType(UncheckedIOException.class,
                                                    () -> assertThat(actual).usingCharset(UTF_8)
                                                                            .hasSameTextualContentAs(expected, UTF_8));
    // THEN
    then(uncheckedIOException).hasMessage("Unable to compare contents of files:<%s> and:<%s>", actual, expected)
                              .hasCauseInstanceOf(MalformedInputException.class);
  }

  @Test
  void should_pass_if_actual_and_expected_have_same_content() throws IOException {
    // GIVEN
    File actual = createFile("line1%nline2%n".formatted(), Charset.defaultCharset());
    File expected = createFile("line1%nline2%n".formatted(), Charset.defaultCharset());
    // WHEN/THEN
    then(actual).hasSameTextualContentAs(expected);
  }

  @Test
  void should_pass_if_actual_is_compared_to_itself() throws IOException {
    // GIVEN
    File actual = createFile("actual%n".formatted(), Charset.defaultCharset());
    // WHEN/THEN
    then(actual).hasSameTextualContentAs(actual);
  }

  @Test
  void should_use_charset_specified_by_usingCharset_to_read_actual_file_content() throws IOException {
    // GIVEN
    File actual = createFile("Gerçek", Charsets.TURKISH);
    File expected = createFile("Gerçek", Charset.defaultCharset());
    // WHEN/THEN
    then(actual).usingCharset(Charsets.TURKISH).hasSameTextualContentAs(expected);
  }

  @Test
  void should_allow_charset_to_be_specified_for_reading_expected_file_content() throws IOException {
    // GIVEN
    File actual = createFile("Gerçek", Charset.defaultCharset());
    File expected = createFile("Gerçek", Charsets.TURKISH);
    // WHEN/THEN
    then(actual).hasSameTextualContentAs(expected, Charsets.TURKISH);
  }

  private static void thenHasFileInfoOfActualAndExpected(AssertionError assertionError, File actual,
                                                         File expected) throws IOException {
    byte[] actualBytes = Files.readAllBytes(actual.toPath());
    byte[] expectedBytes = Files.readAllBytes(expected.toPath());
    then(assertionError).isInstanceOfSatisfying(AssertionFailedError.class, assertionFailedError -> {
      then(assertionFailedError.getActual().getValue()).isInstanceOfSatisfying(FileInfo.class, fileInfo -> {
        then(fileInfo.getPath()).isEqualTo(actual.getAbsolutePath());
        then(fileInfo.getContents()).isEqualTo(actualBytes);
      });
      then(assertionFailedError.getExpected().getValue()).isInstanceOfSatisfying(FileInfo.class, fileInfo -> {
        then(fileInfo.getPath()).isEqualTo(expected.getAbsolutePath());
        then(fileInfo.getContents()).isEqualTo(expectedBytes);
      });
    });
  }

  private File createFile(String content, Charset charset) throws IOException {
    return Files.writeString(PathFactory.createPath(tempDir), content, charset).toFile();
  }

  private File createFile(byte[] content) throws IOException {
    return Files.write(PathFactory.createPath(tempDir), content).toFile();
  }

}
