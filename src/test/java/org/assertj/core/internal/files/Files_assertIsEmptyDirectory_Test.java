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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.internal.files;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldBeDirectory.shouldBeDirectory;
import static org.assertj.core.error.ShouldBeEmptyDirectory.shouldBeEmptyDirectory;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Files.newFile;
import static org.assertj.core.util.Files.newFolder;
import static org.assertj.core.util.Lists.list;
import static org.mockito.Mockito.verify;

import java.io.File;
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
    files.assertIsEmptyDirectory(INFO, actual);
  }

  @Test
  void should_fail_if_actual_is_not_empty() {
    // GIVEN
    File actual = newFolder(tempDir.getAbsolutePath() + "/folder");
    File file = newFile(actual.getAbsolutePath() + "/Test.java");
    List<File> items = list(file);
    // WHEN
    expectAssertionError(() -> files.assertIsEmptyDirectory(INFO, actual));
    // THEN
    verify(failures).failure(INFO, shouldBeEmptyDirectory(actual, items));
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    File actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> files.assertIsEmptyDirectory(INFO, actual));
    // THEN
    assertThat(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_exist() {
    // GIVEN
    File actual = new File("xyz");
    // WHEN
    expectAssertionError(() -> files.assertIsEmptyDirectory(INFO, actual));
    // THEN
    verify(failures).failure(INFO, shouldBeDirectory(actual));
  }

  @Test
  void should_fail_if_actual_exists_but_is_not_a_directory() {
    // GIVEN
    File actual = newFile(tempDir.getAbsolutePath() + "/Test.java");
    // WHEN
    expectAssertionError(() -> files.assertIsEmptyDirectory(INFO, actual));
    // THEN
    verify(failures).failure(INFO, shouldBeDirectory(actual));
  }

  @Test
  void should_throw_error_on_null_listing() {
    // GIVEN
    File actual = newFolder(tempDir.getAbsolutePath() + "/folder");
    actual.setReadable(false);
    // WHEN
    Throwable error = catchThrowable(() -> files.assertIsEmptyDirectory(INFO, actual));
    // THEN
    assertThat(error).isInstanceOf(NullPointerException.class)
                     .hasMessage("Directory listing should not be null");
  }
}
