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
import static org.assertj.core.error.ShouldBeExecutable.shouldBeExecutable;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.junit.jupiter.api.condition.OS.WINDOWS;
import static org.mockito.Mockito.verify;

import java.io.File;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Files;
import org.assertj.core.internal.FilesBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;

/**
 * Tests for <code>{@link Files#assertIsExecutable(AssertionInfo, File)}</code>.
 *
 * @author Rostyslav Ivankiv
 */
class Files_assertIsExecutable_Test extends FilesBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    File actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertIsExecutable(INFO, actual));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_file_does_not_exist() {
    // GIVEN
    File nonExistentFile = new File("xyz");
    // WHEN
    expectAssertionError(() -> underTest.assertIsExecutable(INFO, nonExistentFile));
    // THEN
    verify(failures).failure(INFO, shouldBeExecutable(nonExistentFile));
  }

  @Test
  @DisabledOnOs(value = WINDOWS, disabledReason = "gh-2312")
  void should_fail_if_actual_is_not_executable() {
    // GIVEN
    File actual = new File("src/test/resources/empty.txt");
    // WHEN
    expectAssertionError(() -> underTest.assertIsExecutable(INFO, actual));
    // THEN
    verify(failures).failure(INFO, shouldBeExecutable(actual));
  }

  @Test
  void should_pass_if_actual_is_executable() {
    // GIVEN
    File actual = new File("src/test/resources/executable_file.sh");
    // WHEN/THEN
    underTest.assertIsExecutable(INFO, actual);
  }

}
