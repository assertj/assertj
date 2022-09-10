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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.internal.files;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotExist.shouldNotExist;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.io.File;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Files;
import org.assertj.core.internal.FilesBaseTest;
import org.assertj.core.util.ResourceUtil;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Files#assertDoesNotExist(AssertionInfo, File)}</code>.
 * 
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
class Files_assertDoesNotExist_Test extends FilesBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    File actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> files.assertDoesNotExist(INFO, actual));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_exists() {
    // GIVEN
    File actual = ResourceUtil.getResource("actual_file.txt").toFile();
    // WHEN
    expectAssertionError(() -> files.assertDoesNotExist(INFO, actual));
    // THEN
    verify(failures).failure(INFO, shouldNotExist(actual));
  }

  @Test
  void should_pass_if_actual_does_not_exist() {
    File actual = new File("xyz");
    files.assertDoesNotExist(INFO, actual);
  }
}
