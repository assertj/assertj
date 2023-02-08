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

import static java.nio.file.Files.createFile;
import static java.nio.file.Files.createSymbolicLink;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldEndWithPath.shouldEndWith;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.assertj.core.internal.PathsBaseTest;
import org.junit.jupiter.api.Test;

class Paths_assertEndsWithRaw_Test extends PathsBaseTest {

  @Test
  void should_fail_if_actual_is_null() throws IOException {
    // GIVEN
    Path other = tempDir.resolve("other");
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertEndsWithRaw(INFO, null, other));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_other_is_null() throws IOException {
    // GIVEN
    Path actual = createFile(tempDir.resolve("actual"));
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.assertEndsWithRaw(INFO, actual, null));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage("the expected end path should not be null");
  }

  @Test
  void should_fail_if_actual_does_not_end_with_other() throws IOException {
    // GIVEN
    Path actual = createFile(tempDir.resolve("actual"));
    Path other = tempDir.resolve("other");
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertEndsWithRaw(INFO, actual, other));
    // THEN
    then(error).hasMessage(shouldEndWith(actual, other).create());
  }

  @Test
  void should_pass_if_actual_ends_with_other() throws IOException {
    // GIVEN
    Path actual = createFile(tempDir.resolve("actual"));
    Path other = Paths.get("actual");
    // WHEN/THEN
    underTest.assertEndsWithRaw(INFO, actual, other);
  }

  @Test
  void should_fail_if_actual_is_not_canonical() throws IOException {
    // GIVEN
    Path file = createFile(tempDir.resolve("file"));
    Path actual = createSymbolicLink(tempDir.resolve("actual"), file);
    Path other = Paths.get("file");
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertEndsWithRaw(INFO, actual, other));
    // THEN
    then(error).hasMessage(shouldEndWith(actual, other).create());
  }

  @Test
  void should_fail_if_other_is_not_normalized() throws IOException {
    // GIVEN
    Path actual = createFile(tempDir.resolve("actual"));
    Path other = Paths.get("actual", "..", "actual", ".");
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertEndsWithRaw(INFO, actual, other));
    // THEN
    then(error).hasMessage(shouldEndWith(actual, other).create());
  }

}
