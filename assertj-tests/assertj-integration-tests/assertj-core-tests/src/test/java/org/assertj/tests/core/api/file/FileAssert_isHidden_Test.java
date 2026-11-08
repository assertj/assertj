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
package org.assertj.tests.core.api.file;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeHidden.shouldBeHidden;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.junit.jupiter.api.condition.OS.WINDOWS;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.assertj.tests.core.testkit.PathFactory;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.io.TempDir;

class FileAssert_isHidden_Test {

  @TempDir
  Path tempDir;

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    File actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isHidden());
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @DisabledOnOs(WINDOWS)
  @Nested
  class On_Unix {

    @Test
    void should_fail_if_actual_does_not_exist_and_is_not_hidden() {
      // GIVEN
      File actual = PathFactory.createPath(tempDir).toFile();
      // WHEN
      AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isHidden());
      // THEN
      then(assertionError).hasMessage(shouldBeHidden(actual).create());
    }

    @Test
    void should_pass_if_actual_does_not_exist_and_is_hidden() {
      // GIVEN
      File actual = PathFactory.Unix.createHiddenPath(tempDir).toFile();
      // WHEN/THEN
      assertThat(actual).isHidden();
    }

    @Test
    void should_fail_if_actual_exists_and_is_not_hidden() throws IOException {
      // GIVEN
      File actual = PathFactory.createFile(tempDir).toFile();
      // WHEN
      AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isHidden());
      // THEN
      then(assertionError).hasMessage(shouldBeHidden(actual).create());
    }

    @Test
    void should_pass_if_actual_exists_and_is_hidden() throws IOException {
      // GIVEN
      File actual = PathFactory.Unix.createHiddenFile(tempDir).toFile();
      // WHEN/THEN
      assertThat(actual).isHidden();
    }

  }

  @EnabledOnOs(WINDOWS)
  @Nested
  class On_Windows {

    @Test
    void should_fail_if_actual_does_not_exist() {
      // GIVEN
      File actual = PathFactory.createPath(tempDir).toFile();
      // WHEN
      AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isHidden());
      // THEN
      then(assertionError).hasMessage(shouldBeHidden(actual).create());
    }

    @Test
    void should_fail_if_actual_exists_and_is_not_hidden() throws IOException {
      // GIVEN
      File actual = PathFactory.createFile(tempDir).toFile();
      // WHEN
      AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isHidden());
      // THEN
      then(assertionError).hasMessage(shouldBeHidden(actual).create());
    }

    @Test
    void should_pass_if_actual_exists_and_is_hidden() throws IOException {
      // GIVEN
      File actual = PathFactory.Windows.createHiddenFile(tempDir).toFile();
      // WHEN/THEN
      assertThat(actual).isHidden();
    }

  }

}
