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
import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldBeDirectory.shouldBeDirectory;
import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.error.ShouldExist.shouldExist;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.emptyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link Paths#assertIsDirectoryEmpty(AssertionInfo, Path)}</code>
 *
 * @author Valeriy Vyrva
 */
@SuppressWarnings({"WeakerAccess", "ThrowableNotThrown"})
public class Paths_assertIsDirectoryEmpty_Test extends MockPathsBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class)
      .isThrownBy(() -> paths.assertIsDirectoryEmpty(info, null))
      .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_with_should_exist_error_if_actual_does_not_exist() {
    // GIVEN
    given(nioFilesWrapper.exists(actual)).willReturn(false);
    // WHEN
    Throwable error = catchThrowable(() ->
      paths.assertIsDirectoryEmpty(INFO, actual)
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
      paths.assertIsDirectoryEmpty(INFO, actual)
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
      paths.assertIsDirectoryEmpty(INFO, actual)
    );
    // THEN
    assertThat(error)
      .isInstanceOf(UncheckedIOException.class)
      .hasCause(cause);
  }

  @Test
  public void should_fail_if_actual_is_not_empty() {
    // GIVEN
    Path file = mockRegularFile("root", "Test.class");
    List<Path> items = singletonList(file);
    DirectoryStream<Path> stream = directoryStream(items);
    Path actual = mockDirectory(stream, "root");
    // WHEN
    Throwable error = catchThrowable(() ->
      paths.assertIsDirectoryEmpty(INFO, actual)
    );
    // THEN
    assertThat(error)
      .isInstanceOf(AssertionError.class)
      .hasMessageContaining("[" + file.toString() + "]");
    verify(failures).failure(INFO, shouldBeEmpty(items));
    verify(file, times(0)).getFileName();//We do not even check
    failIfStreamIsOpen(stream);
  }

  @Test
  public void should_pass_if_actual_is_empty() {
    // GIVEN
    List<Path> items = emptyList();
    DirectoryStream<Path> stream = directoryStream(items);
    Path actual = mockDirectory(stream, "root");
    // WHEN
    Throwable error = catchThrowable(() ->
      paths.assertIsDirectoryEmpty(INFO, actual)
    );
    // THEN
    assertThat(error).isNull();
    failIfStreamIsOpen(stream);
  }
}
