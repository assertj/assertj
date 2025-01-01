/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.util;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.util.Preconditions.checkArgument;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Utility methods related to {@link Path}s.
 *
 * @author Stefan Bratanov
 *
 * @since 3.23.0
 */
public class Paths {

  private Paths() {}

  /**
   * Loads the text content of a file at a given path into a list of strings, each string corresponding to a line. The line endings are
   * either \n, \r or \r\n.
   *
   * @param path the path.
   * @param charset the character set to use.
   * @return the content of the file at the given path.
   * @throws NullPointerException if the given charset is {@code null}.
   * @throws UncheckedIOException if an I/O exception occurs.
   */
  public static List<String> linesOf(Path path, Charset charset) {
    requireNonNull(charset, "The charset should not be null");
    try {
      return Files.readAllLines(path, charset);
    } catch (IOException e) {
      throw new UncheckedIOException("Unable to read " + path, e);
    }
  }

  /**
   * Loads the text content of a file at a given path into a list of strings, each string corresponding to a line. The line endings are
   * either \n, \r or \r\n.
   *
   * @param path the path.
   * @param charsetName the name of the character set to use.
   * @return the content of the file at the given path.
   * @throws NullPointerException if the given charset is {@code null}.
   * @throws UncheckedIOException if an I/O exception occurs.
   */
  public static List<String> linesOf(Path path, String charsetName) {
    checkArgumentCharsetIsSupported(charsetName);
    return linesOf(path, Charset.forName(charsetName));
  }

  private static void checkArgumentCharsetIsSupported(String charsetName) {
    checkArgument(Charset.isSupported(charsetName), "Charset:<'%s'> is not supported on this system", charsetName);
  }
}
