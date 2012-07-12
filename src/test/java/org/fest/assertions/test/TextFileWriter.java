/*
 * Created on Jan 27, 2011
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
package org.fest.assertions.test;

import static org.fest.util.Closeables.close;
import static org.fest.util.Flushables.flush;

import java.io.*;

/**
 * @author Yvonne Wang
 */
public class TextFileWriter {

  private static TextFileWriter INSTANCE = new TextFileWriter();

  public static TextFileWriter instance() {
    return INSTANCE;
  }

  public void write(File file, String... content) throws IOException {
    PrintWriter writer = null;
    try {
      writer = new PrintWriter(new FileWriter(file));
      for (String line : content)
        writer.println(line);
    } finally {
      flush(writer);
      close(writer);
    }
  }

  private TextFileWriter() {}
}
