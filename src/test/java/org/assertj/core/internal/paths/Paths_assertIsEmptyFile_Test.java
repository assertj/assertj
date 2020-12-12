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

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.error.ShouldBeRegularFile.shouldBeRegularFile;
import static org.assertj.core.error.ShouldExist.shouldExist;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.BDDMockito.given;

import java.io.IOException;
import java.nio.file.Path;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.exception.PathsException;
import org.assertj.core.internal.Paths;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Paths#assertIsEmptyFile(AssertionInfo, Path)}</code>
 *
 * @author Stefano Cordio
 */
@DisplayName("Paths assertIsEmptyFile")
class Paths_assertIsEmptyFile_Test extends MockPathsBaseTest {

  @Test
  void should_pass_if_actual_is_empty() {
    // GIVEN
    Path actual = mockEmptyRegularFile("file");
    // WHEN/THEN
    paths.assertIsEmptyFile(INFO, actual);
  }

  @Test
  void should_fail_if_actual_is_not_empty() {
    // GIVEN
    Path actual = mockNonEmptyRegularFile("file");
    // WHEN
    AssertionError error = expectAssertionError(() -> paths.assertIsEmptyFile(INFO, actual));
    // THEN
    then(error).hasMessage(shouldBeEmpty(actual).create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Path actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> paths.assertIsEmptyFile(INFO, actual));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_exist() {
    // GIVEN
    given(nioFilesWrapper.exists(actual)).willReturn(false);
    // WHEN
    AssertionError error = expectAssertionError(() -> paths.assertIsEmptyFile(INFO, actual));
    // THEN
    then(error).hasMessage(shouldExist(actual).create());
  }

  @Test
  void should_fail_if_actual_exists_but_is_not_regular_file() {
    // GIVEN
    given(nioFilesWrapper.exists(actual)).willReturn(true);
    given(nioFilesWrapper.isRegularFile(actual)).willReturn(false);
    // WHEN
    AssertionError error = expectAssertionError(() -> paths.assertIsEmptyFile(INFO, actual));
    // THEN
    then(error).hasMessage(shouldBeRegularFile(actual).create());
  }

  @Test
  void should_throw_runtime_error_wrapping_caught_IOException() throws IOException {
    // GIVEN
    IOException cause = new IOException();
    given(nioFilesWrapper.exists(actual)).willReturn(true);
    given(nioFilesWrapper.isRegularFile(actual)).willReturn(true);
    given(nioFilesWrapper.size(actual)).willThrow(cause);
    // WHEN
    Throwable error = catchThrowable(() -> paths.assertIsEmptyFile(INFO, actual));
    // THEN
    then(error).isInstanceOf(PathsException.class)
               .hasCause(cause);
  }

}
