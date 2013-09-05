/*
 * Created on Aug 21, 2012
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
 * Copyright @2006-2012 the original author or authors.
 */
package org.assertj.core.util;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.rules.ExpectedException.none;

import java.io.File;
import java.nio.charset.Charset;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests for {@link Files#contentOf(File, Charset)} and {@link Files#contentOf(File, String)}.
 * 
 * @author Olivier Michallat
 */
public class Files_contentOf_Test {
  @Rule
  public ExpectedException thrown = none();

  private final File sampleFile = new File("src/test/resources/utf8.txt");
  private final String expectedContent = "A text file encoded in UTF-8, with diacritics:"
      + System.getProperty("line.separator") + "é à";

  @Test
  public void should_throw_exception_if_charset_is_null() {
    Charset charset = null;
    thrown.expect(NullPointerException.class);
    Files.contentOf(new File("test"), charset);
  }

  @Test
  public void should_throw_exception_if_charset_name_does_not_exist() {
    thrown.expect(IllegalArgumentException.class);
    Files.contentOf(new File("test"), "Klingon");
  }

  @Test
  public void should_throw_exception_if_file_not_found() {
    File missingFile = new File("missing.txt");
    assertFalse(missingFile.exists());

    thrown.expect(FilesException.class);
    Files.contentOf(missingFile, Charset.defaultCharset());
  }

  @Test
  public void should_load_file_using_charset() {
    // NB: UTF-8 must be supported by every Java implementation
    assertEquals(expectedContent, Files.contentOf(sampleFile, Charset.forName("UTF-8")));
  }

  @Test
  public void should_load_file_using_charset_name() {
    assertEquals(expectedContent, Files.contentOf(sampleFile, "UTF-8"));
  }
}
