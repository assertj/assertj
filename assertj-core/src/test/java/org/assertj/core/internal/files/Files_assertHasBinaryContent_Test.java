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

import static org.apache.commons.io.FileUtils.writeByteArrayToFile;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeFile.shouldBeFile;
import static org.assertj.core.error.ShouldHaveBinaryContent.shouldHaveBinaryContent;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Files.newFile;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

import org.assertj.core.internal.BinaryDiff;
import org.assertj.core.internal.BinaryDiffResult;
import org.assertj.core.internal.Files;
import org.assertj.core.internal.FilesBaseTest;
import org.assertj.core.util.ResourceUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Files#assertHasBinaryContent(org.assertj.core.api.AssertionInfo, File, byte[])}</code>.
 * 
 * @author Olivier Michallat
 * @author Joel Costigliola
 */
class Files_assertHasBinaryContent_Test extends FilesBaseTest {

  private static File actual;
  private static byte[] expected;

  @BeforeAll
  static void setUpOnce() {
    // Does not matter if the values differ, the actual comparison is mocked in this test
    actual = ResourceUtil.getResource("actual_file.txt").toFile();
    expected = new byte[] {};
  }

  @Test
  void should_throw_error_if_expected_is_null() {
    // GIVEN
    byte[] expectedContent = null;
    // WHEN
    NullPointerException npe = catchThrowableOfType(() -> files.assertHasBinaryContent(INFO, actual, expectedContent),
                                                    NullPointerException.class);
    // THEN
    then(npe).hasMessage("The binary content to compare to should not be null");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    File actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> files.assertHasBinaryContent(INFO, actual, expected));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_is_not_file() {
    // GIVEN
    File notAFile = new File("xyz");
    // WHEN
    expectAssertionError(() -> files.assertHasBinaryContent(INFO, notAFile, expected));
    // THEN
    verify(failures).failure(INFO, shouldBeFile(notAFile));
  }

  @Test
  void should_pass_if_file_has_expected_binary_content() throws IOException {
    // GIVEN
    File actual = newFile(tempDir.getAbsolutePath() + "/tmp.txt");
    writeByteArrayToFile(actual, "actual".getBytes());
    byte[] expected = "actual".getBytes();
    // WHEN/THEN
    unMockedFiles.assertHasBinaryContent(INFO, actual, expected);
  }

  @Test
  void should_throw_error_wrapping_caught_IOException() throws IOException {
    // GIVEN
    IOException cause = new IOException();
    when(binaryDiff.diff(actual, expected)).thenThrow(cause);
    // THEN
    UncheckedIOException uioe = catchThrowableOfType(() -> files.assertHasBinaryContent(INFO, actual, expected),
                                                     UncheckedIOException.class);
    // THEN
    then(uioe).hasCause(cause);
  }

  @Test
  void should_fail_if_file_does_not_have_expected_binary_content() throws IOException {
    // GIVEN
    File actual = newFile(tempDir.getAbsolutePath() + "/tmp.txt");
    writeByteArrayToFile(actual, "actual".getBytes());
    byte[] expected = "fake".getBytes();
    BinaryDiff binaryDiff = new BinaryDiff();
    BinaryDiffResult diff = binaryDiff.diff(actual, expected);
    // WHEN
    expectAssertionError(() -> unMockedFiles.assertHasBinaryContent(INFO, actual, expected));
    // THEN
    verify(unMockedFailures).failure(INFO, shouldHaveBinaryContent(actual, diff));
  }
}
