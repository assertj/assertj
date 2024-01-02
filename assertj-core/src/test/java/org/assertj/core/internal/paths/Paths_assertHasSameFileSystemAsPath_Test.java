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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.internal.paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveSameFileSystemAs.shouldHaveSameFileSystemAs;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

import java.nio.file.FileSystem;
import java.nio.file.Path;

import org.assertj.core.internal.PathsBaseTest;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

/**
 * Tests for {@code .assertHasSameFileSystemAsPath}.
 *
 * @author Ashley Scopes
 */
class Paths_assertHasSameFileSystemAsPath_Test extends PathsBaseTest {

  @Test
  void should_fail_if_actual_path_is_null() {
    // GIVEN
    Path expectedPath = mock(Path.class, withSettings().stubOnly());
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertHasSameFileSystemAs(INFO, null, expectedPath));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_expected_path_is_null() {
    // GIVEN
    Path actualPath = mock(Path.class, withSettings().stubOnly());
    // WHEN
    Throwable error = catchThrowable(() -> underTest.assertHasSameFileSystemAs(INFO, actualPath, null));
    // THEN
    then(error).isInstanceOf(NullPointerException.class)
               .hasMessage("The expected path should not be null");
  }

  @Test
  void should_fail_if_expected_filesystem_is_null() {
    // GIVEN
    Path actualPath = mock(Path.class, withSettings().stubOnly());
    FileSystem actualFileSystem = mock(FileSystem.class, withSettings().stubOnly());
    given(actualPath.getFileSystem()).willReturn(actualFileSystem);
    Path expectedPath = mock(Path.class, withSettings().stubOnly());
    given(expectedPath.getFileSystem()).willReturn(null);

    // WHEN
    Throwable error = catchThrowable(() -> underTest.assertHasSameFileSystemAs(INFO, actualPath, expectedPath));
    // THEN
    then(error).isInstanceOf(NullPointerException.class)
               .hasMessage("The expected file system should not be null");
  }

  @Test
  void should_fail_if_actual_filesystem_is_null() {
    // GIVEN
    Path actualPath = mock(Path.class, withSettings().stubOnly());
    given(actualPath.getFileSystem()).willReturn(null);
    Path expectedPath = mock(Path.class, withSettings().stubOnly());
    FileSystem expectedFileSystem = mock(FileSystem.class, withSettings().stubOnly());
    given(expectedPath.getFileSystem()).willReturn(expectedFileSystem);
    // WHEN
    Throwable error = catchThrowable(() -> underTest.assertHasSameFileSystemAs(INFO, actualPath, expectedPath));
    // THEN
    then(error).isInstanceOf(NullPointerException.class)
               .hasMessage("The actual file system should not be null");
  }

  @Test
  void should_fail_if_file_systems_differ() {
    // GIVEN
    Path actualPath = mock(Path.class, withSettings().stubOnly());
    FileSystem actualFileSystem = mock(FileSystem.class, withSettings().stubOnly());
    given(actualPath.getFileSystem()).willReturn(actualFileSystem);
    Path expectedPath = mock(Path.class, withSettings().stubOnly());
    FileSystem expectedFileSystem = mock(FileSystem.class, withSettings().stubOnly());
    given(expectedPath.getFileSystem()).willReturn(expectedFileSystem);
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertHasSameFileSystemAs(INFO, actualPath, expectedPath));
    // THEN
    then(error).hasMessage(shouldHaveSameFileSystemAs(actualPath, expectedPath).create())
               .isInstanceOf(AssertionFailedError.class)
               .extracting(AssertionFailedError.class::cast)
               .satisfies(failure -> assertThat(failure.getActual().getEphemeralValue()).isSameAs(actualFileSystem))
               .satisfies(failure -> assertThat(failure.getExpected().getEphemeralValue()).isSameAs(expectedFileSystem));
  }

  @Test
  void should_succeed_if_file_systems_are_same() {
    // GIVEN
    Path actualPath = mock(Path.class, withSettings().stubOnly());
    FileSystem actualFileSystem = mock(FileSystem.class, withSettings().stubOnly());
    given(actualPath.getFileSystem()).willReturn(actualFileSystem);
    Path expectedPath = mock(Path.class, withSettings().stubOnly());
    given(expectedPath.getFileSystem()).willReturn(actualFileSystem);
    // WHEN/THEN
    underTest.assertHasSameFileSystemAs(INFO, actualPath, expectedPath);
  }
}
