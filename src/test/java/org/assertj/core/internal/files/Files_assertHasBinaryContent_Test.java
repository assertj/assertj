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

import static java.nio.file.Files.createFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeFile.shouldBeFile;
import static org.assertj.core.error.ShouldHaveBinaryContent.shouldHaveBinaryContent;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Files.newFile;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.BinaryDiff;
import org.assertj.core.internal.BinaryDiffResult;
import org.assertj.core.internal.Files;
import org.assertj.core.internal.FilesBaseTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Files#assertHasBinaryContent(org.assertj.core.api.AssertionInfo, File, byte[])}</code>.
 * 
 * @author Olivier Michallat
 * @author Joel Costigliola
 */
@DisplayName("Files.assertHasBinaryContent")
class Files_assertHasBinaryContent_Test extends FilesBaseTest {

  private static File actual;
  private static byte[] expected;

  @BeforeAll
  static void setUpOnce() {
    // Does not matter if the values differ, the actual comparison is mocked in this test
    actual = new File("src/test/resources/actual_file.txt");
    expected = new byte[] {};
  }

  @Test
  void should_throw_error_if_expected_is_null() {
    assertThatNullPointerException().isThrownBy(() -> files.assertHasBinaryContent(someInfo(), actual, null))
                                    .withMessage("The binary content to compare to should not be null");
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> files.assertHasBinaryContent(someInfo(), null, expected))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_is_not_file() {
    AssertionInfo info = someInfo();
    File notAFile = new File("xyz");

    Throwable error = catchThrowable(() -> files.assertHasBinaryContent(info, notAFile, expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeFile(notAFile));
  }

  @Test
  void should_pass_if_file_has_expected_binary_content() throws IOException {

    File actual = newFile(tempDir.getAbsolutePath() + "/tmp.txt");
    byte[] data = "actual".getBytes();
    try (FileOutputStream myWriter = new FileOutputStream(actual)) {
      myWriter.write(data, 0, data.length);
    }
    byte[] expected = "actual".getBytes();
    unMockedFiles.assertHasBinaryContent(someInfo(), actual, expected);
  }

  @Test
  void should_throw_error_wrapping_caught_IOException() throws IOException {
    IOException cause = new IOException();
    when(binaryDiff.diff(actual, expected)).thenThrow(cause);

    assertThatExceptionOfType(UncheckedIOException.class).isThrownBy(() -> files.assertHasBinaryContent(someInfo(),
                                                                                                        actual,
                                                                                                        expected))
                                                         .withCause(cause);
  }

  @Test
  void should_fail_if_file_does_not_have_expected_binary_content() throws IOException {
    File actual = newFile(tempDir.getAbsolutePath() + "/tmp.txt");
    byte[] data = "actual".getBytes();
    try (FileOutputStream myWriter = new FileOutputStream(actual)) {
      myWriter.write(data, 0, data.length);
    }
    byte[] expected = "fake".getBytes();

    BinaryDiff binaryDiff = new BinaryDiff();
    BinaryDiffResult diff = binaryDiff.diff(actual, expected);
    AssertionInfo info = someInfo();

    AssertionError error = expectAssertionError(() -> unMockedFiles.assertHasBinaryContent(info, actual, expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    then(error).hasMessage(shouldHaveBinaryContent(actual, diff).create(info.description(), info.representation()));
  }
}
