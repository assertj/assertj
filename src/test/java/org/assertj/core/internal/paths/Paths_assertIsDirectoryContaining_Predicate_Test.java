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
package org.assertj.core.internal.paths;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Paths;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldBeDirectory.shouldBeDirectory;
import static org.assertj.core.error.ShouldContainAnyOf.shouldContainAnyOf;
import static org.assertj.core.error.ShouldExist.shouldExist;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.emptyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link Paths#assertIsDirectoryContaining(AssertionInfo, Path, Predicate)}</code>
 *
 * @author Valeriy Vyrva
 */
@SuppressWarnings({"WeakerAccess", "ThrowableNotThrown"})
public class Paths_assertIsDirectoryContaining_Predicate_Test extends MockPathsBaseTest {

  /**
   * We will check count call to {@link Path#getFileName()}
   */
  private static final Predicate<Path> javaSource = path -> Optional
    .ofNullable(path.getFileName())
    .map(Path::toString)
    .filter(s -> s.endsWith(".java"))
    .isPresent();

  @Test
  public void should_throw_error_if_filter_is_null() {
    assertThatNullPointerException()
      .isThrownBy(() -> paths.assertIsDirectoryContaining(INFO, null, (Predicate<Path>) null))
      .withMessage("The files filter should not be null");
  }

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class)
      .isThrownBy(() -> paths.assertIsDirectoryContaining(info, null, javaSource))
      .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_with_should_exist_error_if_actual_does_not_exist() {
    // GIVEN
    given(nioFilesWrapper.exists(actual)).willReturn(false);
    // WHEN
    Throwable error = catchThrowable(() ->
      paths.assertIsDirectoryContaining(INFO, actual, javaSource)
    );
    // THEN
    verify(failures).failure(INFO, shouldExist(actual));
    assertThat(error).isInstanceOf(AssertionError.class);
  }

  @Test
  public void should_fail_if_actual_exists_but_is_not_directory() {
    // GIVEN
    given(nioFilesWrapper.exists(actual)).willReturn(true);
    given(nioFilesWrapper.isDirectory(actual)).willReturn(false);
    // WHEN
    Throwable error = catchThrowable(() ->
      paths.assertIsDirectoryContaining(INFO, actual, javaSource)
    );
    // THEN
    verify(failures).failure(INFO, shouldBeDirectory(actual));
    assertThat(error).isInstanceOf(AssertionError.class);
  }

  @Test
  public void should_throw_error_wrapping_catched_IOException() throws IOException {
    // GIVEN
    IOException cause = new IOException();
    given(nioFilesWrapper.exists(actual)).willReturn(true);
    given(nioFilesWrapper.isDirectory(actual)).willReturn(true);
    given(nioFilesWrapper.newDirectoryStream(eq(actual), any())).willThrow(cause);
    // WHEN
    Throwable error = catchThrowable(() ->
      paths.assertIsDirectoryContaining(INFO, actual, javaSource)
    );
    // THEN
    assertThat(error)
      .isInstanceOf(UncheckedIOException.class)
      .hasCause(cause);
  }

  @Test
  public void should_fail_if_actual_is_empty() {
    // GIVEN
    List<Path> items = emptyList();
    DirectoryStream<Path> stream = directoryStream(items);
    Path actual = mockDirectory(stream, "root");
    // WHEN
    Throwable error = catchThrowable(() ->
      paths.assertIsDirectoryContaining(INFO, actual, javaSource)
    );
    // THEN
    assertThat(error)
      .isInstanceOf(AssertionError.class)
      .hasMessageContaining("[]");
    verify(failures).failure(INFO, shouldContainAnyOf(items, javaSource));
    failIfStreamIsOpen(stream);
  }

  @Test
  public void should_fail_if_actual_does_not_contains_expected() {
    // GIVEN
    Path file = mockRegularFile("root", "Test.class");
    List<Path> items = singletonList(file);
    DirectoryStream<Path> stream = directoryStream(items);
    Path actual = mockDirectory(stream, "root");
    // WHEN
    Throwable error = catchThrowable(() ->
      paths.assertIsDirectoryContaining(INFO, actual, javaSource)
    );
    // THEN
    assertThat(error)
      .isInstanceOf(AssertionError.class)
      .hasMessageContaining("[" + file.toString() + "]");
    verify(failures).failure(INFO, shouldContainAnyOf(items, javaSource));
    verify(file, times(1)).getFileName();
    failIfStreamIsOpen(stream);
  }

  @Test
  public void should_pass_if_actual_contains_single_expected() {
    // GIVEN
    Path file = mockRegularFile("Test.java");
    List<Path> items = singletonList(file);
    DirectoryStream<Path> stream = directoryStream(items);
    Path actual = mockDirectory(stream, "root");
    // WHEN
    Throwable error = catchThrowable(() ->
      paths.assertIsDirectoryContaining(INFO, actual, javaSource)
    );
    // THEN
    assertThat(error).isNull();
    verify(file, times(1)).getFileName();
    failIfStreamIsOpen(stream);
  }

  @Test
  public void should_pass_if_actual_contains_only_expected() {
    // GIVEN
    Path file1 = mockRegularFile("Test.java");
    Path file2 = mockRegularFile("Utils.java");
    List<Path> items = Arrays.asList(file1, file2);
    DirectoryStream<Path> stream = directoryStream(items);
    Path actual = mockDirectory(stream, "root");
    // WHEN
    Throwable error = catchThrowable(() ->
      paths.assertIsDirectoryContaining(INFO, actual, javaSource)
    );
    // THEN
    assertThat(error).isNull();
    verify(file1, times(1)).getFileName();
    verify(file2, times(0)).getFileName();//Check lazy directory listing processing
    failIfStreamIsOpen(stream);
  }

  @Test
  public void should_pass_if_actual_contains_others_including_expected() {
    // GIVEN
    Path file1 = mockRegularFile("Test.class");
    Path file2 = mockRegularFile("Test.java");
    Path file3 = mockRegularFile("Utils.class");
    Path file4 = mockRegularFile("Utils.java");
    Path file5 = mockRegularFile("application.yml");
    List<Path> items = Arrays.asList(file1, file2, file3, file4, file5);
    DirectoryStream<Path> stream = directoryStream(items);
    Path actual = mockDirectory(stream, "root");
    // WHEN
    Throwable error = catchThrowable(() ->
      paths.assertIsDirectoryContaining(INFO, actual, javaSource)
    );
    // THEN
    assertThat(error).isNull();
    verify(file1, times(1)).getFileName();
    verify(file2, times(1)).getFileName();
    verify(file3, times(0)).getFileName();//Check lazy directory listing processing
    verify(file4, times(0)).getFileName();
    verify(file5, times(0)).getFileName();
    failIfStreamIsOpen(stream);
  }
}
