/*
 * Created on Jan 27, 2011
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
 * Copyright @2011 the original author or authors.
 */
package org.fest.assertions.util;

import static junit.framework.Assert.*;
import static org.fest.util.Arrays.array;

import java.io.*;

import org.fest.assertions.test.TextFileWriter;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

/**
 * Tests for:
 * <ul>
 * <li><code>{@link FileReader#readerFor(File)}</code></li>
 * <li><code>{@link FileReader#isReady()}</code></li>
 * <li><code>{@link FileReader#lineNumber()}</code></li>
 * <li><code>{@link FileReader#readLine()}</code></li>
 * </ul>
 *
 * @author Yvonne Wang
 */
public class FileReader_Test {

  @Rule public TemporaryFolder folder = new TemporaryFolder();

  private File file;
  private String[] fileContent;
  private FileReader reader;

  @Before public void setUp() throws IOException {
    file = folder.newFile("FileReader_Test.txt");
    fileContent = array("line0", "line1", "line2");
    TextFileWriter.instance().write(file, fileContent);
    reader = FileReader.readerFor(file);
  }

  @After public void tearDown() throws IOException {
    if (reader != null) reader.close();
  }

  @Test public void should_read_file_providing_line_number() throws IOException {
    int lineCount = fileContent.length;
    for (int i = 0; i < lineCount; i++) {
      assertTrue(reader.isReady());
      assertEquals(i, reader.lineNumber());
      assertEquals(fileContent[i], reader.readLine());
    }
    assertFalse(reader.isReady());
  }
}
