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
package org.assertj.core.internal.paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldBeDirectory.shouldBeDirectory;
import static org.assertj.core.error.ShouldContain.directoryShouldContain;
import static org.assertj.core.error.ShouldExist.shouldExist;
import static org.assertj.core.internal.Paths.toPathNames;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.emptyList;
import static org.assertj.core.util.Lists.list;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Paths;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Paths#assertIsDirectoryContaining(AssertionInfo, Path, Predicate)}</code>
 *
 * @author Valeriy Vyrva
 */
public class Paths_assertIsDirectoryContaining_Predicate_Test extends MockPathsBaseTest {

  /**
   * We will check count call to {@link Path#getFileName()}
   */
  private static final Predicate<Path> JAVA_SOURCE = path -> Optional.ofNullable(path.getFileName())
                                                                     .map(Path::toString)
                                                                     .filter(pathName -> pathName.endsWith(".java"))
                                                                     .isPresent();

  @Test
  public void should_pass_if_actual_contains_a_file_matching_the_given_predicate() {
    // GIVEN
    Path file = mockRegularFile("Test.java");
    Path actual = mockDirectory("root", list(file));
    // THEN
    paths.assertIsDirectoryContaining(INFO, actual, JAVA_SOURCE);
  }

  @Test
  public void should_pass_if_all_actual_files_match_the_given_predicate() {
    // GIVEN
    Path file1 = mockRegularFile("Test.java");
    Path file2 = mockRegularFile("Utils.java");
    Path actual = mockDirectory("root", list(file1, file2));
    // THEN
    paths.assertIsDirectoryContaining(INFO, actual, JAVA_SOURCE);
  }

  @Test
  public void should_pass_if_actual_contains_at_least_one_file_matching_the_given_predicate() {
    // GIVEN
    Path file1 = mockRegularFile("Test.class");
    Path file2 = mockRegularFile("Test.java");
    Path file3 = mockRegularFile("Utils.class");
    Path file4 = mockRegularFile("Utils.java");
    Path file5 = mockRegularFile("application.yml");
    Path actual = mockDirectory("root", list(file1, file2, file3, file4, file5));
    // THEN
    paths.assertIsDirectoryContaining(INFO, actual, JAVA_SOURCE);
  }

  @Test
  public void should_throw_error_if_filter_is_null() {
    // GIVEN
    Predicate<Path> filter = null;
    // THEN
    assertThatNullPointerException().isThrownBy(() -> paths.assertIsDirectoryContaining(INFO, null, filter))
                                    .withMessage("The paths filter should not be null");
  }

  @Test
  public void should_fail_if_actual_is_null() {
    // GIVEN
    Path actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> paths.assertIsDirectoryContaining(INFO, actual, JAVA_SOURCE));
    // THEN
    assertThat(error).hasMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_actual_does_not_exist() {
    // GIVEN
    given(nioFilesWrapper.exists(actual)).willReturn(false);
    // WHEN
    expectAssertionError(() -> paths.assertIsDirectoryContaining(INFO, actual, JAVA_SOURCE));
    // THEN
    verify(failures).failure(INFO, shouldExist(actual));
  }

  @Test
  public void should_fail_if_actual_exists_but_is_not_directory() {
    // GIVEN
    given(nioFilesWrapper.exists(actual)).willReturn(true);
    given(nioFilesWrapper.isDirectory(actual)).willReturn(false);
    // WHEN
    expectAssertionError(() -> paths.assertIsDirectoryContaining(INFO, actual, JAVA_SOURCE));
    // THEN
    verify(failures).failure(INFO, shouldBeDirectory(actual));
  }

  @Test
  public void should_throw_runtime_error_wrapping_caught_IOException() throws IOException {
    // GIVEN
    IOException cause = new IOException();
    given(nioFilesWrapper.exists(actual)).willReturn(true);
    given(nioFilesWrapper.isDirectory(actual)).willReturn(true);
    given(nioFilesWrapper.newDirectoryStream(eq(actual), any())).willThrow(cause);
    // WHEN
    Throwable error = catchThrowable(() -> paths.assertIsDirectoryContaining(INFO, actual, JAVA_SOURCE));
    // THEN
    assertThat(error).isInstanceOf(UncheckedIOException.class)
                     .hasCause(cause);
  }

  @Test
  public void should_fail_if_actual_is_empty() {
    // GIVEN
    List<Path> emptyList = emptyList();
    Path actual = mockDirectory("root", emptyList);
    // WHEN
    expectAssertionError(() -> paths.assertIsDirectoryContaining(INFO, actual, JAVA_SOURCE));
    // THEN
    verify(failures).failure(INFO, directoryShouldContain(actual, emptyList(), "the given filter"));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_any_files_matching_the_given_predicate() {
    // GIVEN
    Path file = mockRegularFile("root", "Test.class");
    List<Path> files = list(file);
    Path actual = mockDirectory("root", files);
    // WHEN
    expectAssertionError(() -> paths.assertIsDirectoryContaining(INFO, actual, JAVA_SOURCE));
    // THEN
    verify(failures).failure(INFO, directoryShouldContain(actual, toPathNames(files), "the given filter"));
  }

}
