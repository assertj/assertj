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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.internal.paths;

import static java.nio.file.Files.createDirectory;
import static java.nio.file.Files.createFile;
import static java.nio.file.Files.createSymbolicLink;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveParent.shouldHaveParent;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

import org.assertj.core.internal.PathsBaseTest;
import org.junit.jupiter.api.Test;

class Paths_assertHasParent_Test extends PathsBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Path expected = tempDir.resolve("expected");
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertHasParent(INFO, null, expected));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_expected_is_null() {
    // GIVEN
    Path actual = tempDir.resolve("actual");
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.assertHasParent(INFO, actual, null));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage("expected parent path should not be null");
  }

  @Test
  void should_fail_if_actual_has_no_parent() {
    // GIVEN
    Path actual = tempDir.getRoot();
    Path expected = tempDir.resolve("expected");
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertHasParent(INFO, actual, expected));
    // THEN
    then(error).hasMessage(shouldHaveParent(actual, expected).create());
  }

  @Test
  void should_rethrow_IOException_as_UncheckedIOException_if_actual_cannot_be_resolved() throws IOException {
    // GIVEN
    Path actual = mock(Path.class);
    Path expected = createFile(tempDir.resolve("expected"));
    IOException exception = new IOException("boom!");
    given(actual.toRealPath()).willThrow(exception);
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.assertHasParent(INFO, actual, expected));
    // THEN
    then(thrown).isInstanceOf(UncheckedIOException.class)
                .hasCause(exception);
  }

  @Test
  void should_rethrow_IOException_as_UncheckedIOException_if_other_cannot_be_resolved() throws IOException {
    // GIVEN
    Path actual = createFile(tempDir.resolve("actual"));
    Path expected = mock(Path.class);
    IOException exception = new IOException("boom!");
    given(expected.toRealPath()).willThrow(exception);
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.assertHasParent(INFO, actual, expected));
    // THEN
    then(thrown).isInstanceOf(UncheckedIOException.class)
                .hasCause(exception);
  }

  @Test
  void should_fail_if_actual_parent_is_not_expected_parent() throws IOException {
    // GIVEN
    Path actual = createFile(tempDir.resolve("actual")).toRealPath();
    Path expected = createFile(tempDir.resolve("expected")).toRealPath();
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertHasParent(INFO, actual, expected));
    // THEN
    then(error).hasMessage(shouldHaveParent(actual, actual.getParent(), expected).create());
  }

  @Test
  void should_pass_if_actual_has_expected_parent() throws IOException {
    // GIVEN
    Path actual = createFile(tempDir.resolve("actual")).toRealPath();
    Path expected = tempDir.toRealPath();
    // WHEN/THEN
    underTest.assertHasParent(INFO, actual, expected);
  }

  @Test
  void should_pass_if_actual_is_not_canonical() throws IOException {
    // GIVEN
    Path expected = createDirectory(tempDir.resolve("expected"));
    Path file = createFile(expected.resolve("file"));
    Path actual = createSymbolicLink(tempDir.resolve("actual"), file);
    // WHEN/THEN
    underTest.assertHasParent(INFO, actual, expected);
  }

  @Test
  void should_pass_if_expected_is_not_canonical() throws IOException {
    // GIVEN
    Path directory = createDirectory(tempDir.resolve("directory"));
    Path expected = createSymbolicLink(tempDir.resolve("expected"), directory);
    Path actual = createFile(directory.resolve("actual"));
    // WHEN/THEN
    underTest.assertHasParent(INFO, actual, expected);
  }

}
