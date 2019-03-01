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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.internal.files;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldBeDirectory.shouldBeDirectory;
import static org.assertj.core.error.ShouldContain.directoryShouldContain;
import static org.assertj.core.internal.Files.toFileNames;
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
import java.util.function.Predicate;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Files;
import org.assertj.core.internal.FilesBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Files#assertIsDirectoryContaining(AssertionInfo, File, Predicate)}</code>
 *
 * @author Valeriy Vyrva
 */
public class Files_assertIsDirectoryContaining_Predicate_Test extends FilesBaseTest {

  private static final Predicate<File> JAVA_SOURCE = file -> file.getName().endsWith(".java");

  @Test
  public void should_pass_if_actual_contains_a_file_matching_the_given_predicate() {
    // GIVEN
    File file = mockRegularFile("Test.java");
    List<File> items = list(file);
    // WHEN
    File actual = mockDirectory(items, "root");
    // THEN
    files.assertIsDirectoryContaining(INFO, actual, JAVA_SOURCE);
  }

  @Test
  public void should_pass_if_all_actual_files_match_the_given_predicate() {
    // GIVEN
    File file1 = mockRegularFile("Test.java");
    File file2 = mockRegularFile("Utils.java");
    List<File> items = list(file1, file2);
    // WHEN
    File actual = mockDirectory(items, "root");
    // THEN
    files.assertIsDirectoryContaining(INFO, actual, JAVA_SOURCE);
  }

  @Test
  public void should_pass_if_actual_contains_at_least_one_file_matching_the_given_predicate() {
    // GIVEN
    File file1 = mockRegularFile("Test.class");
    File file2 = mockRegularFile("Test.java");
    File file3 = mockRegularFile("Utils.class");
    File file4 = mockRegularFile("Utils.java");
    File file5 = mockRegularFile("application.yml");
    List<File> items = list(file1, file2, file3, file4, file5);
    // WHEN
    File actual = mockDirectory(items, "root");
    // THEN
    files.assertIsDirectoryContaining(INFO, actual, JAVA_SOURCE);
  }

  @Test
  public void should_throw_npe_if_filter_is_null() {
    // GIVEN
    Predicate<File> filter = null;
    // THEN
    assertThatNullPointerException().isThrownBy(() -> files.assertIsDirectoryContaining(INFO, null, filter))
                                    .withMessage("The files filter should not be null");
  }

  @Test
  public void should_fail_if_actual_is_null() {
    // GIVEN
    File actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> files.assertIsDirectoryContaining(INFO, actual, JAVA_SOURCE));
    // THEN
    assertThat(error).hasMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_actual_does_not_exist() {
    // GIVEN
    given(actual.exists()).willReturn(false);
    // WHEN
    expectAssertionError(() -> files.assertIsDirectoryContaining(INFO, actual, JAVA_SOURCE));
    // THEN
    verify(failures).failure(INFO, shouldBeDirectory(actual));
  }

  @Test
  public void should_fail_if_actual_exists_but_is_not_a_directory() {
    // GIVEN
    given(actual.exists()).willReturn(true);
    given(actual.isDirectory()).willReturn(false);
    // WHEN
    expectAssertionError(() -> files.assertIsDirectoryContaining(INFO, actual, JAVA_SOURCE));
    // THEN
    verify(failures).failure(INFO, shouldBeDirectory(actual));
  }

  @Test
  public void should_throw_error_on_null_directory_listing() {
    // GIVEN
    given(actual.exists()).willReturn(true);
    given(actual.isDirectory()).willReturn(true);
    given(actual.listFiles(any(FileFilter.class))).willReturn(null);
    // WHEN
    Throwable error = catchThrowable(() -> files.assertIsDirectoryContaining(INFO, actual, JAVA_SOURCE));
    // THEN
    assertThat(error).isInstanceOf(NullPointerException.class)
                     .hasMessage("Directory listing should not be null");
  }

  @Test
  public void should_fail_if_actual_is_empty() {
    // GIVEN
    List<File> items = emptyList();
    File actual = mockDirectory(items, "root");
    // WHEN
    expectAssertionError(() -> files.assertIsDirectoryContaining(INFO, actual, JAVA_SOURCE));
    // THEN
    verify(failures).failure(INFO, directoryShouldContain(actual, emptyList(), "the given filter"));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_any_files_matching_the_given_predicate() {
    // GIVEN
    File file = mockRegularFile("root", "Test.class");
    List<File> items = list(file);
    File actual = mockDirectory(items, "root");
    // WHEN
    expectAssertionError(() -> files.assertIsDirectoryContaining(INFO, actual, JAVA_SOURCE));
    // THEN
    verify(failures).failure(INFO, directoryShouldContain(actual, toFileNames(items), "the given filter"));
  }
}
