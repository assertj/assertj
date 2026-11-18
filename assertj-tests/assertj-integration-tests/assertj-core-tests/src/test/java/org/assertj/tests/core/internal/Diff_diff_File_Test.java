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
import static org.assertj.core.api.BDDAssertions.then;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.assertj.core.internal.Diff;
import org.assertj.core.util.diff.Delta;
import org.assertj.tests.core.testkit.PathFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/// Tests for [Diff#diff(File, java.nio.charset.Charset, File, java.nio.charset.Charset)].
///
/// @author Yvonne Wang
class Diff_diff_File_Test {

  private final Diff diff = new Diff();

  @TempDir
  Path tempDir;

  @Test
  void should_return_empty_diff_list_if_files_have_equal_content() throws IOException {
    // GIVEN
    File actual = createFile("line0", "line1");
    File expected = createFile("line0", "line1");
    // WHEN
    List<Delta<String>> diffs = diff.diff(actual, defaultCharset(), expected, defaultCharset());
    // THEN
    then(diffs).isEmpty();
  }

  @Test
  void should_return_diffs_if_files_do_not_have_equal_content() throws IOException {
    // GIVEN
    File actual = createFile("line_0", "line_1");
    File expected = createFile("line0", "line1");
    // WHEN
    List<Delta<String>> diffs = diff.diff(actual, defaultCharset(), expected, defaultCharset());
    // THEN
    then(diffs).singleElement().hasToString("""
        Changed content at line 1:
        expecting:
          ["line0",
           "line1"]
        but was:
          ["line_0",
           "line_1"]
        """.replace("\n", System.lineSeparator()));
  }

  @Test
  void should_return_multiple_diffs_if_files_contain_multiple_differences() throws IOException {
    // GIVEN
    File actual = createFile("line_0", "line1", "line_2");
    File expected = createFile("line0", "line1", "line2");
    // WHEN
    List<Delta<String>> diffs = diff.diff(actual, defaultCharset(), expected, defaultCharset());
    // THEN
    then(diffs).hasSize(2);
    then(diffs.get(0)).hasToString("""
        Changed content at line 1:
        expecting:
          ["line0"]
        but was:
          ["line_0"]
        """.replace("\n", System.lineSeparator()));
    then(diffs.get(1)).hasToString("""
        Changed content at line 3:
        expecting:
          ["line2"]
        but was:
          ["line_2"]
        """.replace("\n", System.lineSeparator()));
  }

  @Test
  void should_be_able_to_detect_mixed_differences() throws IOException {
    // GIVEN
    // @format:off
    File actual   = createFile("line1",                     "line2", "line3", "line4", "line5", "line 9", "line 10", "line 11");
    File expected = createFile("line1", "line1a", "line1b", "line2", "line3", "line7", "line5");
    // @format:on
    // WHEN
    List<Delta<String>> diffs = diff.diff(actual, defaultCharset(), expected, defaultCharset());
    // THEN
    then(diffs).hasSize(3);
    then(diffs.get(0)).hasToString("""
        Missing content at line 2:
          ["line1a",
           "line1b"]
        """.replace("\n", System.lineSeparator()));
    then(diffs.get(1)).hasToString("""
        Changed content at line 6:
        expecting:
          ["line7"]
        but was:
          ["line4"]
        """.replace("\n", System.lineSeparator()));
    then(diffs.get(2)).hasToString("""
        Extra content at line 8:
          ["line 9",
           "line 10",
           "line 11"]
        """.replace("\n", System.lineSeparator()));
  }

  @Test
  void should_return_diffs_if_content_of_actual_is_shorter_than_content_of_expected() throws IOException {
    // GIVEN
    File actual = createFile("line_0");
    File expected = createFile("line_0", "line_1");
    // WHEN
    List<Delta<String>> diffs = diff.diff(actual, defaultCharset(), expected, defaultCharset());
    // THEN
    then(diffs).singleElement().hasToString("""
        Missing content at line 2:
          ["line_1"]
        """.replace("\n", System.lineSeparator()));
  }

  @Test
  void should_return_diffs_if_content_of_actual_is_longer_than_content_of_expected() throws IOException {
    // GIVEN
    File actual = createFile("line_0", "line_1");
    File expected = createFile("line_0");
    // WHEN
    List<Delta<String>> diffs = diff.diff(actual, defaultCharset(), expected, defaultCharset());
    // THEN
    then(diffs).singleElement().hasToString("""
        Extra content at line 2:
          ["line_1"]
        """.replace("\n", System.lineSeparator()));
  }

  // writes each line followed by the platform line separator
  private File createFile(String... lines) throws IOException {
    return Files.write(PathFactory.createPath(tempDir), List.of(lines), defaultCharset()).toFile();
  }

}
