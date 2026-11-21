/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.api.path;

import static java.nio.file.Files.createDirectory;
import static java.nio.file.Files.createFile;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeDirectory.shouldBeDirectory;
import static org.assertj.core.error.ShouldExist.shouldExist;
import static org.assertj.core.error.ShouldNotContain.directoryShouldNotContain;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PathAssert_isDirectoryNotContaining_SyntaxAndPattern_Test {

  @TempDir
  Path tempDir;

  @Test
  void should_fail_if_syntaxAndPattern_is_null() throws IOException {
    // GIVEN
    Path actual = createDirectory(tempDir.resolve("actual"));
    String syntaxAndPattern = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).isDirectoryNotContaining(syntaxAndPattern));
    // THEN
    then(npe).hasMessage("The syntax and pattern should not be null");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Path actual = null;
    String syntaxAndPattern = "glob:**";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isDirectoryNotContaining(syntaxAndPattern));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_exist() {
    // GIVEN
    Path actual = tempDir.resolve("non-existent");
    String syntaxAndPattern = "glob:**";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isDirectoryNotContaining(syntaxAndPattern));
    // THEN
    then(assertionError).hasMessage(shouldExist(actual).create());
  }

  @Test
  void should_fail_if_actual_is_not_a_directory() throws IOException {
    // GIVEN
    Path actual = createFile(tempDir.resolve("file"));
    String syntaxAndPattern = "glob:**";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isDirectoryNotContaining(syntaxAndPattern));
    // THEN
    then(assertionError).hasMessage(shouldBeDirectory(actual).create());
  }

  @Test
  void should_pass_if_actual_is_empty() throws IOException {
    // GIVEN
    Path actual = createDirectory(tempDir.resolve("actual"));
    String syntaxAndPattern = "glob:**";
    // WHEN/THEN
    then(actual).isDirectoryNotContaining(syntaxAndPattern);
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "glob:**file",
      // "glob:file", // fails due to gh-2329
      "regex:.*file",
  // "regex:file", // fails due to gh-2329
  })
  void should_fail_if_actual_directly_contains_any_entries_matching_the_given_pattern(String syntaxAndPattern) throws IOException {
    // GIVEN
    Path actual = createDirectory(tempDir.resolve("actual"));
    Path file = createFile(actual.resolve("file"));
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isDirectoryNotContaining(syntaxAndPattern));
    // THEN
    then(assertionError).hasMessage(directoryShouldNotContain(actual, singletonList(actual.relativize(file)),
                                                              "the '" + syntaxAndPattern + "' pattern").create());
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "glob:**file",
      "glob:file",
      "regex:.*file",
      "regex:file",
  })
  void should_pass_if_actual_does_not_contain_any_entries_matching_the_given_pattern(String syntaxAndPattern) throws IOException {
    // GIVEN
    Path actual = createDirectory(tempDir.resolve("actual"));
    createDirectory(actual.resolve("directory"));
    // WHEN/THEN
    then(actual).isDirectoryNotContaining(syntaxAndPattern);
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "glob:**file",
      "glob:file",
      "regex:.*file",
      "regex:file",
  })
  void should_pass_if_actual_recursively_contains_any_entries_matching_the_given_pattern(String syntaxAndPattern) throws IOException {
    // GIVEN
    Path actual = createDirectory(tempDir.resolve("actual"));
    Path directory = createDirectory(actual.resolve("directory"));
    createFile(directory.resolve("file"));
    // WHEN/THEN
    then(actual).isDirectoryNotContaining(syntaxAndPattern);
  }

}
