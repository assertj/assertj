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
package org.assertj.core.internal.files;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeFile.shouldBeFile;
import static org.assertj.core.error.ShouldHaveNoExtension.shouldHaveNoExtension;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Files.newFile;

import java.io.File;

import org.assertj.core.internal.FilesBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class Files_assertHasNoExtension_Test extends FilesBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    File actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertHasNoExtension(INFO, actual));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_exist() {
    // GIVEN
    File actual = new File("non-existent");
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertHasNoExtension(INFO, actual));
    // THEN
    then(error).hasMessage(shouldBeFile(actual).create());
  }

  @Test
  void should_fail_if_actual_is_not_a_file() {
    // GIVEN
    File actual = tempDir;
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertHasNoExtension(INFO, actual));
    // THEN
    then(error).hasMessage(shouldBeFile(actual).create());
  }

  @Test
  void should_fail_if_actual_has_extension() {
    // GIVEN
    File actual = newFile(tempDir.getAbsolutePath() + "/text.txt");
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertHasNoExtension(INFO, actual));
    // THEN
    then(error).hasMessage(shouldHaveNoExtension(actual, "txt").create());
  }

  @ParameterizedTest
  @ValueSource(strings = { "file", "file." })
  void should_pass_if_actual_has_no_extension(String filename) {
    // GIVEN / WHEN
    File actual = newFile(tempDir.getAbsolutePath() + "/" + filename);
    // THEN
    underTest.assertHasNoExtension(INFO, actual);
  }

}
