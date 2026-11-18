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
import static org.assertj.core.error.ShouldBeFile.shouldBeFile;
import static org.assertj.core.error.ShouldHaveNoExtension.shouldHaveNoExtension;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class FileAssert_hasNoExtension_Test {

  @TempDir
  Path tempDir;

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    File actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasNoExtension());
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_exist() {
    // GIVEN
    File actual = tempDir.resolve("non-existent").toFile();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasNoExtension());
    // THEN
    then(assertionError).hasMessage(shouldBeFile(actual).create());
  }

  @Test
  void should_fail_if_actual_is_a_directory() {
    // GIVEN
    File actual = tempDir.toFile();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasNoExtension());
    // THEN
    then(assertionError).hasMessage(shouldBeFile(actual).create());
  }

  @Test
  void should_fail_if_actual_has_an_extension() throws IOException {
    // GIVEN
    File actual = createFile("text.txt");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasNoExtension());
    // THEN
    then(assertionError).hasMessage(shouldHaveNoExtension(actual, "txt").create());
  }

  @ParameterizedTest
  @ValueSource(strings = { "file", "file." })
  void should_pass_if_actual_has_no_extension(String filename) throws IOException {
    // GIVEN
    File actual = createFile(filename);
    // WHEN/THEN
    then(actual).hasNoExtension();
  }

  private File createFile(String filename) throws IOException {
    return Files.createFile(tempDir.resolve(filename)).toFile();
  }

}
