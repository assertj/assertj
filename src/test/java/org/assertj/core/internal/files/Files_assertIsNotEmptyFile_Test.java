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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.internal.files;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Files;
import org.assertj.core.internal.FilesBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeFile.shouldBeFile;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.verifyNoInteractions;

/**
 * Tests for <code>{@link Files#assertIsNotEmptyFile(AssertionInfo, File)}</code>
 */
@DisplayName("Files assertIsNotEmptyFile")
class Files_assertIsNotEmptyFile_Test extends FilesBaseTest {

  @Test
  void should_pass_if_actual_is_not_empty() {
    // GIVEN
    given(actual.isFile()).willReturn(true);
    given(actual.length()).willReturn(1L);
    // WHEN
    files.assertIsNotEmptyFile(INFO, actual);
    // THEN
    verifyNoInteractions(failures);
  }

  @Test
  void should_fail_if_actual_is_empty() {
    // GIVEN
    given(actual.isFile()).willReturn(true);
    given(actual.length()).willReturn(0L);
    // WHEN
    expectAssertionError(() -> files.assertIsNotEmptyFile(INFO, actual));
    // THEN
    verify(failures).failure(INFO, shouldNotBeEmpty(actual));
  }

  @Test
  void should_fail_if_actual_is_a_directory() {
    // GIVEN
    given(actual.isFile()).willReturn(false);
    given(actual.length()).willReturn(1L);
    // WHEN
    expectAssertionError(() -> files.assertIsNotEmptyFile(INFO, actual));
    // THEN
    verify(failures).failure(INFO, shouldBeFile(actual));
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    File actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> files.assertIsNotEmptyFile(INFO, actual));
    // THEN
    then(error).hasMessage(actualIsNull());
  }
}
