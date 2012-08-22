/*
 * Created on Feb 9, 2008
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
 * Copyright @2008-2012 the original author or authors.
 */
package org.fest.assertions.internal;

import static java.lang.String.format;
import static java.util.Collections.unmodifiableList;
import static org.fest.util.Closeables.closeQuietly;
import static org.fest.util.Objects.areEqual;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.fest.util.VisibleForTesting;

/**
 * Compares the contents of two files or two streams.
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
  private static final String EOF = "EOF";

  @VisibleForTesting
  public List<String> diff(InputStream actual, InputStream expected) throws IOException {
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
  public List<String> diff(File actual, File expected) throws IOException {
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
  public List<String> diff(File actual, String expected, Charset charset) throws IOException {
    BufferedReader reader1 = null;
    try {
      reader1 = readerFor(actual, charset);
      BufferedReader reader2 = readerFor(expected);
      return unmodifiableList(diff(reader1, reader2));
    } finally {
      closeQuietly(reader1);
    }
  }

  private BufferedReader readerFor(InputStream stream) {
    return new BufferedReader(new InputStreamReader(stream));
  }

  private BufferedReader readerFor(InputStream stream, Charset charset) {
    return new BufferedReader(new InputStreamReader(stream, charset));
  }

  private BufferedReader readerFor(File file) throws IOException {
    return readerFor(new FileInputStream(file));
  }

  private BufferedReader readerFor(File file, Charset charset) throws IOException {
    return readerFor(new FileInputStream(file), charset);
  }

  private BufferedReader readerFor(String string) {
    return new BufferedReader(new StringReader(string));
  }

  private List<String> diff(BufferedReader actual, BufferedReader expected) throws IOException {
    List<String> diffs = new ArrayList<String>();
    int lineNumber = 0;
    while (true) {
      String actualLine = actual.readLine();
      String expectedLine = expected.readLine();
      if (actualLine == null || expectedLine == null) {
        if (expectedLine != null) {
          diffs.add(output(lineNumber, EOF, expectedLine));
        }
        if (actualLine != null) {
          diffs.add(output(lineNumber, actualLine, EOF));
        }
        return diffs;
      } else if (!areEqual(actualLine, expectedLine)) {
        diffs.add(output(lineNumber, actualLine, expectedLine));
      }
      lineNumber += 1;
    }
  }

  private String output(int lineNumber, String actual, String expected) {
    return format("line:<%d>, expected:<%s> but was:<%s>", lineNumber, expected, actual);
  }
}
