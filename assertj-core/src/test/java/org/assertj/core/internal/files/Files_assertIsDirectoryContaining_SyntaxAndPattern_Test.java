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
package org.assertj.core.internal.files;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeDirectory.shouldBeDirectory;
import static org.assertj.core.error.ShouldContain.directoryShouldContain;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Files.newFile;
import static org.assertj.core.util.Files.newFolder;
import static org.assertj.core.util.Lists.list;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.FileFilter;
import java.util.List;
import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Files;
import org.assertj.core.internal.FilesBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Files#assertIsDirectoryContaining(AssertionInfo, File, String)}</code>
 *
 * @author Valeriy Vyrva
 */
class Files_assertIsDirectoryContaining_SyntaxAndPattern_Test extends FilesBaseTest {

  private static final String JAVA_SOURCE_PATTERN = "regex:.+\\.java";
  private static final String JAVA_SOURCE_PATTERN_DESCRIPTION = format("the '%s' pattern", JAVA_SOURCE_PATTERN);

  @Test
  void should_pass_if_actual_contains_a_file_matching_the_given_pathMatcherPattern() {
    // GIVEN
    File actual = newFolder(tempDir.getAbsolutePath() + "/folder");
    newFile(actual.getAbsolutePath() + "/Test.java");
    // WHEN/THEN
    underTest.assertIsDirectoryContaining(INFO, actual, JAVA_SOURCE_PATTERN);
  }

  @Test
  void should_pass_if_all_actual_files_match_the_given_pathMatcherPattern() {
    // GIVEN
    File actual = newFolder(tempDir.getAbsolutePath() + "/folder");
    newFile(actual.getAbsolutePath() + "/Test.java");
    newFile(actual.getAbsolutePath() + "/Utils.java");
    // WHEN/THEN
    underTest.assertIsDirectoryContaining(INFO, actual, JAVA_SOURCE_PATTERN);
  }

  @Test
  void should_pass_if_actual_contains_some_files_matching_the_given_pathMatcherPattern() {
    // GIVEN
    File actual = newFolder(tempDir.getAbsolutePath() + "/folder");
    newFile(actual.getAbsolutePath() + "/Test.java");
    newFile(actual.getAbsolutePath() + "/Test.class");
    newFile(actual.getAbsolutePath() + "/Utils.class");
    newFile(actual.getAbsolutePath() + "/Utils.java");
    newFile(actual.getAbsolutePath() + "/application.yml");
    // WHEN/THEN
    underTest.assertIsDirectoryContaining(INFO, actual, JAVA_SOURCE_PATTERN);
  }

  @Test
  void should_throw_error_if_pathMatcherPattern_is_null() {
    // GIVEN
    String pathMatcherPattern = null;
    // THEN
    assertThatNullPointerException().isThrownBy(() -> underTest.assertIsDirectoryContaining(INFO, null, pathMatcherPattern))
                                    .withMessage("The syntax and pattern should not be null");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    File actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertIsDirectoryContaining(INFO, actual, JAVA_SOURCE_PATTERN));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_exist() {
    // GIVEN
    File actual = new File("xyz");
    // WHEN
    expectAssertionError(() -> underTest.assertIsDirectoryContaining(INFO, actual, JAVA_SOURCE_PATTERN));
    // THEN
    verify(failures).failure(INFO, shouldBeDirectory(actual));
  }

  @Test
  void should_fail_if_actual_exists_but_is_not_a_directory() {
    // GIVEN
    File actual = newFile(tempDir.getAbsolutePath() + "/Test.java");
    // WHEN
    expectAssertionError(() -> underTest.assertIsDirectoryContaining(INFO, actual, JAVA_SOURCE_PATTERN));
    // THEN
    verify(failures).failure(INFO, shouldBeDirectory(actual));
  }

  // use mock as it's hard to simulate listFiles(FileFilter.class) to return null
  @Test
  void should_throw_error_on_null_listing() {
    // GIVEN
    File actual = mock(File.class);
    given(actual.exists()).willReturn(true);
    given(actual.isDirectory()).willReturn(true);
    given(actual.listFiles(any(FileFilter.class))).willReturn(null);
    mockPathMatcher(actual);
    // WHEN
    Throwable error = catchThrowable(() -> underTest.assertIsDirectoryContaining(INFO, actual, JAVA_SOURCE_PATTERN));
    // THEN
    then(error).isInstanceOf(NullPointerException.class)
               .hasMessage("Directory listing should not be null");
  }

  @Test
  void should_fail_if_actual_is_empty() {
    // GIVEN
    File actual = newFolder(tempDir.getAbsolutePath() + "/folder");
    // WHEN
    expectAssertionError(() -> underTest.assertIsDirectoryContaining(INFO, actual, JAVA_SOURCE_PATTERN));
    // THEN
    verify(failures).failure(INFO, directoryShouldContain(actual, emptyList(), JAVA_SOURCE_PATTERN_DESCRIPTION));
  }

  @Test
  void should_fail_if_actual_does_not_contain_any_files_matching_the_given_pathMatcherPattern() {
    // GIVEN
    File actual = newFolder(tempDir.getAbsolutePath() + "/folder");
    File file = newFile(actual.getAbsolutePath() + "/Test.class");
    List<File> items = list(file);
    // WHEN
    expectAssertionError(() -> underTest.assertIsDirectoryContaining(INFO, actual, JAVA_SOURCE_PATTERN));
    // THEN
    verify(failures).failure(INFO, directoryShouldContain(actual, items, JAVA_SOURCE_PATTERN_DESCRIPTION));
  }

}
