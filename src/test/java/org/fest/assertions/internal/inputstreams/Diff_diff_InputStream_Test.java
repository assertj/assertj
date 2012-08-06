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
package org.fest.assertions.internal.inputstreams;

import static junit.framework.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import org.fest.assertions.internal.Diff;

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
    List<String> diffs = diff.diff(actual, expected);
    assertEquals(0, diffs.size());
  }

  @Test
  public void should_return_diffs_if_inputstreams_do_not_have_equal_content() throws IOException {
    actual = stream("base", "line_0", "line_1");
    expected = stream("base", "line0", "line1");
    List<String> diffs = diff.diff(actual, expected);
    assertEquals(2, diffs.size());
    assertEquals("line:<1>, expected:<line0> but was:<line_0>", diffs.get(0));
    assertEquals("line:<2>, expected:<line1> but was:<line_1>", diffs.get(1));
  }

  @Test
  public void should_return_diffs_if_content_of_actual_is_shorter_than_content_of_expected() throws IOException {
    actual = stream("base", "line_0");
    expected = stream("base", "line_0", "line_1");
    List<String> diffs = diff.diff(actual, expected);
    assertEquals(1, diffs.size());
    assertEquals("line:<2>, expected:<line_1> but was:<EOF>", diffs.get(0));
  }

  @Test
  public void should_return_diffs_if_content_of_actual_is_longer_than_content_of_expected() throws IOException {
    actual = stream("base", "line_0", "line_1");
    expected = stream("base", "line_0");
    List<String> diffs = diff.diff(actual, expected);
    assertEquals(1, diffs.size());
    assertEquals("line:<2>, expected:<EOF> but was:<line_1>", diffs.get(0));
  }
}
