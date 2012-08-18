/*
 * Created on Jan 28, 2011
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2011 the original author or authors.
 */
package org.fest.assertions.internal.files;

import static junit.framework.Assert.assertEquals;

import static org.fest.util.Arrays.array;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import org.fest.assertions.internal.Diff;
import org.fest.util.TextFileWriter;

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
    assertEquals(0, diffs.size());
  }

  @Test
  public void should_return_diffs_if_files_do_not_have_equal_content() throws IOException {
    writer.write(actual, "line_0", "line_1");
    writer.write(expected, "line0", "line1");
    List<String> diffs = diff.diff(actual, expected);
    assertEquals(2, diffs.size());
    assertEquals("line:<0>, expected:<line0> but was:<line_0>", diffs.get(0));
    assertEquals("line:<1>, expected:<line1> but was:<line_1>", diffs.get(1));
  }

  @Test
  public void should_return_diffs_if_content_of_actual_is_shorter_than_content_of_expected() throws IOException {
    writer.write(actual, "line_0");
    writer.write(expected, "line_0", "line_1");
    List<String> diffs = diff.diff(actual, expected);
    assertEquals(1, diffs.size());
    assertEquals("line:<1>, expected:<line_1> but was:<EOF>", diffs.get(0));
  }

  @Test
  public void should_return_diffs_if_content_of_actual_is_longer_than_content_of_expected() throws IOException {
    writer.write(actual, "line_0", "line_1");
    writer.write(expected, "line_0");
    List<String> diffs = diff.diff(actual, expected);
    assertEquals(1, diffs.size());
    assertEquals("line:<1>, expected:<EOF> but was:<line_1>", diffs.get(0));
  }
}
