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

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeDirectory.shouldBeDirectory;
import static org.assertj.core.error.ShouldExist.shouldExist;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
class Paths_assertIsNotEmptyDirectory_Test extends PathsBaseTest {

  @BeforeEach
  void setUpNioFilesWrapper() throws IOException {
    given(nioFilesWrapper.newDirectoryStream(any(), any())).willCallRealMethod();
  }

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    AssertionError error = expectAssertionError(() -> paths.assertIsNotEmptyDirectory(info, null));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_exist() {
    // GIVEN
    Path actual = tempDir.resolve("non-existent");
    // WHEN
    AssertionError error = expectAssertionError(() -> paths.assertIsNotEmptyDirectory(info, actual));
    // THEN
    then(error).hasMessage(shouldExist(actual).create());
  }

  @Test
  void should_fail_if_actual_is_not_a_directory() throws IOException {
    // GIVEN
    Path actual = Files.createFile(tempDir.resolve("file"));
    // WHEN
    AssertionError error = expectAssertionError(() -> paths.assertIsNotEmptyDirectory(info, actual));
    // THEN
    then(error).hasMessage(shouldBeDirectory(actual).create());
  }

  @Test
  void should_pass_if_actual_is_not_empty() throws IOException {
    // GIVEN
    Path actual = Files.createDirectory(tempDir.resolve("actual"));
    Files.createFile(actual.resolve("file"));
    // WHEN/THEN
    paths.assertIsNotEmptyDirectory(info, actual);
  }

  @Test
  void should_fail_if_actual_is_empty() throws IOException {
    // GIVEN
    Path actual = Files.createDirectory(tempDir.resolve("actual"));
    // WHEN
    AssertionError error = expectAssertionError(() -> paths.assertIsNotEmptyDirectory(info, actual));
    // THEN
    then(error).hasMessage(shouldNotBeEmpty(actual).create());
  }

  @Test
  void should_rethrow_IOException_as_UncheckedIOException() throws IOException {
    // GIVEN
    Path actual = Files.createDirectory(tempDir.resolve("actual"));
    IOException exception = new IOException("boom!");
    willThrow(exception).given(nioFilesWrapper).newDirectoryStream(eq(actual), any());
    // WHEN
    Throwable thrown = catchThrowable(() -> paths.assertIsNotEmptyDirectory(info, actual));
    // THEN
    then(thrown).isInstanceOf(UncheckedIOException.class)
                .hasCause(exception);
  }

}
