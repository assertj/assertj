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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldBeFile.shouldBeFile;
import static org.assertj.core.error.ShouldHaveSize.shouldHaveSize;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.io.File;
import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Files;
import org.assertj.core.internal.FilesBaseTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Files#assertHasSize(AssertionInfo, File, long)}</code>
 *
 * @author Krishna Chaithanya Ganta
 */
@DisplayName("FileAssert hasSize")
public class File_assertHasSize_Test extends FilesBaseTest {

  private static File actual;

  @BeforeAll
  public static void setUpOnce() {
    actual = new File("src/test/resources/actual_file.txt");
  }

  @Test
  public void should_throw_error_if_actual_is_null() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> files.assertHasSize(someInfo(), null, actual.length()));
    // THEN
    assertThat(assertionError).hasMessage(actualIsNull());
  }

  @Test
  public void should_throw_error_if_actual_file_does_not_have_the_expected_size() {
    // GIVEN
    AssertionInfo info = someInfo();
    // WHEN
    expectAssertionError(() -> files.assertHasSize(info, actual, 36L));
    // THEN
    verify(failures).failure(info, shouldHaveSize(actual, 36L));
  }

  @Test
  public void should_fail_if_actual_is_not_file() {
    // GIVEN
    AssertionInfo info = someInfo();
    File notAFile = new File("xyz");
    // WHEN
    expectAssertionError(() -> files.assertHasSize(info, notAFile, 36L));
    // THEN
    verify(failures).failure(info, shouldBeFile(notAFile));
  }

  @Test
  public void should_pass_if_actual_has_expected_size() {
    files.assertHasSize(someInfo(), actual, actual.length());
  }
}
