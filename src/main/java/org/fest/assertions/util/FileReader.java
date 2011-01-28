/*
 * Created on Jan 26, 2011
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

import java.io.*;

import org.fest.util.VisibleForTesting;

/**
 * A buffered character-input stream that keeps track of line numbers.
 *
 * @author Yvonne Wang
 */
public class FileReader implements Closeable {

  /**
   * Creates a new <code>{@link FileReader}</code>.
   * @param file the file to read.
   * @return the created {@code FileReader}.
   * @throws IOException if an I/O error occurs.
   */
  public static FileReader readerFor(File file) throws IOException {
    return new FileReader(new FileInputStream(file));
  }

  private final InputStream inputStream;
  private final LineNumberReader reader;

  @VisibleForTesting FileReader(InputStream inputStream) {
    this.inputStream = inputStream;
    this.reader = new LineNumberReader(new BufferedReader(new InputStreamReader(inputStream)));
  }

  /**
   * Indicates whether this stream is ready to be read. A buffered character stream is ready if the buffer is not empty,
   * or if the underlying character stream is ready.
   * @return {@code true} if the next read is guaranteed not to block for input, {@code false} otherwise. Note that
   * returning {@code false} does not guarantee that the next read will block.
   * @throws IOException if an I/O error occurs.
   */
  public boolean isReady() throws IOException {
    return reader.ready();
  }

  /**
   * Returns the current line number. Counting starts from zero.
   * @return the current line number.
   */
  public int lineNumber() {
    return reader.getLineNumber();
  }

  /**
   * Read a line of text. A line is considered to be terminated by any one of a line feed ('\n'), a carriage return
   * ('\r'), or a carriage return followed immediately by a line feed.
   * @return a {@code String} containing the contents of the line, not including any line-termination characters, or
   * {@code null} if the end of the stream has been reached
   * @throws IOException if an I/O error occurs.
   */
  public String readLine() throws IOException {
    return reader.readLine();
  }

  /** {@inheritDoc} */
  public void close() throws IOException {
    inputStream.close();
  }
}
