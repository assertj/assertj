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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchIllegalArgumentException;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeFile.shouldBeFile;
import static org.assertj.core.error.ShouldHaveBinaryContent.shouldHaveBinaryContent;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.assertj.core.internal.BinaryDiff;
import org.assertj.tests.core.testkit.PathFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.opentest4j.AssertionFailedError;
import org.opentest4j.FileInfo;

class FileAssert_hasSameBinaryContentAs_Test {

  @TempDir
  Path tempDir;

  @Test
  void should_fail_if_actual_is_null() throws IOException {
    // GIVEN
    File actual = null;
    File expected = createFile("expected".getBytes());
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasSameBinaryContentAs(expected));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_expected_is_null() throws IOException {
    // GIVEN
    File actual = createFile("actual".getBytes());
    File expected = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).hasSameBinaryContentAs(expected));
    // THEN
    then(npe).hasMessage("The file to compare to should not be null");
  }

  @Test
  void should_fail_if_expected_does_not_exist() throws IOException {
    // GIVEN
    File actual = createFile("actual".getBytes());
    File expected = PathFactory.createPath(tempDir).toFile();
    // WHEN
    var iae = catchIllegalArgumentException(() -> assertThat(actual).hasSameBinaryContentAs(expected));
    // THEN
    then(iae).hasMessage("Expected file:<'%s'> should be an existing file", expected);
  }

  @Test
  void should_fail_if_expected_is_a_directory() throws IOException {
    // GIVEN
    File actual = createFile("actual".getBytes());
    File expected = Files.createDirectory(PathFactory.createPath(tempDir)).toFile();
    // WHEN
    var iae = catchIllegalArgumentException(() -> assertThat(actual).hasSameBinaryContentAs(expected));
    // THEN
    then(iae).hasMessage("Expected file:<'%s'> should be an existing file", expected);
  }

  @Test
  void should_fail_if_actual_does_not_exist() throws IOException {
    // GIVEN
    File actual = PathFactory.createPath(tempDir).toFile();
    File expected = createFile("expected".getBytes());
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasSameBinaryContentAs(expected));
    // THEN
    then(assertionError).hasMessage(shouldBeFile(actual).create());
  }

  @Test
  void should_fail_if_actual_is_a_directory() throws IOException {
    // GIVEN
    File actual = Files.createDirectory(PathFactory.createPath(tempDir)).toFile();
    File expected = createFile("expected".getBytes());
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasSameBinaryContentAs(expected));
    // THEN
    then(assertionError).hasMessage(shouldBeFile(actual).create());
  }

  @Test
  void should_fail_if_actual_does_not_have_expected_binary_content() throws IOException {
    // GIVEN
    File actual = createFile("assertJ".getBytes());
    byte[] expectedBytes = "assertJ++".getBytes();
    File expected = createFile(expectedBytes);
    var binaryDiff = new BinaryDiff().diff(actual, expectedBytes);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasSameBinaryContentAs(expected));
    // THEN
    then(assertionError).hasMessage(shouldHaveBinaryContent(actual, binaryDiff).create());
  }

  @Test
  void should_fail_with_file_info_of_actual_and_expected() throws IOException {
    // GIVEN
    byte[] actualBytes = "actual".getBytes();
    File actual = createFile(actualBytes);
    byte[] expectedBytes = "expected".getBytes();
    File expected = createFile(expectedBytes);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasSameBinaryContentAs(expected));
    // THEN
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

  @Test
  void should_pass_if_actual_has_same_binary_content_as_expected() throws IOException {
    // GIVEN
    byte[] content = "some content".getBytes();
    File actual = createFile(content);
    File expected = createFile(content);
    // WHEN/THEN
    then(actual).hasSameBinaryContentAs(expected);
  }

  private File createFile(byte[] content) throws IOException {
    return Files.write(PathFactory.createPath(tempDir), content).toFile();
  }

}
