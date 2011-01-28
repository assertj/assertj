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

import static org.mockito.Mockito.*;

import java.io.*;

import org.junit.*;

/**
 * Tests for <code>{@link FileReader#close()}</code>.
 *
 * @author Yvonne Wang
 */
public class FileReader_close_Test {

  private InputStream inputStream;
  private FileReader reader;

  @Before public void setUp() {
    inputStream = mock(InputStream.class);
    reader = new FileReader(inputStream);
  }

  @Test public void should_close_internal_InputStream() throws IOException {
    reader.close();
    verify(inputStream).close();
  }
}
