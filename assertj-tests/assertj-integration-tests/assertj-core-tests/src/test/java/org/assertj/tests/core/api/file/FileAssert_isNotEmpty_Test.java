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

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeFile.shouldBeFile;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.assertj.tests.core.testkit.PathFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileAssert_isNotEmpty_Test {

  @TempDir
  Path tempDir;

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    File actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isNotEmpty());
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_exist() {
    // GIVEN
    File actual = PathFactory.createPath(tempDir).toFile();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isNotEmpty());
    // THEN
    then(assertionError).hasMessage(shouldBeFile(actual).create());
  }

  @Test
  void should_fail_if_actual_is_a_directory() throws IOException {
    // GIVEN
    File actual = Files.createDirectory(PathFactory.createPath(tempDir)).toFile();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isNotEmpty());
    // THEN
    then(assertionError).hasMessage(shouldBeFile(actual).create());
  }

  @Test
  void should_fail_if_actual_is_empty() throws IOException {
    // GIVEN
    File actual = PathFactory.createFile(tempDir).toFile();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isNotEmpty());
    // THEN
    then(assertionError).hasMessage(shouldNotBeEmpty(actual).create());
  }

  @Test
  void should_pass_if_actual_is_not_empty() throws IOException {
    // GIVEN
    File actual = Files.writeString(PathFactory.createPath(tempDir), "content", UTF_8).toFile();
    // WHEN/THEN
    then(actual).isNotEmpty();
  }

}
