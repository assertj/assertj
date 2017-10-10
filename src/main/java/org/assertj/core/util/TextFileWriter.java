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
package org.assertj.core.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;

/**
 * @author Yvonne Wang
 * @author Olivier Michallat
 */
public class TextFileWriter {
  private static final TextFileWriter INSTANCE = new TextFileWriter();

  public static TextFileWriter instance() {
    return INSTANCE;
  }

  public void write(File file, String... content) throws IOException {
    write(file, Charset.defaultCharset(), content);
  }

  public void write(File file, Charset charset, String... content) throws IOException {
    try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), charset))) {
      for (String line : content) {
        writer.println(line);
      }
    }
  }

  private TextFileWriter() {
  }
}
