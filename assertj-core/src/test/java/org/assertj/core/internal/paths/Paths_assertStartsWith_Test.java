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

import static java.nio.file.Files.createDirectory;
import static java.nio.file.Files.createFile;
import static java.nio.file.Files.createSymbolicLink;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldStartWithPath.shouldStartWith;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

import org.assertj.core.internal.PathsBaseTest;
import org.junit.jupiter.api.Test;

class Paths_assertStartsWith_Test extends PathsBaseTest {

  @Test
  void should_fail_if_actual_is_null() throws IOException {
    // GIVEN
    Path other = createFile(tempDir.resolve("other"));
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertStartsWith(INFO, null, other));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_other_is_null() throws IOException {
    // GIVEN
    Path actual = createFile(tempDir.resolve("actual"));
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.assertStartsWith(INFO, actual, null));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage("the expected start path should not be null");
  }

  @Test
  void should_rethrow_IOException_as_UncheckedIOException_if_actual_cannot_be_resolved() throws IOException {
    // GIVEN
    Path actual = mock(Path.class);
    Path other = createFile(tempDir.resolve("other"));
    IOException exception = new IOException("boom!");
    given(actual.toRealPath()).willThrow(exception);
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.assertStartsWith(INFO, actual, other));
    // THEN
    then(thrown).isInstanceOf(UncheckedIOException.class)
                .hasCause(exception);
  }

  @Test
  void should_rethrow_IOException_as_UncheckedIOException_if_other_cannot_be_resolved() throws IOException {
    // GIVEN
    Path actual = createFile(tempDir.resolve("actual"));
    Path other = mock(Path.class);
    IOException exception = new IOException("boom!");
    given(other.toRealPath()).willThrow(exception);
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.assertStartsWith(INFO, actual, other));
    // THEN
    then(thrown).isInstanceOf(UncheckedIOException.class)
                .hasCause(exception);
  }

  @Test
  void should_fail_if_actual_does_not_start_with_other() throws IOException {
    // GIVEN
    Path actual = createFile(tempDir.resolve("actual"));
    Path other = createFile(tempDir.resolve("other"));
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertStartsWith(INFO, actual, other));
    // THEN
    then(error).hasMessage(shouldStartWith(actual, other).create());
  }

  @Test
  void should_pass_if_actual_starts_with_other() throws IOException {
    // GIVEN
    Path other = createDirectory(tempDir.resolve("other")).toRealPath();
    Path actual = createFile(other.resolve("actual")).toRealPath();
    // WHEN/THEN
    underTest.assertStartsWith(INFO, actual, other);
  }

  @Test
  void should_pass_if_actual_is_not_canonical() throws IOException {
    // GIVEN
    Path other = createDirectory(tempDir.resolve("other"));
    Path file = createFile(other.resolve("file"));
    Path actual = createSymbolicLink(tempDir.resolve("actual"), file);
    // WHEN/THEN
    underTest.assertStartsWith(INFO, actual, other);
  }

  @Test
  void should_pass_if_other_is_not_canonical() throws IOException {
    // GIVEN
    Path directory = createDirectory(tempDir.resolve("directory"));
    Path other = createSymbolicLink(tempDir.resolve("other"), directory);
    Path actual = createFile(directory.resolve("actual"));
    // WHEN/THEN
    underTest.assertStartsWith(INFO, actual, other);
  }

}
