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

import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeFile.shouldBeFile;
import static org.assertj.core.error.ShouldHaveContent.shouldHaveContent;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.util.List;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Diff;
import org.assertj.core.internal.Files;
import org.assertj.core.internal.FilesBaseTest;
import org.assertj.core.util.ResourceUtil;
import org.assertj.core.util.diff.Delta;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Files#assertHasContent(AssertionInfo, File, String, Charset)}</code>.
 * 
 * @author Olivier Michallat
 * @author Joel Costigliola
 */
class Files_assertHasContent_Test extends FilesBaseTest {

  private static File actual;
  private static String expected;
  private static Charset charset;

  @BeforeAll
  static void setUpOnce() {
    // Does not matter if the values differ, the actual comparison is mocked in this test
    actual = ResourceUtil.getResource("actual_file.txt").toFile();
    expected = "xyz";
    charset = Charset.defaultCharset();
  }

  @Test
  void should_throw_error_if_expected_is_null() {
    // GIVEN
    String expectedContent = null;
    // WHEN
    NullPointerException npe = catchThrowableOfType(() -> files.assertHasContent(INFO, actual, expectedContent, charset),
                                                    NullPointerException.class);
    // THEN
    then(npe).hasMessage("The text to compare to should not be null");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    File actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> files.assertHasContent(INFO, actual, expected, charset));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_is_not_file() {
    // GIVEN
    File notAFile = new File("xyz");
    // WHEN
    expectAssertionError(() -> files.assertHasContent(INFO, notAFile, expected, charset));
    // THEN
    verify(failures).failure(INFO, shouldBeFile(notAFile));
  }

  @Test
  void should_pass_if_file_has_text_content() {
    String expected = "actual";
    files.assertHasContent(INFO, actual, expected, charset);
  }

  @Test
  void should_throw_error_wrapping_caught_IOException() throws IOException {
    // GIVEN
    IOException cause = new IOException();
    when(diff.diff(actual, expected, charset)).thenThrow(cause);
    // WHEN
    UncheckedIOException uioe = catchThrowableOfType(() -> files.assertHasContent(INFO, actual, expected, charset),
                                                     UncheckedIOException.class);
    // THEN
    then(uioe).hasCause(cause);
  }

  @Test
  void should_fail_if_file_does_not_have_expected_text_content() throws IOException {
    // GIVEN
    Diff diff = new Diff();
    List<Delta<String>> diffs = diff.diff(actual, expected, charset);
    // WHEN
    expectAssertionError(() -> unMockedFiles.assertHasContent(INFO, actual, expected, charset));
    // THEN
    verify(unMockedFailures).failure(INFO, shouldHaveContent(actual, charset, diffs));
  }
}
