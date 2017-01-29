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
package org.assertj.core.internal;

import static java.nio.file.Files.newBufferedReader;
import static java.util.Collections.unmodifiableList;
import static org.assertj.core.util.Closeables.closeQuietly;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.VisibleForTesting;
import org.assertj.core.util.diff.Delta;
import org.assertj.core.util.diff.DiffUtils;
import org.assertj.core.util.diff.Patch;


/**
 * Compares the contents of two files, inputStreams or paths.
 * 
 * @author David DIDIER
 * @author Alex Ruiz
 * @author Yvonne Wang
 * @author Matthieu Baechler
 * @author Olivier Michallat
 * @author Joel Costigliola
 */
@VisibleForTesting
public class Diff {

  @VisibleForTesting
  public List<Delta<String>> diff(InputStream actual, InputStream expected) throws IOException {
    return diff(readerFor(actual), readerFor(expected));
  }

  @VisibleForTesting
  public List<Delta<String>> diff(File actual, Charset actualCharset, File expected, Charset expectedCharset) throws IOException {
    return diff(actual.toPath(), actualCharset, expected.toPath(), expectedCharset);
  }

  @VisibleForTesting
  public List<Delta<String>> diff(Path actual, Charset actualCharset, Path expected, Charset expectedCharset) throws IOException {
    return diff(newBufferedReader(actual, actualCharset), newBufferedReader(expected, expectedCharset));
  }

  @VisibleForTesting
  public List<Delta<String>> diff(File actual, String expected, Charset charset) throws IOException {
    return diff(actual.toPath(), expected, charset);
  }

  @VisibleForTesting
  public List<Delta<String>> diff(Path actual, String expected, Charset charset) throws IOException {
    return diff(newBufferedReader(actual, charset), readerFor(expected));
  }

  private BufferedReader readerFor(InputStream stream) {
    return new BufferedReader(new InputStreamReader(stream, Charset.defaultCharset()));
  }

  private BufferedReader readerFor(String string) {
    return new BufferedReader(new StringReader(string));
  }

  private List<Delta<String>> diff(BufferedReader actual, BufferedReader expected) throws IOException {
    try {
      List<String> actualLines = linesFromBufferedReader(actual);
      List<String> expectedLines = linesFromBufferedReader(expected);
      
      Patch<String> patch = DiffUtils.diff(expectedLines, actualLines);
      return unmodifiableList(patch.getDeltas());
    } finally {
      closeQuietly(actual, expected);
    }
  }

  private List<String> linesFromBufferedReader(BufferedReader reader) throws IOException {
    String line;
    List<String> lines = new ArrayList<>();
    while ((line = reader.readLine()) != null) {
      lines.add(line);
    }
    return lines;
  }
}
