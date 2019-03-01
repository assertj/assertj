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

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Files;
import org.assertj.core.internal.FilesBaseTest;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldBeDirectory.shouldBeDirectory;
import static org.assertj.core.error.ShouldNotContain.shouldNotContain;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.emptyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link Files#assertIsDirectoryNotContaining(AssertionInfo, File, Predicate)}</code>
 *
 * @author Valeriy Vyrva
 */
@SuppressWarnings({"WeakerAccess", "ThrowableNotThrown"})
public class Files_assertIsDirectoryNotContaining_Predicate_Test extends FilesBaseTest {

  private static final Predicate<File> javaSource = file -> file.getName().endsWith(".java");

  @Test
  public void should_throw_error_if_filter_is_null() {
    assertThatNullPointerException()
      .isThrownBy(() -> files.assertIsDirectoryNotContaining(INFO, null, (Predicate<File>) null))
      .withMessage("The files filter should not be null");
  }

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class)
      .isThrownBy(() -> files.assertIsDirectoryNotContaining(INFO, null, javaSource))
      .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_with_should_be_directory_error_if_actual_does_not_exist() {
    // GIVEN
    given(actual.exists()).willReturn(false);
    // WHEN
    Throwable error = catchThrowable(() ->
      files.assertIsDirectoryNotContaining(INFO, actual, javaSource)
    );
    // THEN
    verify(failures).failure(INFO, shouldBeDirectory(actual));
    assertThat(error).isInstanceOf(AssertionError.class);
  }

  @Test
  public void should_fail_if_actual_exists_but_is_not_directory() {
    // GIVEN
    given(actual.exists()).willReturn(true);
    given(actual.isDirectory()).willReturn(false);
    // WHEN
    Throwable error = catchThrowable(() ->
      files.assertIsDirectoryNotContaining(INFO, actual, javaSource)
    );
    // THEN
    verify(failures).failure(INFO, shouldBeDirectory(actual));
    assertThat(error).isInstanceOf(AssertionError.class);
  }

  @Test
  public void should_throw_error_on_null_listing() {
    // GIVEN
    given(actual.exists()).willReturn(true);
    given(actual.isDirectory()).willReturn(true);
    given(actual.list()).willReturn(null);
    given(actual.listFiles(any(FileFilter.class))).willCallRealMethod();
    // WHEN
    Throwable error = catchThrowable(() ->
      files.assertIsDirectoryNotContaining(INFO, actual, javaSource)
    );
    // THEN
    assertThat(error)
      .isInstanceOf(NullPointerException.class)
      .hasMessage("Directory listing should not be null");
  }

  @Test
  public void should_fail_if_actual_contains_single_not_expected() {
    // GIVEN
    File file = mockRegularFile("Test.java");
    List<File> items = singletonList(file);
    File actual = mockDirectory(items, "root");
    // WHEN
    Throwable error = catchThrowable(() ->
      files.assertIsDirectoryNotContaining(INFO, actual, javaSource)
    );
    // THEN
    assertThat(error)
      .isInstanceOf(AssertionError.class)
      .hasMessageContaining("["
        + '"' + file.getName() + '"' + "]"
      );
    verify(failures).failure(INFO, shouldNotContain(
      items.stream().map(File::getName).collect(Collectors.toList()),
      javaSource,
      items.stream().map(File::getName).collect(Collectors.toList())
    ));
  }

  @Test
  public void should_fail_if_actual_contains_only_not_expected() {
    // GIVEN
    File file1 = mockRegularFile("Test.java");
    File file2 = mockRegularFile("Utils.java");
    List<File> items = Arrays.asList(file1, file2);
    File actual = mockDirectory(items, "root");
    // WHEN
    Throwable error = catchThrowable(() ->
      files.assertIsDirectoryNotContaining(INFO, actual, javaSource)
    );
    // THEN
    assertThat(error)
      .isInstanceOf(AssertionError.class)
      .hasMessageContaining("["
        + '"' + file1.getName() + '"' + ", "
        + '"' + file2.getName() + '"' + "]"
      );
    verify(failures).failure(INFO, shouldNotContain(
      items.stream().map(File::getName).collect(Collectors.toList()),
      javaSource,
      items.stream().map(File::getName).collect(Collectors.toList())
    ));
  }

  @Test
  public void should_fail_if_actual_contains_others_including_not_expected() {
    // GIVEN
    File file1 = mockRegularFile("Test.class");
    File file2 = mockRegularFile("Test.java");
    File file3 = mockRegularFile("Utils.class");
    File file4 = mockRegularFile("Utils.java");
    File file5 = mockRegularFile("application.yml");
    List<File> items = Arrays.asList(file1, file2, file3, file4, file5);
    File actual = mockDirectory(items, "root");
    // WHEN
    Throwable error = catchThrowable(() ->
      files.assertIsDirectoryNotContaining(INFO, actual, javaSource)
    );
    // THEN
    assertThat(error)
      .isInstanceOf(AssertionError.class)
      .hasMessageContaining("["
        + '"' + file2.getName() + '"' + ", "
        + '"' + file4.getName() + '"' + "]"
      );
    verify(failures).failure(INFO, shouldNotContain(
      items.stream().map(File::getName).collect(Collectors.toList()),
      javaSource,
      Arrays.asList(file2.getName(), file4.getName())
    ));
  }

  @Test
  public void should_pass_if_actual_is_empty() {
    // GIVEN
    List<File> items = emptyList();
    File actual = mockDirectory(items, "root");
    // WHEN
    Throwable error = catchThrowable(() ->
      files.assertIsDirectoryNotContaining(INFO, actual, javaSource)
    );
    // THEN
    assertThat(error).isNull();
  }

  @Test
  public void should_pass_if_actual_does_not_contains_expected() {
    // GIVEN
    File file = mockRegularFile("root", "Test.class");
    List<File> items = singletonList(file);
    File actual = mockDirectory(items, "root");
    // WHEN
    Throwable error = catchThrowable(() ->
      files.assertIsDirectoryNotContaining(INFO, actual, javaSource)
    );
    // THEN
    assertThat(error).isNull();
  }
}
