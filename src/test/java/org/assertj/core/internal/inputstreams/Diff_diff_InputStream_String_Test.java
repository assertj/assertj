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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.internal.inputstreams;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.internal.inputstreams.Diff_diff_InputStream_Test.stream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.assertj.core.internal.Diff;
import org.assertj.core.util.diff.Delta;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class Diff_diff_InputStream_String_Test {

  private static Diff diff;

  @BeforeAll
  public static void setUpOnce() {
    diff = new Diff();
  }

  private InputStream actual;
  private String expected;

  @Test
  public void should_return_empty_diff_list_if_inputstreams_have_equal_content() throws IOException {
    // GIVEN
    actual = stream("base", "line0", "line1");
    expected = joinLines("base", "line0", "line1");
    // WHEN
    List<Delta<String>> diffs = diff.diff(actual, expected);
    // THEN
    assertThat(diffs).isEmpty();
  }

  @Test
  public void should_return_diffs_if_inputstreams_do_not_have_equal_content() throws IOException {
    // GIVEN
    actual = stream("base", "line_0", "line_1");
    expected = joinLines("base", "line0", "line1");
    // WHEN
    List<Delta<String>> diffs = diff.diff(actual, expected);
    // THEN
    assertThat(diffs).hasSize(1)
                     .first().hasToString(format("Changed content at line 2:%n"
                                                 + "expecting:%n"
                                                 + "  [\"line0\",%n"
                                                 + "   \"line1\"]%n"
                                                 + "but was:%n"
                                                 + "  [\"line_0\",%n"
                                                 + "   \"line_1\"]%n"));
  }

  @Test
  public void should_return_multiple_diffs_if_inputstreams_contain_multiple_differences() throws IOException {
    // GIVEN
    actual = stream("base", "line_0", "line1", "line_2");
    expected = joinLines("base", "line0", "line1", "line2");
    // WHEN
    List<Delta<String>> diffs = diff.diff(actual, expected);
    // THEN
    assertThat(diffs).hasSize(2);
    assertThat(diffs.get(0)).hasToString(format("Changed content at line 2:%n"
                                                + "expecting:%n"
                                                + "  [\"line0\"]%n"
                                                + "but was:%n"
                                                + "  [\"line_0\"]%n"));
    assertThat(diffs.get(1)).hasToString(format("Changed content at line 4:%n"
                                                + "expecting:%n"
                                                + "  [\"line2\"]%n"
                                                + "but was:%n"
                                                + "  [\"line_2\"]%n"));
  }

  @Test
  public void should_return_diffs_if_content_of_actual_is_shorter_than_content_of_expected() throws IOException {
    // GIVEN
    actual = stream("base", "line_0");
    expected = joinLines("base", "line_0", "line_1");
    // WHEN
    List<Delta<String>> diffs = diff.diff(actual, expected);
    // THEN
    assertThat(diffs).hasSize(1);
    assertThat(diffs.get(0)).hasToString(format("Missing content at line 3:%n"
                                                + "  [\"line_1\"]%n"));
  }

  @Test
  public void should_return_diffs_if_content_of_actual_is_longer_than_content_of_expected() throws IOException {
    // GIVEN
    actual = stream("base", "line_0", "line_1");
    expected = joinLines("base", "line_0");
    // WHEN
    List<Delta<String>> diffs = diff.diff(actual, expected);
    // THEN
    assertThat(diffs).hasSize(1);
    assertThat(diffs.get(0)).hasToString(format("Extra content at line 3:%n"
                                                + "  [\"line_1\"]%n"));
  }

  @Test
  public void should_return_single_diff_line_for_new_line_at_start() throws IOException {
    // GIVEN
    actual = stream("", "line_0", "line_1", "line_2");
    expected = joinLines("line_0", "line_1", "line_2");
    // WHEN
    List<Delta<String>> diffs = diff.diff(actual, expected);
    // THEN
    assertThat(diffs).hasSize(1);
    assertThat(diffs.get(0)).hasToString(format("Extra content at line 1:%n"
                                                + "  [\"\"]%n"));
  }

  static String joinLines(String... lines) {
    return Arrays.stream(lines).collect(joining(System.lineSeparator()));
  }

}
