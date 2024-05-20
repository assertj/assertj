/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.internal.files;

import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveName.shouldHaveName;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Files.newFile;
import static org.mockito.Mockito.verify;

import java.io.File;
import org.assertj.core.internal.FilesBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * <code>{@link org.assertj.core.internal.Files#assertHasName(org.assertj.core.api.AssertionInfo, java.io.File, String)} </code>
 * .
 * 
 * @author Jean-Christophe Gay
 */
class Files_assertHasName_Test extends FilesBaseTest {

  private String expectedName = "expected.name";

  @Test
  void should_throw_error_if_actual_is_null() {
    // GIVEN
    File actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertHasName(INFO, actual, expectedName));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_throw_npe_if_name_is_null() {
    // GIVEN
    String expectedName = null;
    // WHEN
    NullPointerException npe = catchThrowableOfType(() -> underTest.assertHasName(INFO, actual, expectedName),
                                                    NullPointerException.class);
    // THEN
    then(npe).hasMessage("The expected name should not be null.");
  }

  @Test
  void should_throw_error_if_actual_does_not_have_the_expected_name() {
    // GIVEN
    File actual = newFile(tempDir.getAbsolutePath() + "/not_expected.name");
    // WHEN
    expectAssertionError(() -> underTest.assertHasName(INFO, actual, expectedName));
    // THEN
    verify(failures).failure(INFO, shouldHaveName(actual, expectedName));
  }

  @Test
  void should_pass_if_actual_has_expected_name() {
    File actual = newFile(tempDir.getAbsolutePath() + "/expected.name");
    underTest.assertHasName(INFO, actual, expectedName);
  }
}
