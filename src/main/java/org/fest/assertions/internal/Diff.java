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
    LineNumberReader readsActual = null;
    LineNumberReader readsExpected = null;
    try {
      readsActual = readerFor(actual);
      readsExpected = readerFor(expected);
      return unmodifiableList(diff(readsActual, readsExpected));
    } finally {
      close(readsActual);
      close(readsExpected);
    }
  }

  private LineNumberReader readerFor(File file) throws IOException {
    return new LineNumberReader(new BufferedReader(new InputStreamReader(new FileInputStream(file))));
  }

  private List<String> diff(LineNumberReader readsActual, LineNumberReader readsExpected) throws IOException {
    List<String> diffs = new ArrayList<String>();
    while (readsExpected.ready() && readsActual.ready()) {
      int lineNumber = readsExpected.getLineNumber();
      String actualLine = readsActual.readLine();
      String expectedLine = readsExpected.readLine();
      if (areEqual(actualLine, expectedLine)) continue;
      diffs.add(output(lineNumber, actualLine, expectedLine));
      if (!readsActual.ready() && readsExpected.ready())
        diffs.add(output(lineNumber, EOF, expectedLine));
      if (readsActual.ready() && !readsExpected.ready())
        diffs.add(output(lineNumber, actualLine, EOF));
    }
    return diffs;
  }

  private String output(int lineNumber, String actual, String expected) {
    return format("line:<%d>, expected:<%s> but was<%s>", lineNumber, expected, actual);
  }
}
