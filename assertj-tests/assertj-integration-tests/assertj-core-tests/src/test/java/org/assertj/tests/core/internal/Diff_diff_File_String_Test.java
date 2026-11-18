/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.internal;

import static java.nio.charset.Charset.defaultCharset;
import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.BDDAssertions.then;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.assertj.core.internal.Diff;
import org.assertj.core.util.diff.Delta;
import org.assertj.tests.core.testkit.PathFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/// Tests for [Diff#diff(File, String, Charset)].
///
/// @author Olivier Michallat
class Diff_diff_File_String_Test {

  private final Diff diff = new Diff();

  @TempDir
  Path tempDir;

  @Test
  void should_return_empty_diff_list_if_file_and_string_have_equal_content() throws IOException {
    // GIVEN
    File actual = createFile(defaultCharset(), "line0", "line1");
    String expected = "line0%nline1%n".formatted();
    // WHEN
    List<Delta<String>> diffs = diff.diff(actual, expected, defaultCharset());
    // THEN
    then(diffs).isEmpty();
  }

  @Test
  void should_return_diffs_if_file_and_string_do_not_have_equal_content() throws IOException {
    // GIVEN
    File actual = createFile(UTF_8, "Touché");
    String expected = "Touché%n".formatted();
    // WHEN
    List<Delta<String>> diffs = diff.diff(actual, expected, ISO_8859_1);
    // THEN
    then(diffs).singleElement().hasToString("""
        Changed content at line 1:
        expecting:
          ["Touché"]
        but was:
          ["TouchÃ©"]
        """.replace("\n", System.lineSeparator()));
  }

  @Test
  void should_return_diffs_if_content_of_actual_is_shorter_than_content_of_expected() throws IOException {
    // GIVEN
    File actual = createFile(defaultCharset(), "line_0");
    String expected = "line_0%nline_1%n".formatted();
    // WHEN
    List<Delta<String>> diffs = diff.diff(actual, expected, defaultCharset());
    // THEN
    then(diffs).singleElement().hasToString("""
        Missing content at line 2:
          ["line_1"]
        """.replace("\n", System.lineSeparator()));
  }

  @Test
  void should_return_diffs_if_content_of_actual_is_longer_than_content_of_expected() throws IOException {
    // GIVEN
    File actual = createFile(defaultCharset(), "line_0", "line_1");
    String expected = "line_0%n".formatted();
    // WHEN
    List<Delta<String>> diffs = diff.diff(actual, expected, defaultCharset());
    // THEN
    then(diffs).singleElement().hasToString("""
        Extra content at line 2:
          ["line_1"]
        """.replace("\n", System.lineSeparator()));
  }

  // writes each line followed by the platform line separator
  private File createFile(Charset charset, String... lines) throws IOException {
    return Files.write(PathFactory.createPath(tempDir), List.of(lines), charset).toFile();
  }

}
