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

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.BitSet;
import java.util.List;

interface EndOfLineFinder {
  EndOfLines findEndOfLines(FileChannel fileChannel) throws IOException;

  class EndOfLines {
    private final List<Long> endOfLines;
    private final BitSet twoCharacters;

    EndOfLines(List<Long> endOfLines, BitSet twoCharacters) {
      this.endOfLines = endOfLines;
      this.twoCharacters = twoCharacters;
    }

    int size() {
      return endOfLines.size();
    }

    long endOfLine(int index) {
      return endOfLines.get(index);
    }

    boolean twoCharacters(int index) {
      return twoCharacters.get(index);
    }

    @Override
    public String toString() {
      return "EndOfLines{" +
             "endOfLines=" + endOfLines +
             ", twoCharacters=" + twoCharacters +
             '}';
    }
  }
}
