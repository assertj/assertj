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
package org.assertj.core.internal.files;

import static java.lang.String.format;
import static java.nio.file.Files.createDirectory;
import static java.nio.file.Files.createFile;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeDirectory.shouldBeDirectory;
import static org.assertj.core.error.ShouldNotContain.directoryShouldNotContain;
import static org.assertj.core.internal.files.Files_assertIsDirectoryContaining_with_String_Test.mockPathMatcher;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.List;

import org.assertj.core.internal.FilesBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * @author Valeriy Vyrva
 */
class Files_assertIsDirectoryNotContaining_with_String_Test extends FilesBaseTest {

  private static final String JAVA_SOURCE_PATTERN = "regex:.+\\.java";
  private static final String JAVA_SOURCE_PATTERN_DESCRIPTION = format("the '%s' pattern", JAVA_SOURCE_PATTERN);

  @Test
  void should_pass_if_actual_does_not_contain_files_matching_the_given_pathMatcherPattern() {
    // GIVEN
    File file = mockRegularFile("root", "Test.class");
    List<File> items = list(file);
    File actual = mockDirectory(items, "root");
    mockPathMatcher(actual);
    // THEN
    files.assertIsDirectoryNotContaining(INFO, actual, JAVA_SOURCE_PATTERN);
  }

  @Test
  void should_pass_if_actual_is_empty() {
    // GIVEN
    List<File> items = emptyList();
    File actual = mockDirectory(items, "root");
    mockPathMatcher(actual);
    // THEN
    files.assertIsDirectoryNotContaining(INFO, actual, JAVA_SOURCE_PATTERN);
  }

  @Test
  void should_throw_error_if_given_pathMatcherPattern_is_null() {
    // GIVEN
    String pathMatcherPattern = null;
    // THEN
    assertThatNullPointerException().isThrownBy(() -> files.assertIsDirectoryNotContaining(INFO, null, pathMatcherPattern))
                                    .withMessage("The syntax and pattern should not be null");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    File actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> files.assertIsDirectoryNotContaining(INFO, actual, JAVA_SOURCE_PATTERN));
    // THEN
    assertThat(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_exist() {
    // GIVEN
    given(actual.exists()).willReturn(false);
    mockPathMatcher(actual);
    // WHEN
    expectAssertionError(() -> files.assertIsDirectoryNotContaining(INFO, actual, JAVA_SOURCE_PATTERN));
    // THEN
    verify(failures).failure(INFO, shouldBeDirectory(actual));
  }

  @Test
  void should_fail_if_actual_exists_but_is_not_directory() {
    // GIVEN
    given(actual.exists()).willReturn(true);
    given(actual.isDirectory()).willReturn(false);
    mockPathMatcher(actual);
    // WHEN
    expectAssertionError(() -> files.assertIsDirectoryNotContaining(INFO, actual, JAVA_SOURCE_PATTERN));
    // THEN
    verify(failures).failure(INFO, shouldBeDirectory(actual));
  }

  @Test
  void should_throw_error_on_null_listing() {
    // GIVEN
    given(actual.exists()).willReturn(true);
    given(actual.isDirectory()).willReturn(true);
    given(actual.listFiles(any(FileFilter.class))).willReturn(null);
    mockPathMatcher(actual);
    // WHEN
    Throwable error = catchThrowable(() -> files.assertIsDirectoryNotContaining(INFO, actual, JAVA_SOURCE_PATTERN));
    // THEN
    assertThat(error).isInstanceOf(NullPointerException.class)
                     .hasMessage("Directory listing should not be null");
  }

  @Test
  void should_fail_if_one_actual_file_matches_the_filter() {
    // GIVEN
    File file = mockRegularFile("Test.java");
    List<File> items = list(file);
    File actual = mockDirectory(items, "root");
    mockPathMatcher(actual);
    // WHEN
    expectAssertionError(() -> files.assertIsDirectoryNotContaining(INFO, actual, JAVA_SOURCE_PATTERN));
    // THEN
    verify(failures).failure(INFO, directoryShouldNotContain(actual, items, JAVA_SOURCE_PATTERN_DESCRIPTION));
  }

  @Test
  void should_fail_if_all_actual_files_match_the_filter() {
    // GIVEN
    File file1 = mockRegularFile("Test.java");
    File file2 = mockRegularFile("Utils.java");
    List<File> items = list(file1, file2);
    File actual = mockDirectory(items, "root");
    mockPathMatcher(actual);
    // WHEN
    expectAssertionError(() -> files.assertIsDirectoryNotContaining(INFO, actual, JAVA_SOURCE_PATTERN));
    // THEN
    verify(failures).failure(INFO, directoryShouldNotContain(actual, items, JAVA_SOURCE_PATTERN_DESCRIPTION));
  }

  @Test
  void should_fail_if_some_actual_files_match_the_filter() {
    // GIVEN
    File file1 = mockRegularFile("Test.class");
    File file2 = mockRegularFile("Test.java");
    File file3 = mockRegularFile("Utils.class");
    File file4 = mockRegularFile("Utils.java");
    File file5 = mockRegularFile("application.yml");
    List<File> items = list(file1, file2, file3, file4, file5);
    File actual = mockDirectory(items, "root");
    mockPathMatcher(actual);
    // WHEN
    expectAssertionError(() -> files.assertIsDirectoryNotContaining(INFO, actual, JAVA_SOURCE_PATTERN));
    // THEN
    verify(failures).failure(INFO,
                             directoryShouldNotContain(actual, list(file2, file4), JAVA_SOURCE_PATTERN_DESCRIPTION));
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "glob:**file",
      "glob:file",
      "regex:.*file",
      "regex:file",
  })
  void should_fail_if_actual_contains_at_least_one_path_matching_the_given_pattern(String syntaxAndPattern) throws IOException {
    // GIVEN
    File actual = createDirectory(tempDir.resolve("actual")).toFile();
    File file = createFile(actual.toPath().resolve("file")).toFile();
    // WHEN
    AssertionError error = expectAssertionError(() -> files.assertIsDirectoryNotContaining(INFO, actual,
                                                                                           syntaxAndPattern));
    // THEN
    then(error).hasMessage(directoryShouldNotContain(actual, list(file), "the '" + syntaxAndPattern + "' pattern").create());
  }

}
