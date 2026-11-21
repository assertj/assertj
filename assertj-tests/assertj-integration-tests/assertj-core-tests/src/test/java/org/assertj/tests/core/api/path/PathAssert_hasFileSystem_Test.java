/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.api.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveFileSystem.shouldHaveFileSystem;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

import java.nio.file.FileSystem;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

class PathAssert_hasFileSystem_Test {

  @Test
  void should_fail_if_actual_path_is_null() {
    // GIVEN
    Path actual = null;
    FileSystem expectedFileSystem = mock(FileSystem.class, withSettings().stubOnly());
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasFileSystem(expectedFileSystem));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_expected_filesystem_is_null() {
    // GIVEN
    Path actual = mock(Path.class, withSettings().stubOnly());
    FileSystem actualFileSystem = mock(FileSystem.class, withSettings().stubOnly());
    given(actual.getFileSystem()).willReturn(actualFileSystem);
    FileSystem expectedFileSystem = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).hasFileSystem(expectedFileSystem));
    // THEN
    then(npe).hasMessage("The expected file system should not be null");
  }

  @Test
  void should_fail_if_actual_filesystem_is_null() {
    // GIVEN
    Path actual = mock(Path.class, withSettings().stubOnly());
    given(actual.getFileSystem()).willReturn(null);
    FileSystem expectedFileSystem = mock(FileSystem.class, withSettings().stubOnly());
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).hasFileSystem(expectedFileSystem));
    // THEN
    then(npe).hasMessage("The actual file system should not be null");
  }

  @Test
  void should_fail_if_file_systems_differ() {
    // GIVEN
    Path actual = mock(Path.class, withSettings().stubOnly());
    FileSystem actualFileSystem = mock(FileSystem.class, withSettings().stubOnly());
    given(actual.getFileSystem()).willReturn(actualFileSystem);
    FileSystem expectedFileSystem = mock(FileSystem.class, withSettings().stubOnly());
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasFileSystem(expectedFileSystem));
    // THEN
    then(assertionError).hasMessage(shouldHaveFileSystem(actual, expectedFileSystem).create())
                        .isInstanceOf(AssertionFailedError.class)
                        .extracting(AssertionFailedError.class::cast)
                        .satisfies(failure -> then(failure.getActual().getEphemeralValue()).isSameAs(actualFileSystem))
                        .satisfies(failure -> then(failure.getExpected().getEphemeralValue()).isSameAs(expectedFileSystem));
  }

  @Test
  void should_pass_if_file_systems_are_same() {
    // GIVEN
    Path actual = mock(Path.class, withSettings().stubOnly());
    FileSystem actualFileSystem = mock(FileSystem.class, withSettings().stubOnly());
    given(actual.getFileSystem()).willReturn(actualFileSystem);
    // WHEN/THEN
    then(actual).hasFileSystem(actualFileSystem);
  }

}
