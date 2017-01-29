/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.internal.inputstreams;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.assertj.core.internal.Diff;
import org.assertj.core.util.diff.Delta;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for <code>{@link Diff#diff(InputStream, InputStream)}</code>.
 * 
 * @author Matthieu Baechler
 */
public class Diff_diff_InputStream_Test {

  private final static String LINE_SEPARATOR = System.getProperty("line.separator");
  private static Diff diff;

  @BeforeClass
  public static void setUpOnce() {
    diff = new Diff();
  }

  private InputStream actual;
  private InputStream expected;

  private InputStream stream(String... lines) throws UnsupportedEncodingException {
    StringBuilder stringBuilder = new StringBuilder();
    for (String line : lines) {
      stringBuilder.append(line).append(LINE_SEPARATOR);
    }
    return new ByteArrayInputStream(stringBuilder.toString().getBytes("ASCII"));
  }

  @Test
  public void should_return_empty_diff_list_if_inputstreams_have_equal_content() throws IOException {
    actual = stream("base", "line0", "line1");
    expected = stream("base", "line0", "line1");
    List<Delta<String>> diffs = diff.diff(actual, expected);
    assertThat(diffs).isEmpty();
  }

  @Test
  public void should_return_diffs_if_inputstreams_do_not_have_equal_content() throws IOException {
    // GIVEN
    actual = stream("base", "line_0", "line_1");
    expected = stream("base", "line0", "line1");
    // WHEN
    List<Delta<String>> diffs = diff.diff(actual, expected);
    // THEN
    assertThat(diffs).hasSize(1);
    assertThat(diffs.get(0)).hasToString(format("Changed content at line 2:%n"
                                                + "expecting:%n"
                                                + "  [\"line0\",%n"
                                                + "   \"line1\"]%n"
                                                + "but was:%n"
                                                + "  [\"line_0\",%n"
                                                + "   \"line_1\"]%n"));
  }

  @Test
  public void should_return_multiple_diffs_if_inputstreams_contain_multiple_differences() throws IOException {
    actual = stream("base", "line_0", "line1", "line_2");
    expected = stream("base", "line0", "line1", "line2");
    List<Delta<String>> diffs = diff.diff(actual, expected);
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
    actual = stream("base", "line_0");
    expected = stream("base", "line_0", "line_1");
    List<Delta<String>> diffs = diff.diff(actual, expected);
    assertThat(diffs).hasSize(1);
    assertThat(diffs.get(0)).hasToString(format("Missing content at line 3:%n"
                                                + "  [\"line_1\"]%n"));
  }

  @Test
  public void should_return_diffs_if_content_of_actual_is_longer_than_content_of_expected() throws IOException {
    actual = stream("base", "line_0", "line_1");
    expected = stream("base", "line_0");
    List<Delta<String>> diffs = diff.diff(actual, expected);
    assertThat(diffs).hasSize(1);
    assertThat(diffs.get(0)).hasToString(format("Extra content at line 3:%n"
                                                + "  [\"line_1\"]%n"));
  }

  @Test
  public void should_return_single_diff_line_for_new_line_at_start() throws IOException {
    actual = stream("", "line_0", "line_1", "line_2");
    expected = stream("line_0", "line_1", "line_2");
    List<Delta<String>> diffs = diff.diff(actual, expected);
    assertThat(diffs).hasSize(1);
    assertThat(diffs.get(0)).hasToString(format("Extra content at line 1:%n"
                                                + "  [\"\"]%n"));
  }
}
