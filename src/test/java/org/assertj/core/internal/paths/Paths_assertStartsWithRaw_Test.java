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
package org.assertj.core.internal.paths;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldStartWithPath.shouldStartWith;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.assertj.core.api.Assertions;
import org.assertj.core.internal.PathsBaseTest;
import org.junit.jupiter.api.Test;

class Paths_assertStartsWithRaw_Test extends PathsBaseTest {

  @Test
  void should_fail_if_actual_is_null() throws IOException {
    // GIVEN
    Path other = Files.createFile(tempDir.resolve("other"));
    // WHEN
    AssertionError error = expectAssertionError(() -> paths.assertStartsWithRaw(info, null, other));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_other_is_null() throws IOException {
    // GIVEN
    Path actual = Files.createFile(tempDir.resolve("actual"));
    // WHEN
    Throwable thrown = Assertions.catchThrowable(() -> paths.assertStartsWithRaw(info, actual, null));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage("the expected start path should not be null");
  }

  @Test
  void should_fail_if_actual_does_not_start_with_other() throws IOException {
    // GIVEN
    Path actual = Files.createFile(tempDir.resolve("actual"));
    Path other = Files.createFile(tempDir.resolve("other"));
    // WHEN
    AssertionError error = expectAssertionError(() -> paths.assertStartsWithRaw(info, actual, other));
    // THEN
    then(error).hasMessage(shouldStartWith(actual, other).create());
  }

  @Test
  void should_pass_if_actual_starts_with_other() throws IOException {
    // GIVEN
    Path other = Files.createDirectory(tempDir.resolve("other")).toRealPath();
    Path actual = Files.createFile(other.resolve("actual")).toRealPath();
    // WHEN/THEN
    paths.assertStartsWithRaw(info, actual, other);
  }

  @Test
  void should_fail_if_actual_is_not_canonical() throws IOException {
    // GIVEN
    Path other = Files.createDirectory(tempDir.resolve("other"));
    Path file = Files.createFile(other.resolve("file"));
    Path actual = Files.createSymbolicLink(tempDir.resolve("actual"), file);
    // WHEN
    AssertionError error = expectAssertionError(() -> paths.assertStartsWithRaw(info, actual, other));
    // THEN
    then(error).hasMessage(shouldStartWith(actual, other).create());
  }

  @Test
  void should_fail_if_other_is_not_canonical() throws IOException {
    // GIVEN
    Path directory = Files.createDirectory(tempDir.resolve("directory"));
    Path other = Files.createSymbolicLink(tempDir.resolve("other"), directory);
    Path actual = Files.createFile(directory.resolve("actual"));
    // WHEN
    AssertionError error = expectAssertionError(() -> paths.assertStartsWithRaw(info, actual, other));
    // THEN
    then(error).hasMessage(shouldStartWith(actual, other).create());
  }

}
