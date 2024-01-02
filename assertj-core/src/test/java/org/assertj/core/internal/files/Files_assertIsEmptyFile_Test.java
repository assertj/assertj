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
import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.error.ShouldBeFile.shouldBeFile;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Files.newFile;
import static org.assertj.core.util.Files.newFolder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.io.File;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Files;
import org.assertj.core.internal.FilesBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Files#assertIsEmptyFile(AssertionInfo, File)}</code>
 */
class Files_assertIsEmptyFile_Test extends FilesBaseTest {

  @Test
  void should_pass_if_actual_is_empty() {
    // GIVEN
    File actual = newFile(tempDir.getAbsolutePath() + "/Test.java");
    // WHEN
    underTest.assertIsEmptyFile(INFO, actual);
    // THEN
    verifyNoInteractions(failures);
  }

  @Test
  void should_fail_if_actual_is_not_empty() {
    // GIVEN
    File actual = new File("src/test/resources/actual_file.txt");
    // WHEN
    expectAssertionError(() -> underTest.assertIsEmptyFile(INFO, actual));
    // THEN
    verify(failures).failure(INFO, shouldBeEmpty(actual));
  }

  @Test
  void should_fail_if_actual_is_a_directory() {
    // GIVEN
    File actual = newFolder(tempDir.getAbsolutePath() + "/folder");
    // WHEN
    expectAssertionError(() -> underTest.assertIsEmptyFile(INFO, actual));
    // THEN
    verify(failures).failure(INFO, shouldBeFile(actual));
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    File actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertIsEmptyFile(INFO, actual));
    // THEN
    then(error).hasMessage(actualIsNull());
  }
}
