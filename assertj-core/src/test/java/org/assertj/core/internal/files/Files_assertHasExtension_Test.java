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

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeFile.shouldBeFile;
import static org.assertj.core.error.ShouldHaveExtension.shouldHaveExtension;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Files.newFile;
import static org.mockito.Mockito.verify;

import java.io.File;

import org.assertj.core.internal.FilesBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for
 * <code>{@link org.assertj.core.internal.Files#assertHasExtension(org.assertj.core.api.AssertionInfo, java.io.File, String)}</code>
 * .
 * 
 * @author Jean-Christophe Gay
 */
class Files_assertHasExtension_Test extends FilesBaseTest {

  private final String expectedExtension = "java";

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String expected = "txt";
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertHasExtension(INFO, null, expected));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_expected_extension_is_null() {
    // GIVEN
    File actual = new File("file.txt");
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.assertHasExtension(INFO, actual, null));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage("The expected extension should not be null.");
  }

  @Test
  void should_throw_error_if_actual_is_not_a_file() {
    // GIVEN
    File actual = tempDir;
    // WHEN
    expectAssertionError(() -> underTest.assertHasExtension(INFO, actual, expectedExtension));
    // THEN
    verify(failures).failure(INFO, shouldBeFile(actual));
  }

  @Test
  void should_throw_error_if_actual_does_not_have_the_expected_extension() {
    // GIVEN
    File actual = newFile(tempDir.getAbsolutePath() + "/file.png");
    // WHEN
    expectAssertionError(() -> underTest.assertHasExtension(INFO, actual, expectedExtension));
    // THEN
    verify(failures).failure(INFO, shouldHaveExtension(actual, "png", expectedExtension));
  }

  @ParameterizedTest
  @ValueSource(strings = { "file", "file." })
  void should_fail_if_actual_has_no_extension(String filename) {
    // GIVEN
    File actual = newFile(tempDir.getAbsolutePath() + "/" + filename);
    String expected = "log";
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertHasExtension(INFO, actual, expected));
    // THEN
    then(error).hasMessage(shouldHaveExtension(actual, expected).create());
  }

  @Test
  void should_pass_if_actual_has_expected_extension() {
    // GIVEN
    File actual = newFile(tempDir.getAbsolutePath() + "/file.java");
    // WHEN/THEN
    underTest.assertHasExtension(INFO, actual, expectedExtension);
  }
}
