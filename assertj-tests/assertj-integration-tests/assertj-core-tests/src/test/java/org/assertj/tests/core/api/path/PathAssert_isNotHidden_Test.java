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
import static org.assertj.core.api.Assertions.catchException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeHidden.shouldNotBeHidden;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.junit.jupiter.api.condition.OS.WINDOWS;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

import org.assertj.tests.core.testkit.PathFactory;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.io.TempDir;

class PathAssert_isNotHidden_Test {

  @TempDir
  Path tempDir;

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Path actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isNotHidden());
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @DisabledOnOs(WINDOWS)
  @Nested
  class On_Unix {

    @Test
    void should_fail_if_actual_does_not_exist_and_is_hidden() {
      // GIVEN
      Path actual = PathFactory.Unix.createHiddenPath(tempDir);
      // WHEN
      AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isNotHidden());
      // THEN
      then(assertionError).hasMessage(shouldNotBeHidden(actual).create());
    }

    @Test
    void should_pass_if_actual_does_not_exist_and_is_not_hidden() {
      // GIVEN
      Path actual = PathFactory.createPath(tempDir);
      // WHEN/THEN
      assertThat(actual).isNotHidden();
    }

    @Test
    void should_fail_if_actual_exists_and_is_hidden() throws IOException {
      // GIVEN
      Path actual = PathFactory.Unix.createHiddenFile(tempDir);
      // WHEN
      AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isNotHidden());
      // THEN
      then(assertionError).hasMessage(shouldNotBeHidden(actual).create());
    }

    @Test
    void should_pass_if_actual_exists_and_is_not_hidden() throws IOException {
      // GIVEN
      Path actual = PathFactory.createFile(tempDir);
      // WHEN/THEN
      assertThat(actual).isNotHidden();
    }

  }

  @EnabledOnOs(WINDOWS)
  @Nested
  class On_Windows {

    @Test
    void should_fail_if_actual_does_not_exist() {
      // GIVEN
      Path actual = PathFactory.createPath(tempDir);
      // WHEN/THEN
      Exception exception = catchException(() -> assertThat(actual).isNotHidden());
      // THEN
      then(exception).isInstanceOf(UncheckedIOException.class)
                     .hasCauseInstanceOf(NoSuchFileException.class);
    }

    @Test
    void should_fail_if_actual_exists_and_is_hidden() throws IOException {
      // GIVEN
      Path actual = PathFactory.Windows.createHiddenFile(tempDir);
      // WHEN
      AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isNotHidden());
      // THEN
      then(assertionError).hasMessage(shouldNotBeHidden(actual).create());
    }

    @Test
    void should_pass_if_actual_exists_and_is_not_hidden() throws IOException {
      // GIVEN
      Path actual = PathFactory.createFile(tempDir);
      // WHEN/THEN
      assertThat(actual).isNotHidden();
    }

  }

}
