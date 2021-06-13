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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldBeRegularFile.shouldBeRegularFile;
import static org.assertj.core.error.ShouldHaveExtension.shouldHaveExtension;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.assertj.core.api.AssertionInfo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Tests for
 * <code>{@link org.assertj.core.internal.Paths#assertHasExtension(AssertionInfo, java.nio.file.Path, String)}</code>.
 */
@DisplayName("Paths assertHasExtension")
class Paths_assertHasExtension_Test extends MockPathsBaseTest {

  private final String expectedExtension = "log";
  public static FileSystemResource resource;

  private static Path file;
  private static Path symlink;
  private static Path directory;

  @BeforeAll
  static void initPaths() throws IOException {
    resource = new FileSystemResource();
    final FileSystem fs = resource.getFileSystem();

    directory = fs.getPath("/dir");
    Files.createDirectory(directory);
    file = fs.getPath("/dir/gc.log");
    symlink = fs.getPath("/dir/good-symlink");
    Files.createFile(file);
    Files.createSymbolicLink(symlink, file);
  }

  @AfterAll
  static void tearDown() {
    resource.close();
  }

  @Test
  void should_throw_error_if_actual_is_null() {
    // WHEN/THEN
    assertThatExceptionOfType(AssertionError.class)
      .isThrownBy(() -> paths.assertHasExtension(someInfo(), null, expectedExtension))
      .withMessage(actualIsNull());
  }

  @Test
  void should_throw_npe_if_extension_is_null() {
    // WHEN/THEN
    assertThatNullPointerException().isThrownBy(() -> paths.assertHasExtension(someInfo(), actual, null))
                                    .withMessage("The expected extension should not be null.");
  }

  @ParameterizedTest
  @MethodSource("irregularFileProviders")
  void should_throw_error_if_actual_is_not_a_regular_file(Path irregularFile) {
    // GIVEN
    when(nioFilesWrapper.exists(actual)).thenReturn(true);
    when(actual.getFileName()).thenReturn(irregularFile);

    // WHEN
    Throwable error = catchThrowable(() -> paths.assertHasExtension(info, actual, expectedExtension));

    // THEN
    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeRegularFile(actual));
  }

  private static Stream<Arguments> irregularFileProviders() {
    return Stream.of(
      Arguments.of(symlink),
      Arguments.of(directory)
    );
  }

  @Test
  void should_throw_error_if_actual_does_not_have_the_expected_extension() {
    // GIVEN
    when(actual.getFileName()).thenReturn(file);
    when(nioFilesWrapper.isRegularFile(actual)).thenReturn(true);
    when(nioFilesWrapper.exists(actual)).thenReturn(true);

    // WHEN
    Throwable error = catchThrowable(() -> paths.assertHasExtension(info, actual, "png"));

    // THEN
    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(someInfo(), shouldHaveExtension(actual, "log", "png"));
  }

  @Test
  void should_pass_if_actual_has_expected_extension() {
    // GIVEN
    when(actual.getFileName()).thenReturn(file);
    when(nioFilesWrapper.isRegularFile(actual)).thenReturn(true);
    when(nioFilesWrapper.exists(actual)).thenReturn(true);

    // WHEN/THEN
    paths.assertHasExtension(someInfo(), actual, expectedExtension);
  }
}
