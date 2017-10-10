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

import static org.assertj.core.util.Preconditions.checkArgument;
import static org.assertj.core.util.Preconditions.checkNotNull;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import org.assertj.core.api.exception.RuntimeIOException;

/**
 * Utility methods related to URLs.
 *
 * @author Turbo87
 * @author dorzey
 */
public class URLs {

  private URLs() {
  }

  /**
   * Loads the text content of a URL into a character string.
   *
   * @param url the URL.
   * @param charsetName the name of the character set to use.
   * @return the content of the file.
   * @throws IllegalArgumentException if the given character set is not supported on this platform.
   * @throws RuntimeIOException if an I/O exception occurs.
   */
  public static String contentOf(URL url, String charsetName) {
    checkArgumentCharsetIsSupported(charsetName);
    return contentOf(url, Charset.forName(charsetName));
  }

  /**
   * Loads the text content of a URL into a character string.
   *
   * @param url the URL.
   * @param charset the character set to use.
   * @return the content of the URL.
   * @throws NullPointerException if the given charset is {@code null}.
   * @throws RuntimeIOException if an I/O exception occurs.
   */
  public static String contentOf(URL url, Charset charset) {
    checkNotNull(charset, "The charset should not be null");
    try {
      return loadContents(url.openStream(), charset);
    } catch (IOException e) {
      throw new RuntimeIOException("Unable to read " + url, e);
    }
  }

  /**
   * Loads the text content of a URL into a list of strings, each string corresponding to a line. The line endings are
   * either \n, \r or \r\n.
   *
   * @param url the URL.
   * @param charset the character set to use.
   * @return the content of the URL.
   * @throws NullPointerException if the given charset is {@code null}.
   * @throws RuntimeIOException if an I/O exception occurs.
   */
  public static List<String> linesOf(URL url, Charset charset) {
    checkNotNull(charset, "The charset should not be null");
    try {
      return loadLines(url.openStream(), charset);
    } catch (IOException e) {
      throw new RuntimeIOException("Unable to read " + url, e);
    }
  }

  /**
   * Loads the text content of a URL into a list of strings, each string corresponding to a line. The line endings are
   * either \n, \r or \r\n.
   *
   * @param url the URL.
   * @param charsetName the name of the character set to use.
   * @return the content of the URL.
   * @throws NullPointerException if the given charset is {@code null}.
   * @throws RuntimeIOException if an I/O exception occurs.
   */
  public static List<String> linesOf(URL url, String charsetName) {
    checkArgumentCharsetIsSupported(charsetName);
    return linesOf(url, Charset.forName(charsetName));
  }

  private static String loadContents(InputStream stream, Charset charset) throws IOException {
    try (StringWriter writer = new StringWriter();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset))) {
      int c;
      while ((c = reader.read()) != -1) {
        writer.write(c);
      }
      return writer.toString();
    }
  }

  private static List<String> loadLines(InputStream stream, Charset charset) throws IOException {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset))) {
      List<String> strings = Lists.newArrayList();

      String line = reader.readLine();
      while (line != null) {
        strings.add(line);
        line = reader.readLine();
      }

      return strings;

    }
  }

  private static void checkArgumentCharsetIsSupported(String charsetName) {
    checkArgument(Charset.isSupported(charsetName), "Charset:<'%s'> is not supported on this system", charsetName);
  }

}
