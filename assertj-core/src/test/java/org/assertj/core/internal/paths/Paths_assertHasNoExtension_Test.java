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
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeRegularFile.shouldBeRegularFile;
import static org.assertj.core.error.ShouldExist.shouldExist;
import static org.assertj.core.error.ShouldHaveNoExtension.shouldHaveNoExtension;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.io.IOException;
import java.nio.file.Path;

import org.assertj.core.internal.PathsBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class Paths_assertHasNoExtension_Test extends PathsBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Path actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertHasNoExtension(INFO, actual));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_exist() {
    // GIVEN
    Path actual = tempDir.resolve("non-existent");
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertHasNoExtension(INFO, actual));
    // THEN
    then(error).hasMessage(shouldExist(actual).create());
  }

  @Test
  void should_fail_if_actual_is_not_a_regular_file() throws IOException {
    // GIVEN
    Path actual = createDirectory(tempDir.resolve("directory"));
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertHasNoExtension(INFO, actual));
    // THEN
    then(error).hasMessage(shouldBeRegularFile(actual).create());
  }

  @Test
  void should_fail_if_actual_has_extension() throws IOException {
    // GIVEN
    Path actual = createFile(tempDir.resolve("file.txt"));
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertHasNoExtension(INFO, actual));
    // THEN
    then(error).hasMessage(shouldHaveNoExtension(actual, "txt").create());
  }

  @ParameterizedTest
  @ValueSource(strings = { "file", "file." })
  void should_pass_if_actual_has_no_extension(String filename) throws IOException {
    // GIVEN
    Path actual = createFile(tempDir.resolve(filename));
    // WHEN/THEN
    underTest.assertHasNoExtension(INFO, actual);
  }

}
