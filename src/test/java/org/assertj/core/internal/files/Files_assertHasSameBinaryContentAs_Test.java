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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.internal.files;

import static java.nio.file.Files.readAllBytes;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeFile.shouldBeFile;
import static org.assertj.core.error.ShouldHaveBinaryContent.shouldHaveBinaryContent;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Files.newFile;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.FileOutputStream;
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
    // GIVEN/WHEN
    byte[] data = "some content".getBytes();

    File actual = newFile(tempDir.getAbsolutePath() + "/actual.txt");
    try (FileOutputStream myWriter = new FileOutputStream(actual)) {
      myWriter.write(data, 0, data.length);
    }

    File expected = newFile(tempDir.getAbsolutePath() + "/expected.txt");
    try (FileOutputStream myWriter = new FileOutputStream(expected)) {
      myWriter.write(data, 0, data.length);
    }

    // THEN
    unMockedFiles.assertSameBinaryContentAs(someInfo(), actual, expected);
  }

  @Test
  void should_throw_error_if_expected_is_null() {
    // GIVEN/WHEN
    NullPointerException npe = catchThrowableOfType(() -> files.assertSameBinaryContentAs(someInfo(), actual, null),
                                                    NullPointerException.class);
    // THEN
    then(npe).hasMessage("The file to compare to should not be null");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN/WHEN
    AssertionError error = expectAssertionError(() -> files.assertSameBinaryContentAs(someInfo(), null, expected));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_is_not_a_file() {
    // GIVEN
    File notAFile = new File("xyz");
    // WHEN
    AssertionError error = expectAssertionError(() -> files.assertSameBinaryContentAs(someInfo(), notAFile, expected));
    // THEN
    then(error).hasMessage(shouldBeFile(notAFile).create());
  }

  @Test
  void should_fail_if_expected_is_not_a_file() {
    // GIVEN
    File notAFile = new File("xyz");
    // WHEN
    IllegalArgumentException iae = catchThrowableOfType(() -> files.assertSameBinaryContentAs(someInfo(), actual, notAFile),
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
    UncheckedIOException uioe = catchThrowableOfType(() -> files.assertSameBinaryContentAs(someInfo(), actual, expected),
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
    expectAssertionError(() -> unMockedFiles.assertSameBinaryContentAs(someInfo(), actual, expected));
    // THEN
    verify(unMockedFailures).failure(someInfo(), shouldHaveBinaryContent(actual, diff));
  }
}
