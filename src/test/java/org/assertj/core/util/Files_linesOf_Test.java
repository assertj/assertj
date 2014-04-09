/*
 * Created on Apr 9, 2014
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2006-2014 the original author or authors.
 */
package org.assertj.core.util;

import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import static junit.framework.Assert.*;
import static org.assertj.core.test.ExpectedException.none;

/**
 * Tests for {@link Files#linesOf(File, Charset)} and {@link Files#linesOf(File, String)}.
 * 
 * @author Mateusz Haligowski
 */
public class Files_linesOf_Test {

  private static final File SAMPLE_UNIX_FILE = new File("src/test/resources/utf8.txt");
  private static final File SAMPLE_WIN_FILE = new File("src/test/resources/utf8_win.txt");
  private static final File SAMPLE_MAC_FILE = new File("src/test/resources/utf8_mac.txt");

  private static final List<String> EXPECTED_CONTENT = Lists.newArrayList(
      "A text file encoded in UTF-8, with diacritics:", "é à");

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_throw_exception_when_charset_is_null() {
    Charset charset = null;
    thrown.expect(NullPointerException.class);
    Files.linesOf(SAMPLE_UNIX_FILE, charset);
  }

  @Test
  public void should_throw_exception_if_charset_name_does_not_exist() {
    thrown.expect(IllegalArgumentException.class);
    Files.linesOf(new File("test"), "Klingon");
  }

  @Test
  public void should_throw_exception_if_file_not_found() {
    File missingFile = new File("missing.txt");
    assertFalse(missingFile.exists());

    thrown.expect(FilesException.class);
    Files.linesOf(missingFile, Charset.defaultCharset());
  }

  @Test
  public void should_pass_if_unix_file_is_split_into_lines() {
    assertEquals(EXPECTED_CONTENT, Files.linesOf(SAMPLE_UNIX_FILE, Charset.forName("UTF-8")));
  }

  @Test
  public void should_pass_if_unix_file_is_split_into_lines_using_charset() {
    assertEquals(EXPECTED_CONTENT, Files.linesOf(SAMPLE_UNIX_FILE, "UTF-8"));
  }

  @Test
  public void should_pass_if_windows_file_is_split_into_lines() {
    assertEquals(EXPECTED_CONTENT, Files.linesOf(SAMPLE_WIN_FILE, Charset.forName("UTF-8")));
  }

  @Test
  public void should_pass_if_windows_file_is_split_into_lines_using_charset() {
    assertEquals(EXPECTED_CONTENT, Files.linesOf(SAMPLE_WIN_FILE, "UTF-8"));
  }

  @Test
  public void should_pass_if_mac_file_is_split_into_lines() {
    assertEquals(EXPECTED_CONTENT, Files.linesOf(SAMPLE_MAC_FILE, Charset.forName("UTF-8")));
  }

  @Test
  public void should_pass_if_mac_file_is_split_into_lines_using_charset() {
    assertEquals(EXPECTED_CONTENT, Files.linesOf(SAMPLE_MAC_FILE, "UTF-8"));
  }
}
