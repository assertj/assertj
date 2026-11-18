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
import static org.assertj.core.error.ShouldHaveName.shouldHaveName;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.io.File;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileAssert_hasFileName_Test {

  @TempDir
  Path tempDir;

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    File actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasFileName("expected.name"));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_expected_name_is_null() {
    // GIVEN
    File actual = tempDir.resolve("expected.name").toFile();
    String expectedName = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).hasFileName(expectedName));
    // THEN
    then(npe).hasMessage("The expected name should not be null.");
  }

  @Test
  void should_fail_if_actual_does_not_have_the_expected_name() {
    // GIVEN
    File actual = tempDir.resolve("not_expected.name").toFile();
    String expectedName = "expected.name";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasFileName(expectedName));
    // THEN
    then(assertionError).hasMessage(shouldHaveName(actual, expectedName).create());
  }

  @Test
  void should_pass_if_actual_has_the_expected_name() {
    // GIVEN
    File actual = tempDir.resolve("expected.name").toFile();
    // WHEN/THEN
    then(actual).hasFileName("expected.name");
  }

}
