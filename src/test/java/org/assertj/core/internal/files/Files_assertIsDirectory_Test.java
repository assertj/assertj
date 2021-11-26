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

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.error.ShouldBeDirectory.shouldBeDirectory;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Files.newFile;
import static org.mockito.Mockito.verify;

import java.io.File;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Files;
import org.assertj.core.internal.FilesBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Files#assertIsDirectory(AssertionInfo, File)}</code>.
 * 
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
class Files_assertIsDirectory_Test extends FilesBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> files.assertIsDirectory(someInfo(), null))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_is_not_directory() {
    // GIVEN
    File actual = newFile(tempDir.getAbsolutePath() + "/file.txt");
    AssertionInfo info = someInfo();
    // WHEN
    Throwable error = catchThrowable(() -> files.assertIsDirectory(info, actual));
    // THEN
    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeDirectory(actual));
  }

  @Test
  void should_pass_if_actual_is_directory() {
    File actual = tempDir;
    files.assertIsDirectory(someInfo(), actual);
  }
}
