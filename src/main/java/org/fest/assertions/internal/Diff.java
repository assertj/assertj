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
 * Copyright @2008-2011 the original author or authors.
 */
package org.fest.assertions.internal;

import static java.lang.String.format;
import static java.util.Collections.unmodifiableList;
import static org.fest.util.Closeables.close;
import static org.fest.util.Objects.areEqual;

import java.io.*;
import java.util.*;

/**
 * Compares the contents of two files.
 *
 * @author David DIDIER
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
class Diff {

  private static final String EOF = "EOF";

  List<String> diff(File actual, File expected) throws IOException {
    LineNumberReader reader1 = null;
    LineNumberReader reader2 = null;
    try {
      reader1 = readerFor(actual);
      reader2 = readerFor(expected);
      return unmodifiableList(diff(reader1, reader2));
    } finally {
      close(reader1);
      close(reader2);
    }
  }

  private LineNumberReader readerFor(File file) throws IOException {
    return new LineNumberReader(new BufferedReader(new InputStreamReader(new FileInputStream(file))));
  }

  // reader1 -> actual, reader2 -> expected
  private List<String> diff(LineNumberReader reader1, LineNumberReader reader2) throws IOException {
    List<String> diffs = new ArrayList<String>();
    while (reader2.ready() && reader1.ready()) {
      int lineNumber = reader2.getLineNumber();
      String line1 = reader1.readLine();
      String line2 = reader2.readLine();
      if (areEqual(line1, line2)) continue;
      diffs.add(output(lineNumber, line1, line2));
    }
    if (!reader1.ready() && reader2.ready())
      diffs.add(output(reader2.getLineNumber(), EOF, reader2.readLine()));
    if (reader1.ready() && !reader2.ready())
      diffs.add(output(reader1.getLineNumber(), reader1.readLine(), EOF));
    return diffs;
  }

  private String output(int lineNumber, String actual, String expected) {
    return format("line:<%d>, expected:<%s> but was:<%s>", lineNumber, expected, actual);
  }
}
