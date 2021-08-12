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

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeDirectory.shouldBeDirectory;
import static org.assertj.core.error.ShouldContain.directoryShouldContain;
import static org.assertj.core.error.ShouldExist.shouldExist;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.emptyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.assertj.core.internal.PathsBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Valeriy Vyrva
 */
class Paths_assertIsDirectoryContaining_with_String_Test extends PathsBaseTest {

  @BeforeEach
  void setUpNioFilesWrapper() throws IOException {
    given(nioFilesWrapper.newDirectoryStream(any(), any())).willCallRealMethod();
  }

  @Test
  void should_fail_if_syntaxAndPattern_is_null() throws IOException {
    // GIVEN
    Path actual = Files.createDirectory(tempDir.resolve("actual"));
    String syntaxAndPattern = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> paths.assertIsDirectoryContaining(info, actual, syntaxAndPattern));
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
    AssertionError error = expectAssertionError(() -> paths.assertIsDirectoryContaining(info, actual, syntaxAndPattern));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_exist() {
    // GIVEN
    Path actual = tempDir.resolve("non-existent");
    String syntaxAndPattern = "glob:**";
    // WHEN
    AssertionError error = expectAssertionError(() -> paths.assertIsDirectoryContaining(info, actual, syntaxAndPattern));
    // THEN
    then(error).hasMessage(shouldExist(actual).create());
  }

  @Test
  void should_fail_if_actual_is_not_a_directory() throws IOException {
    // GIVEN
    Path actual = Files.createFile(tempDir.resolve("file"));
    String syntaxAndPattern = "glob:**";
    // WHEN
    AssertionError error = expectAssertionError(() -> paths.assertIsDirectoryContaining(info, actual, syntaxAndPattern));
    // THEN
    then(error).hasMessage(shouldBeDirectory(actual).create());
  }

  @Test
  void should_rethrow_IOException_as_UncheckedIOException() throws Exception {
    // GIVEN
    Path actual = Files.createDirectory(tempDir.resolve("actual"));
    String syntaxAndPattern = "glob:*";
    IOException cause = new IOException("boom!");
    willThrow(cause).given(nioFilesWrapper).newDirectoryStream(any(), any());
    // WHEN
    Throwable thrown = catchThrowable(() -> paths.assertIsDirectoryContaining(info, actual, syntaxAndPattern));
    // THEN
    then(thrown).isInstanceOf(UncheckedIOException.class)
                .hasCause(cause);
  }

  @Test
  void should_fail_if_actual_is_empty() throws IOException {
    // GIVEN
    Path actual = Files.createDirectory(tempDir.resolve("actual"));
    String syntaxAndPattern = "glob:**";
    // WHEN
    AssertionError error = expectAssertionError(() -> paths.assertIsDirectoryContaining(info, actual, syntaxAndPattern));
    // THEN
    then(error).hasMessage(directoryShouldContain(actual, emptyList(), "the 'glob:**' pattern").create());
  }

  @Test
  void should_pass_if_actual_contains_at_least_one_path_matching_the_given_pattern() throws IOException {
    // GIVEN
    Path actual = Files.createDirectory(tempDir.resolve("actual"));
    Files.createFile(actual.resolve("file"));
    Files.createDirectory(actual.resolve("directory"));
    String syntaxAndPattern = "glob:**file";
    // WHEN/THEN
    paths.assertIsDirectoryContaining(info, actual, syntaxAndPattern);
  }

  @Test
  void should_fail_if_actual_does_not_contain_any_paths_matching_the_given_pattern() throws IOException {
    // GIVEN
    Path actual = Files.createDirectory(tempDir.resolve("actual"));
    Path directory = Files.createDirectory(actual.resolve("directory"));
    String syntaxAndPattern = "glob:**file";
    // WHEN
    AssertionError error = expectAssertionError(() -> paths.assertIsDirectoryContaining(info, actual, syntaxAndPattern));
    // THEN
    then(error).hasMessage(directoryShouldContain(actual, singletonList(directory.toString()),
                                                  "the 'glob:**file' pattern").create());
  }

}
