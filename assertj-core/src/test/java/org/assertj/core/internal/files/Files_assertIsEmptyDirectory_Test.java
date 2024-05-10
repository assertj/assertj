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

import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeDirectory.shouldBeDirectory;
import static org.assertj.core.error.ShouldBeEmptyDirectory.shouldBeEmptyDirectory;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Files.newFile;
import static org.assertj.core.util.Files.newFolder;
import static org.assertj.core.util.Lists.list;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

import org.assertj.core.internal.FilesBaseTest;
import org.junit.jupiter.api.Test;

/**
 * @author Valeriy Vyrva
 */
class Files_assertIsEmptyDirectory_Test extends FilesBaseTest {

  @Test
  void should_pass_if_actual_is_empty() {
    // GIVEN
    File actual = newFolder(tempDir.getAbsolutePath() + "/folder");
    // THEN
    underTest.assertIsEmptyDirectory(INFO, actual);
  }

  @Test
  void should_fail_if_actual_is_not_empty() {
    // GIVEN
    File actual = newFolder(tempDir.getAbsolutePath() + "/folder");
    File file = newFile(actual.getAbsolutePath() + "/Test.java");
    List<File> items = list(file);
    // WHEN
    expectAssertionError(() -> underTest.assertIsEmptyDirectory(INFO, actual));
    // THEN
    verify(failures).failure(INFO, shouldBeEmptyDirectory(actual, items));
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    File actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertIsEmptyDirectory(INFO, actual));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_exist() {
    // GIVEN
    File actual = new File("xyz");
    // WHEN
    expectAssertionError(() -> underTest.assertIsEmptyDirectory(INFO, actual));
    // THEN
    verify(failures).failure(INFO, shouldBeDirectory(actual));
  }

  @Test
  void should_fail_if_actual_exists_but_is_not_a_directory() {
    // GIVEN
    File actual = newFile(tempDir.getAbsolutePath() + "/Test.java");
    // WHEN
    expectAssertionError(() -> underTest.assertIsEmptyDirectory(INFO, actual));
    // THEN
    verify(failures).failure(INFO, shouldBeDirectory(actual));
  }

  // use mock as it's hard to simulate listFiles(FileFilter.class) to return null
  @Test
  void should_throw_error_on_null_listing() {
    // GIVEN
    File actual = mock(File.class);
    given(actual.exists()).willReturn(true);
    given(actual.isDirectory()).willReturn(true);
    given(actual.listFiles(any(FileFilter.class))).willReturn(null);
    // WHEN
    Throwable error = catchThrowableOfType(() -> underTest.assertIsEmptyDirectory(INFO, actual), NullPointerException.class);
    // THEN
    then(error).hasMessage("Directory listing should not be null");
  }
}
