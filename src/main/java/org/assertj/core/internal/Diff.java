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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.internal;

import static java.util.Collections.unmodifiableList;
import static org.assertj.core.util.Closeables.closeQuietly;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
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
    BufferedReader reader1 = null;
    BufferedReader reader2 = null;
    try {
      reader1 = readerFor(actual);
      reader2 = readerFor(expected);
      return unmodifiableList(diff(reader1, reader2));
    } finally {
      closeQuietly(reader1);
      closeQuietly(reader2);
    }
  }

  @VisibleForTesting
  public List<Delta<String>> diff(File actual, File expected) throws IOException {
    return diff(actual.toPath(), expected.toPath());
  }

  @VisibleForTesting
  public List<Delta<String>> diff(Path actual, Path expected) throws IOException {
    BufferedReader reader1 = null;
    BufferedReader reader2 = null;
    try {
      reader1 = Files.newBufferedReader(actual, Charset.defaultCharset());
      reader2 = Files.newBufferedReader(expected, Charset.defaultCharset());
      return unmodifiableList(diff(reader1, reader2));
    } finally {
      closeQuietly(reader1);
      closeQuietly(reader2);
    }
  }

  @VisibleForTesting
  public List<Delta<String>> diff(File actual, String expected, Charset charset) throws IOException {
    return diff(actual.toPath(), expected, charset);
  }

  @VisibleForTesting
  public List<Delta<String>> diff(Path actual, String expected, Charset charset) throws IOException {
    BufferedReader reader1 = null;
    try {
      reader1 = Files.newBufferedReader(actual, charset);
      BufferedReader reader2 = readerFor(expected);
      return unmodifiableList(diff(reader1, reader2));
    } finally {
      closeQuietly(reader1);
    }
  }

  private BufferedReader readerFor(InputStream stream) {
    return new BufferedReader(new InputStreamReader(stream));
  }

  private BufferedReader readerFor(String string) {
    return new BufferedReader(new StringReader(string));
  }

  private List<Delta<String>> diff(BufferedReader actual, BufferedReader expected) throws IOException {
    List<String> actualLines = linesFromBufferedReader(actual);
    List<String> expectedLines = linesFromBufferedReader(expected);

    Patch<String> patch = DiffUtils.diff(expectedLines, actualLines);
    return patch.getDeltas();
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
