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
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeFile.shouldBeFile;
import static org.assertj.core.error.ShouldHaveExtension.shouldHaveExtension;
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

class FileAssert_hasExtension_Test {

  @TempDir
  Path tempDir;

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    File actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasExtension("txt"));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_expected_extension_is_null() throws IOException {
    // GIVEN
    File actual = createFile("file.txt");
    String expected = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).hasExtension(expected));
    // THEN
    then(npe).hasMessage("The expected extension should not be null.");
  }

  @Test
  void should_fail_if_actual_does_not_exist() {
    // GIVEN
    File actual = tempDir.resolve("file.java").toFile();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasExtension("java"));
    // THEN
    then(assertionError).hasMessage(shouldBeFile(actual).create());
  }

  @Test
  void should_fail_if_actual_is_a_directory() {
    // GIVEN
    File actual = tempDir.toFile();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasExtension("java"));
    // THEN
    then(assertionError).hasMessage(shouldBeFile(actual).create());
  }

  @Test
  void should_fail_if_actual_does_not_have_the_expected_extension() throws IOException {
    // GIVEN
    File actual = createFile("file.png");
    String expected = "java";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasExtension(expected));
    // THEN
    then(assertionError).hasMessage(shouldHaveExtension(actual, "png", expected).create());
  }

  @ParameterizedTest
  @ValueSource(strings = { "file", "file." })
  void should_fail_if_actual_has_no_extension(String filename) throws IOException {
    // GIVEN
    File actual = createFile(filename);
    String expected = "log";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasExtension(expected));
    // THEN
    then(assertionError).hasMessage(shouldHaveExtension(actual, expected).create());
  }

  @Test
  void should_pass_if_actual_has_the_expected_extension() throws IOException {
    // GIVEN
    File actual = createFile("file.java");
    // WHEN/THEN
    then(actual).hasExtension("java");
  }

  private File createFile(String filename) throws IOException {
    return Files.createFile(tempDir.resolve(filename)).toFile();
  }

}
