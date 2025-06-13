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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.tests.core.internal.paths;

import static java.nio.file.Files.createFile;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldEndWithPath.shouldEndWith;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

class Paths_assertEndsWith_Test extends PathsBaseTest {

  @Test
  void should_fail_if_actual_is_null() throws IOException {
    // GIVEN
    Path other = tempDir.resolve("other");
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertEndsWith(INFO, null, other));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_other_is_null() throws IOException {
    // GIVEN
    Path actual = createFile(tempDir.resolve("actual"));
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.assertEndsWith(INFO, actual, null));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage("the expected end path should not be null");
  }

  @Test
  void should_rethrow_IOException_as_UncheckedIOException() throws IOException {
    // GIVEN
    Path actual = spy(createFile(tempDir.resolve("actual")));
    Path other = tempDir.resolve("other");
    IOException exception = new IOException("boom!");
    given(actual.toRealPath()).willThrow(exception);
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.assertEndsWith(INFO, actual, other));
    // THEN
    then(thrown).isInstanceOf(UncheckedIOException.class)
                .hasCause(exception);
  }

  @Test
  void should_fail_if_actual_does_not_end_with_other() throws IOException {
    // GIVEN
    Path actual = createFile(tempDir.resolve("actual"));
    Path other = tempDir.resolve("other");
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertEndsWith(INFO, actual, other));
    // THEN
    then(error).hasMessage(shouldEndWith(actual, other).create());
  }

  @Test
  void should_pass_if_actual_ends_with_other() throws IOException {
    // GIVEN
    Path actual = createFile(tempDir.resolve("actual"));
    Path other = Path.of("actual");
    // WHEN/THEN
    underTest.assertEndsWith(INFO, actual, other);
  }

  @Test
  void should_pass_if_actual_is_not_canonical() throws IOException {
    // GIVEN
    Path file = createFile(tempDir.resolve("file"));
    Path actual = tryToCreateSymbolicLink(tempDir.resolve("actual"), file);
    Path other = Path.of("file");
    // WHEN/THEN
    underTest.assertEndsWith(INFO, actual, other);
  }

  @Test
  void should_pass_if_other_is_not_normalized() throws IOException {
    // GIVEN
    Path actual = createFile(tempDir.resolve("actual"));
    Path other = Path.of("actual", "..", "actual", ".");
    // WHEN/THEN
    underTest.assertEndsWith(INFO, actual, other);
  }

  @Test
  void should_pass_on_non_existing_file() {
    // GIVEN
    Path actual = Path.of("foo/bar/baz");
    Path other = Path.of("baz");
    // WHEN/THEN
    underTest.assertEndsWith(INFO, actual, other);
  }
}
