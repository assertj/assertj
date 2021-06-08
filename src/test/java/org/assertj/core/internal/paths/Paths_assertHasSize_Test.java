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

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldExist.shouldExist;
import static org.assertj.core.error.ShouldHaveSize.shouldHaveSize;

import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.BDDMockito.given;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Paths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

/**
 * Tests for <code>{@link Paths#assertHasSize(AssertionInfo, Path, long)} </code>
 */
@DisplayName("Paths assertHasSize")
class Paths_assertHasSize_Test extends MockPathsBaseTest {

  private long expectedSize;

  @BeforeEach
  public void setUpPath() {
    given(nioFilesWrapper.exists(actual)).willReturn(true);
    given(nioFilesWrapper.isReadable(actual)).willReturn(true);
    expectedSize = 1L;
  }

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    AssertionError error = expectAssertionError(() -> paths.assertHasSize(info, null, expectedSize));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_exist() {
    // GIVEN
    given(nioFilesWrapper.exists(actual)).willReturn(false);
    // WHEN
    AssertionError error = expectAssertionError(() -> paths.assertHasSize(info, actual, expectedSize));
    // THEN
    then(error).hasMessage(shouldExist(actual).create());
  }

  @Test
  void should_throw_IOException_if_IO_error_occurs_when_finding_path_size() throws IOException {
    // GIVEN
    IOException exception = new IOException();
    given(nioFilesWrapper.size(actual)).willThrow(exception);
    // THEN
    assertThatExceptionOfType(UncheckedIOException.class).isThrownBy(() -> paths.assertHasSize(info, actual, expectedSize))
      .withMessage(format("unable to verify the size of the path: <$s>", actual));
  }

  @Test
  void should_pass_if_actual_size_equals_expected_size() throws IOException{
    // GIVEN
    long actualSize = expectedSize;
    given(nioFilesWrapper.size(actual)).willReturn(actualSize);
    // THEN
    paths.assertHasSize(info, actual, expectedSize);
  }

  @Test
  void should_fail_if_actual_size_does_not_equal_expected_size() throws IOException{
    // GIVEN
    long actualSize = 2L;
    given(nioFilesWrapper.size(actual)).willReturn(actualSize);
    // WHEN
    AssertionError error = expectAssertionError(() -> paths.assertHasSize(info, actual, expectedSize));
    // THEN
    then(error).hasMessage(shouldHaveSize(actual, expectedSize, actualSize).create());
  }

}
