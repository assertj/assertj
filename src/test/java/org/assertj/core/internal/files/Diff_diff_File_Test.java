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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.internal.files;

import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.util.Arrays.array;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.assertj.core.internal.Diff;
import org.assertj.core.util.TextFileWriter;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;


/**
 * Tests for <code>{@link Diff#diff(File, File)}</code>.
 * 
 * @author Yvonne Wang
 */
public class Diff_diff_File_Test {

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  private static Diff diff;
  private static TextFileWriter writer;

  @BeforeClass
  public static void setUpOnce() {
    diff = new Diff();
    writer = TextFileWriter.instance();
  }

  private File actual;
  private File expected;

  @Before
  public void setUp() throws IOException {
    actual = folder.newFile("actual.txt");
    expected = folder.newFile("expected.txt");
  }

  @Test
  public void should_return_empty_diff_list_if_files_have_equal_content() throws IOException {
    String[] content = array("line0", "line1");
    writer.write(actual, content);
    writer.write(expected, content);
    List<String> diffs = diff.diff(actual, expected);
    assertThat(diffs).isEmpty();
  }

  @Test
  public void should_return_diffs_if_files_do_not_have_equal_content() throws IOException {
    writer.write(actual, "line_0", "line_1");
    writer.write(expected, "line0", "line1");
    List<String> diffs = diff.diff(actual, expected);
    assertThat(diffs).hasSize(1);
    assertThat(diffs.get(0)).isEqualTo("line:<1>, expected:<line0\nline1> but was:<line_0\nline_1>");
  }

  @Test
  public void should_return_multiple_diffs_if_files_contain_multiple_differences() throws IOException {
    writer.write(actual, "line_0", "line1", "line_2");
    writer.write(expected, "line0", "line1", "line2");
    List<String> diffs = diff.diff(actual, expected);
    assertThat(diffs).hasSize(2);
    assertThat(diffs.get(0)).isEqualTo("line:<1>, expected:<line0> but was:<line_0>");
    assertThat(diffs.get(1)).isEqualTo("line:<3>, expected:<line2> but was:<line_2>");
  }

  @Test
  public void should_return_diffs_if_content_of_actual_is_shorter_than_content_of_expected() throws IOException {
    writer.write(actual, "line_0");
    writer.write(expected, "line_0", "line_1");
    List<String> diffs = diff.diff(actual, expected);
    assertThat(diffs).hasSize(1);
    assertThat(diffs.get(0)).isEqualTo("line:<2>, expected:<line_1> but was:<>");
  }

  @Test
  public void should_return_diffs_if_content_of_actual_is_longer_than_content_of_expected() throws IOException {
    writer.write(actual, "line_0", "line_1");
    writer.write(expected, "line_0");
    List<String> diffs = diff.diff(actual, expected);
    assertThat(diffs).hasSize(1);
    assertThat(diffs.get(0)).isEqualTo("line:<2>, expected:<> but was:<line_1>");
  }
}
