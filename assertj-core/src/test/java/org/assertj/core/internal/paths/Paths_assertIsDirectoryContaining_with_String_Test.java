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
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeDirectory.shouldBeDirectory;
import static org.assertj.core.error.ShouldContain.directoryShouldContain;
import static org.assertj.core.error.ShouldExist.shouldExist;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willThrow;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

import org.assertj.core.internal.PathsBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * @author Valeriy Vyrva
 */
class Paths_assertIsDirectoryContaining_with_String_Test extends PathsBaseTest {

  @Test
  void should_fail_if_syntaxAndPattern_is_null() throws IOException {
    // GIVEN
    Path actual = createDirectory(tempDir.resolve("actual"));
    String syntaxAndPattern = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.assertIsDirectoryContaining(INFO, actual, syntaxAndPattern));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage("The syntax and pattern should not be null");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Path actual = null;
    String syntaxAndPattern = "glob:**";
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertIsDirectoryContaining(INFO, actual, syntaxAndPattern));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_exist() {
    // GIVEN
    Path actual = tempDir.resolve("non-existent");
    String syntaxAndPattern = "glob:**";
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertIsDirectoryContaining(INFO, actual, syntaxAndPattern));
    // THEN
    then(error).hasMessage(shouldExist(actual).create());
  }

  @Test
  void should_fail_if_actual_is_not_a_directory() throws IOException {
    // GIVEN
    Path actual = createFile(tempDir.resolve("file"));
    String syntaxAndPattern = "glob:**";
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertIsDirectoryContaining(INFO, actual, syntaxAndPattern));
    // THEN
    then(error).hasMessage(shouldBeDirectory(actual).create());
  }

  @Test
  void should_rethrow_IOException_as_UncheckedIOException() throws Exception {
    // GIVEN
    Path actual = createDirectory(tempDir.resolve("actual"));
    String syntaxAndPattern = "glob:*";
    IOException cause = new IOException("boom!");
    willThrow(cause).given(nioFilesWrapper).newDirectoryStream(any(), any());
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.assertIsDirectoryContaining(INFO, actual, syntaxAndPattern));
    // THEN
    then(thrown).isInstanceOf(UncheckedIOException.class)
                .hasCause(cause);
  }

  @Test
  void should_fail_if_actual_is_empty() throws IOException {
    // GIVEN
    Path actual = createDirectory(tempDir.resolve("actual"));
    String syntaxAndPattern = "glob:**";
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertIsDirectoryContaining(INFO, actual, syntaxAndPattern));
    // THEN
    then(error).hasMessage(directoryShouldContain(actual, emptyList(), "the 'glob:**' pattern").create());
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "glob:**file",
//    "glob:file",    // fails due to gh-2329
      "regex:.*file",
//    "regex:file",   // fails due to gh-2329
  })
  void should_pass_if_actual_directly_contains_any_entries_matching_the_given_pattern(String syntaxAndPattern) throws IOException {
    // GIVEN
    Path actual = createDirectory(tempDir.resolve("actual"));
    createDirectory(actual.resolve("directory"));
    createFile(actual.resolve("file"));
    // WHEN/THEN
    underTest.assertIsDirectoryContaining(INFO, actual, syntaxAndPattern);
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "glob:**file",
      "glob:file",
      "regex:.*file",
      "regex:file",
  })
  void should_fail_if_actual_does_not_contain_any_entries_matching_the_given_pattern(String syntaxAndPattern) throws IOException {
    // GIVEN
    Path actual = createDirectory(tempDir.resolve("actual"));
    Path directory = createDirectory(actual.resolve("directory"));
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertIsDirectoryContaining(INFO, actual, syntaxAndPattern));
    // THEN
    then(error).hasMessage(directoryShouldContain(actual, list(directory), "the '" + syntaxAndPattern + "' pattern").create());
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "glob:**file",
      "glob:file",
      "regex:.*file",
      "regex:file",
  })
  void should_fail_if_actual_recursively_contains_any_entries_matching_the_given_pattern(String syntaxAndPattern) throws IOException {
    // GIVEN
    Path actual = createDirectory(tempDir.resolve("actual"));
    Path directory = createDirectory(actual.resolve("directory"));
    createFile(directory.resolve("file"));
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertIsDirectoryContaining(INFO, actual, syntaxAndPattern));
    // THEN
    then(error).hasMessage(directoryShouldContain(actual, list(directory), "the '" + syntaxAndPattern + "' pattern").create());
  }

}
