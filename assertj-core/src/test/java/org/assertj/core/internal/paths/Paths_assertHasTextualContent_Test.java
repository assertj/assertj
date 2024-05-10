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
package org.assertj.core.internal.paths;

import static java.nio.charset.Charset.defaultCharset;
import static java.nio.file.Files.createFile;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeReadable.shouldBeReadable;
import static org.assertj.core.error.ShouldExist.shouldExist;
import static org.assertj.core.error.ShouldHaveContent.shouldHaveContent;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.junit.jupiter.api.condition.OS.WINDOWS;
import static org.mockito.BDDMockito.willThrow;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.assertj.core.internal.PathsBaseTest;
import org.assertj.core.util.diff.Delta;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;

/**
 * @author Olivier Michallat
 * @author Joel Costigliola
 */
class Paths_assertHasTextualContent_Test extends PathsBaseTest {

  private static final Charset CHARSET = defaultCharset();

  @Test
  void should_fail_if_expected_is_null() throws IOException {
    // GIVEN
    Path actual = createFile(tempDir.resolve("actual"));
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.assertHasTextualContent(INFO, actual, null, CHARSET));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage("The text to compare to should not be null");
  }

  @Test
  void should_fail_if_actual_is_null() throws IOException {
    // GIVEN
    String expected = "expected";
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertHasTextualContent(INFO, null, expected, CHARSET));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_exist() throws IOException {
    // GIVEN
    Path actual = tempDir.resolve("non-existent");
    String expected = "expected";
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertHasTextualContent(INFO, actual, expected, CHARSET));
    // THEN
    then(error).hasMessage(shouldExist(actual).create());
  }

  @Test
  @DisabledOnOs(value = WINDOWS, disabledReason = "gh-2312")
  void should_fail_if_actual_is_not_readable() throws IOException {
    // GIVEN
    Path actual = createFile(tempDir.resolve("actual"));
    actual.toFile().setReadable(false);
    String expected = "expected";
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertHasTextualContent(INFO, actual, expected, CHARSET));
    // THEN
    then(error).hasMessage(shouldBeReadable(actual).create());
  }

  @Test
  void should_pass_if_actual_has_expected_textual_content() throws IOException {
    // GIVEN
    Path actual = Files.write(tempDir.resolve("actual"), "Content".getBytes(CHARSET));
    String expected = "Content";
    // WHEN/THEN
    underTest.assertHasTextualContent(INFO, actual, expected, CHARSET);
  }

  @Test
  void should_fail_if_actual_does_not_have_expected_textual_content() throws IOException {
    // GIVEN
    Path actual = Files.write(tempDir.resolve("actual"), "Content".getBytes(CHARSET));
    String expected = "Another content";
    List<Delta<String>> diffs = diff.diff(actual, expected, CHARSET);
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertHasTextualContent(INFO, actual, expected, CHARSET));
    // THEN
    then(error).hasMessage(shouldHaveContent(actual, CHARSET, diffs).create(INFO.description(), INFO.representation()));
  }

  @Test
  void should_rethrow_IOException_as_UncheckedIOException() throws IOException {
    // GIVEN
    Path actual = createFile(tempDir.resolve("actual"));
    String expected = "expected";
    IOException exception = new IOException("boom!");
    willThrow(exception).given(diff).diff(actual, expected, CHARSET);
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.assertHasTextualContent(INFO, actual, expected, CHARSET));
    // THEN
    then(thrown).isInstanceOf(UncheckedIOException.class)
                .hasMessage("Unable to verify text contents of path:<%s>", actual)
                .hasCause(exception);
  }

}
