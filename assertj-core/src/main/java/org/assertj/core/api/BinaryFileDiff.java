/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api;

import static org.assertj.core.error.ShouldHaveBinaryContent.shouldHaveBinaryContent;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;

final class BinaryFileDiff {

  private static final int BUFFER_SIZE = 8192;

  private BinaryFileDiff() {}

  static ErrorMessageFactory compare(Path actual, Path expected, Object actualForMessage) throws IOException {
    try (InputStream expectedStream = Files.newInputStream(expected);
        InputStream actualStream = Files.newInputStream(actual)) {
      BasicFileAttributes expectedAttributes = Files.readAttributes(expected, BasicFileAttributes.class);
      BasicFileAttributes actualAttributes = Files.readAttributes(actual, BasicFileAttributes.class);
      long actualSize = actualAttributes.size();
      long expectedSize = expectedAttributes.size();
      if (actualSize != expectedSize && actualAttributes.isRegularFile() && expectedAttributes.isRegularFile()
          && hasStorageCapacity(actual) && hasStorageCapacity(expected)) {
        return new BasicErrorMessageFactory("%nExpecting%n  %s%nto have the same binary content but sizes differ:%n"
                                            + "  expected: " + expectedSize + " bytes%n"
                                            + "  actual: " + actualSize + " bytes",
                                            actualForMessage);
      }

      byte[] expectedBuffer = new byte[BUFFER_SIZE];
      byte[] actualBuffer = new byte[BUFFER_SIZE];
      long offset = 0;
      while (true) {
        int expectedLength = expectedStream.readNBytes(expectedBuffer, 0, BUFFER_SIZE);
        int actualLength = actualStream.readNBytes(actualBuffer, 0, BUFFER_SIZE);
        int mismatch = Arrays.mismatch(expectedBuffer, 0, expectedLength, actualBuffer, 0, actualLength);
        if (mismatch >= 0) {
          int expectedByte = mismatch == expectedLength ? -1 : Byte.toUnsignedInt(expectedBuffer[mismatch]);
          int actualByte = mismatch == actualLength ? -1 : Byte.toUnsignedInt(actualBuffer[mismatch]);
          return shouldHaveBinaryContent(actualForMessage, offset + mismatch, expectedByte, actualByte);
        }
        if (expectedLength < BUFFER_SIZE) return null;
        offset += expectedLength;
      }
    }
  }

  private static boolean hasStorageCapacity(Path path) {
    try {
      // Virtual file systems such as procfs can report nominal file sizes and no storage capacity.
      return Files.getFileStore(path).getTotalSpace() > 0;
    } catch (IOException | UnsupportedOperationException | SecurityException e) {
      // File contents can still be compared when storage capacity is unavailable.
      return false;
    }
  }
}
