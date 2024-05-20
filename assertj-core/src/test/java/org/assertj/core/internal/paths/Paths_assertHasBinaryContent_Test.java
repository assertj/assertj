/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.internal.paths;

import static java.nio.file.Files.createFile;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeReadable.shouldBeReadable;
import static org.assertj.core.error.ShouldExist.shouldExist;
import static org.assertj.core.error.ShouldHaveBinaryContent.shouldHaveBinaryContent;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.junit.jupiter.api.condition.OS.WINDOWS;
import static org.mockito.BDDMockito.willThrow;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.assertj.core.internal.BinaryDiffResult;
import org.assertj.core.internal.PathsBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;

class Paths_assertHasBinaryContent_Test extends PathsBaseTest {

  @Test
  void should_fail_if_expected_is_null() throws IOException {
    // GIVEN
    Path actual = createFile(tempDir.resolve("actual"));
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.assertHasBinaryContent(INFO, actual, null));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage("The binary content to compare to should not be null");
  }

  @Test
  void should_fail_if_actual_is_null() throws IOException {
    // GIVEN
    byte[] expected = "expected".getBytes();
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertHasBinaryContent(INFO, null, expected));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_exist() throws IOException {
    // GIVEN
    Path actual = tempDir.resolve("non-existent");
    byte[] expected = "expected".getBytes();
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertHasBinaryContent(INFO, actual, expected));
    // THEN
    then(error).hasMessage(shouldExist(actual).create());
  }

  @Test
  @DisabledOnOs(value = WINDOWS, disabledReason = "gh-2312")
  void should_fail_if_actual_is_not_readable() throws IOException {
    // GIVEN
    Path actual = createFile(tempDir.resolve("actual"));
    actual.toFile().setReadable(false);
    byte[] expected = "expected".getBytes();
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertHasBinaryContent(INFO, actual, expected));
    // THEN
    then(error).hasMessage(shouldBeReadable(actual).create());
  }

  @Test
  void should_pass_if_actual_has_expected_binary_content() throws IOException {
    // GIVEN
    Path actual = Files.write(tempDir.resolve("actual"), "Content".getBytes());
    byte[] expected = "Content".getBytes();
    // WHEN/THEN
    underTest.assertHasBinaryContent(INFO, actual, expected);
  }

  @Test
  void should_fail_if_actual_does_not_have_expected_binary_content() throws IOException {
    // GIVEN
    Path actual = Files.write(tempDir.resolve("actual"), "Content".getBytes());
    byte[] expected = "Another content".getBytes();
    BinaryDiffResult diff = binaryDiff.diff(actual, expected);
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertHasBinaryContent(INFO, actual, expected));
    // THEN
    then(error).hasMessage(shouldHaveBinaryContent(actual, diff).create(INFO.description(), INFO.representation()));
  }

  @Test
  void should_rethrow_IOException_as_UncheckedIOException() throws IOException {
    // GIVEN
    Path actual = createFile(tempDir.resolve("actual"));
    byte[] expected = "expected".getBytes();
    IOException exception = new IOException("boom!");
    willThrow(exception).given(binaryDiff).diff(actual, expected);
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.assertHasBinaryContent(INFO, actual, expected));
    // THEN
    then(thrown).isInstanceOf(UncheckedIOException.class)
                .hasMessage("Unable to verify binary contents of path:<%s>", actual)
                .hasCause(exception);
  }

}
