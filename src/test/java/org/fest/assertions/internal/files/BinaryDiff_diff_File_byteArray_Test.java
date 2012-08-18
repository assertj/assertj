/*
 * Created on Jul 20, 2012
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
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import org.fest.assertions.internal.BinaryDiff;
import org.fest.assertions.internal.BinaryDiffResult;
import org.fest.util.TextFileWriter;

/**
 * Tests for <code>{@link BinaryDiff#diff(java.io.File, byte[])}</code>.
 * 
 * @author Olivier Michallat
 * @author Joel Costigliola
 */
public class BinaryDiff_diff_File_byteArray_Test {

  private static final String LINE_SEPARATOR = System.getProperty("line.separator");

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  private static BinaryDiff binaryDiff;
  private static TextFileWriter writer;

  @BeforeClass
  public static void setUpOnce() {
    binaryDiff = new BinaryDiff();
    writer = TextFileWriter.instance();
  }

  private File actual;
  private byte[] expected;

  @Before
  public void setUp() throws IOException {
    actual = folder.newFile("actual.txt");
  }

  @Test
  public void should_return_no_diff_if_file_and_array_have_equal_content() throws IOException {
    writer.write(actual, "test");
    // Note: writer inserts a new line after each line so we need it in our expected content
    expected = ("test" + LINE_SEPARATOR).getBytes();
    BinaryDiffResult result = binaryDiff.diff(actual, expected);
    assertTrue(result.hasNoDiff());
  }

  @Test
  public void should_return_diff_if_inputstreams_differ_on_one_byte() throws IOException {
    writer.write(actual, "test");
    expected = ("fest" + LINE_SEPARATOR).getBytes();
    BinaryDiffResult result = binaryDiff.diff(actual, expected);
    assertEquals(0, result.offset);
    assertEquals("0x74", result.actual);
    assertEquals("0x66", result.expected);
  }

  @Test
  public void should_return_diff_if_actual_is_shorter() throws IOException {
    writer.write(actual, "foo");
    expected = ("foo" + LINE_SEPARATOR + "bar").getBytes();
    BinaryDiffResult result = binaryDiff.diff(actual, expected);
    assertEquals(3 + LINE_SEPARATOR.length(), result.offset);
    assertEquals("EOF", result.actual);
    assertEquals("0x62", result.expected);
  }

  @Test
  public void should_return_diff_if_expected_is_shorter() throws IOException {
    writer.write(actual, "foobar");
    expected = "foo".getBytes();
    BinaryDiffResult result = binaryDiff.diff(actual, expected);
    assertEquals(3, result.offset);
    assertEquals("0x62", result.actual);
    assertEquals("EOF", result.expected);
  }
}
