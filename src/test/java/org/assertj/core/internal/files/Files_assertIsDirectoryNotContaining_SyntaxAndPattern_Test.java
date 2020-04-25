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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.internal.files;

import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldBeDirectory.shouldBeDirectory;
import static org.assertj.core.error.ShouldNotContain.directoryShouldNotContain;
import static org.assertj.core.internal.Files.toFileNames;
import static org.assertj.core.internal.files.Files_assertIsDirectoryContaining_SyntaxAndPattern_Test.mockPathMatcher;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.emptyList;
import static org.assertj.core.util.Lists.list;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Files;
import org.assertj.core.internal.FilesBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Files#assertIsDirectoryNotContaining(AssertionInfo, File, String)}</code>
 *
 * @author Valeriy Vyrva
 */
public class Files_assertIsDirectoryNotContaining_SyntaxAndPattern_Test extends FilesBaseTest {

  private static final String JAVA_SOURCE_PATTERN = "regex:.+\\.java";
  private static final String JAVA_SOURCE_PATTERN_DESCRIPTION = format("the '%s' pattern", JAVA_SOURCE_PATTERN);

  @Test
  public void should_pass_if_actual_does_not_contain_files_matching_the_given_pathMatcherPattern() {
    // GIVEN
    File file = mockRegularFile("root", "Test.class");
    List<File> items = singletonList(file);
    File actual = mockDirectory(items, "root");
    mockPathMatcher(actual);
    // THEN
    files.assertIsDirectoryNotContaining(INFO, actual, JAVA_SOURCE_PATTERN);
  }

  @Test
  public void should_pass_if_actual_is_empty() {
    // GIVEN
    List<File> items = emptyList();
    File actual = mockDirectory(items, "root");
    mockPathMatcher(actual);
    // THEN
    files.assertIsDirectoryNotContaining(INFO, actual, JAVA_SOURCE_PATTERN);
  }

  @Test
  public void should_throw_error_if_given_pathMatcherPattern_is_null() {
    // GIVEN
    String pathMatcherPattern = null;
    // THEN
    assertThatNullPointerException().isThrownBy(() -> files.assertIsDirectoryNotContaining(INFO, null, pathMatcherPattern))
                                    .withMessage("The syntax and pattern should not be null");
  }

  @Test
  public void should_fail_if_actual_is_null() {
    // GIVEN
    File actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> files.assertIsDirectoryNotContaining(INFO, actual, JAVA_SOURCE_PATTERN));
    // THEN
    assertThat(error).hasMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_actual_does_not_exist() {
    // GIVEN
    given(actual.exists()).willReturn(false);
    mockPathMatcher(actual);
    // WHEN
    expectAssertionError(() -> files.assertIsDirectoryNotContaining(INFO, actual, JAVA_SOURCE_PATTERN));
    // THEN
    verify(failures).failure(INFO, shouldBeDirectory(actual));
  }

  @Test
  public void should_fail_if_actual_exists_but_is_not_directory() {
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
  public void should_throw_error_on_null_listing() {
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
  public void should_fail_if_one_actual_file_matches_the_filter() {
    // GIVEN
    File file = mockRegularFile("Test.java");
    List<File> items = list(file);
    File actual = mockDirectory(items, "root");
    mockPathMatcher(actual);
    // WHEN
    expectAssertionError(() -> files.assertIsDirectoryNotContaining(INFO, actual, JAVA_SOURCE_PATTERN));
    // THEN
    verify(failures).failure(INFO, directoryShouldNotContain(actual, toFileNames(items), JAVA_SOURCE_PATTERN_DESCRIPTION));
  }

  @Test
  public void should_fail_if_all_actual_files_match_the_filter() {
    // GIVEN
    File file1 = mockRegularFile("Test.java");
    File file2 = mockRegularFile("Utils.java");
    List<File> items = list(file1, file2);
    File actual = mockDirectory(items, "root");
    mockPathMatcher(actual);
    // WHEN
    expectAssertionError(() -> files.assertIsDirectoryNotContaining(INFO, actual, JAVA_SOURCE_PATTERN));
    // THEN
    verify(failures).failure(INFO, directoryShouldNotContain(actual, toFileNames(items), JAVA_SOURCE_PATTERN_DESCRIPTION));
  }

  @Test
  public void should_fail_if_some_actual_files_match_the_filter() {
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
                             directoryShouldNotContain(actual, toFileNames(list(file2, file4)), JAVA_SOURCE_PATTERN_DESCRIPTION));
  }

}
