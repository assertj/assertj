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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.util.diff.stream;

import static java.nio.ByteBuffer.allocate;
import static org.assertj.core.internal.Diff.MAX_CHAR_BUFFER_SIZE;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

class DefaultEndOfLineFinder implements EndOfLineFinder {
  @Override
  public EndOfLines findEndOfLines(FileChannel fileChannel) throws IOException {
    List<Long> endOfLines = new ArrayList<>();
    BitSet twoCharacters = new BitSet();
    ByteBuffer buffer = allocate(MAX_CHAR_BUFFER_SIZE);
    byte character;
    int bytesRead;
    long offset = 0;
    boolean wasEOL = false;

    while ((bytesRead = fileChannel.read(buffer)) != -1) {
      buffer.position(0);
      buffer.limit(bytesRead);
      while (buffer.hasRemaining()) {
        character = buffer.get();
        wasEOL = false;
        if ((character == '\r') || (character == '\n')) {
          wasEOL = true;
          endOfLines.add(offset + buffer.position() - 1);
          int bitIndex = endOfLines.size() - 1;
          twoCharacters.clear(bitIndex);
          if (character == '\r') {
            if (buffer.hasRemaining()) {
              if (buffer.get() != '\n') {
                buffer.position(buffer.position() - 1);
              } else {
                twoCharacters.set(bitIndex);
              }
            }
          }
        }
      }
      buffer.clear();
      offset += bytesRead;
    }

    if (!wasEOL) {
      endOfLines.add(fileChannel.size());
    }

    return new EndOfLines(endOfLines, twoCharacters);
  }
}
