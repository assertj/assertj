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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.assertj.core.util.VisibleForTesting;


/**
 * Compares the binary content of two inputStreams/paths.
 * 
 * @author Olivier Michallat
 */
@VisibleForTesting
public class BinaryDiff {

  @VisibleForTesting
  public BinaryDiffResult diff(File actual, byte[] expected) throws IOException {
    return diff(actual.toPath(), expected);
  }

  @VisibleForTesting
  public BinaryDiffResult diff(Path actual, byte[] expected) throws IOException {
    InputStream expectedStream = new ByteArrayInputStream(expected);
    InputStream actualStream = null;
    boolean threw = true;
    try {
      actualStream = Files.newInputStream(actual);
      BinaryDiffResult result = diff(actualStream, expectedStream);
      threw = false;
      return result;
    } finally {
      try {
        if (actualStream != null) actualStream.close();
      } catch (IOException e) {
        // Only rethrow if it doesn't shadow an exception thrown from the inner try block
        if (!threw) throw e;
      }
    }
  }

  @VisibleForTesting
  public BinaryDiffResult diff(InputStream actualStream, InputStream expectedStream) throws IOException {
    int index = 0;
    while (true) {
      int actual = actualStream.read();
      int expected = expectedStream.read();
      if (actual == -1 && expected == -1) return BinaryDiffResult.noDiff(); // reached end of both streams
      if (actual != expected) return new BinaryDiffResult(index, expected, actual);
      index += 1;
    }
  }
}
