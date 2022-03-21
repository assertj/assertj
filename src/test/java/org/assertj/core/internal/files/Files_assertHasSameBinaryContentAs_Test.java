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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.internal.files;

import static java.nio.file.Files.readAllBytes;
import static org.apache.commons.io.FileUtils.writeByteArrayToFile;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeFile.shouldBeFile;
import static org.assertj.core.error.ShouldHaveBinaryContent.shouldHaveBinaryContent;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Files.newFile;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

import org.assertj.core.internal.BinaryDiff;
import org.assertj.core.internal.BinaryDiffResult;
import org.assertj.core.internal.FilesBaseTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class Files_assertHasSameBinaryContentAs_Test extends FilesBaseTest {

  private static File actual;
  private static File expected;
  private static byte[] expectedBytes;

  @BeforeAll
  static void setUpOnce() throws IOException {
    // Does not matter if the values differ, the actual comparison is mocked in this test
    actual = new File("src/test/resources/actual_file.txt");
    expected = new File("src/test/resources/expected_file.txt");
    expectedBytes = readAllBytes(expected.toPath());
  }

  @Test
  void should_pass_if_file_has_expected_binary_content() throws IOException {
    // GIVEN
    byte[] data = "some content".getBytes();
    File actual = newFile(tempDir.getAbsolutePath() + "/actual.txt");
    writeByteArrayToFile(actual, data);
    File expected = newFile(tempDir.getAbsolutePath() + "/expected.txt");
    writeByteArrayToFile(expected, data);
    // THEN
    unMockedFiles.assertSameBinaryContentAs(INFO, actual, expected);
  }

  @Test
  void should_throw_error_if_expected_is_null() {
    // GIVEN
    File expected = null;
    // WHEN
    NullPointerException npe = catchThrowableOfType(() -> files.assertSameBinaryContentAs(INFO, actual, expected),
                                                    NullPointerException.class);
    // THEN
    then(npe).hasMessage("The file to compare to should not be null");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    File actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> files.assertSameBinaryContentAs(INFO, actual, expected));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_is_not_a_file() {
    // GIVEN
    File notAFile = new File("xyz");
    // WHEN
    AssertionError error = expectAssertionError(() -> files.assertSameBinaryContentAs(INFO, notAFile, expected));
    // THEN
    then(error).hasMessage(shouldBeFile(notAFile).create());
  }

  @Test
  void should_fail_if_expected_is_not_a_file() {
    // GIVEN
    File notAFile = new File("xyz");
    // WHEN
    IllegalArgumentException iae = catchThrowableOfType(() -> files.assertSameBinaryContentAs(INFO, actual, notAFile),
                                                        IllegalArgumentException.class);
    // THEN
    then(iae).hasMessage("Expected file:<'%s'> should be an existing file", notAFile);
  }

  @Test
  void should_throw_error_wrapping_caught_IOException() throws IOException {
    // GIVEN
    IOException cause = new IOException();
    given(binaryDiff.diff(actual, expectedBytes)).willThrow(cause);
    // WHEN
    UncheckedIOException uioe = catchThrowableOfType(() -> files.assertSameBinaryContentAs(INFO, actual, expected),
                                                     UncheckedIOException.class);
    // THEN
    then(uioe).hasCause(cause);
  }

  @Test
  void should_fail_if_file_does_not_have_expected_binary_content() throws IOException {
    // GIVEN
    BinaryDiff binaryDiff = new BinaryDiff();
    BinaryDiffResult diff = binaryDiff.diff(actual, expectedBytes);
    // WHEN
    expectAssertionError(() -> unMockedFiles.assertSameBinaryContentAs(INFO, actual, expected));
    // THEN
    verify(unMockedFailures).failure(INFO, shouldHaveBinaryContent(actual, diff));
  }
}
