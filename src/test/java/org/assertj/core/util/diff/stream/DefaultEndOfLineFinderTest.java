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

import static java.nio.charset.Charset.defaultCharset;
import static java.nio.charset.Charset.forName;
import static java.nio.file.Files.delete;
import static java.nio.file.Files.write;
import static java.util.stream.IntStream.range;
import static org.assertj.core.internal.Diff.MAX_CHAR_BUFFER_SIZE;
import static org.assertj.core.util.Files.newTemporaryFile;
import static org.assertj.core.util.TempFileUtil.createTempPathWithContent;
import static org.assertj.core.util.diff.stream.DefaultEndOfLineFinderTest.ByteArrayBuilder.bytes;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.assertj.core.util.diff.stream.EndOfLineFinder.EndOfLines;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SoftAssertionsExtension.class)
class DefaultEndOfLineFinderTest {
  private final DefaultEndOfLineFinder defaultEndOfLineFinder = new DefaultEndOfLineFinder();

  private final SoftAssertions softly = new SoftAssertions();

  @Test
  public void findEndOfLines_smallFile_without_EOL() throws IOException {
    byte[] bytes = { 'a', 'b', 'c', 'z' };
    findEndOfLines(bytes, false, bytes.length);
  }

  @Test
  public void findEndOfLines_bigFile_without_EOL() throws IOException {
    byte[] bytes = bytes('a', 'b', 'c', 'z').repeat(' ', MAX_CHAR_BUFFER_SIZE).append('H').bytes();
    findEndOfLines(bytes, false, bytes.length);
  }

  @Test
  public void findEndOfLines_smallFile_with_lineFeed_at_begin() throws IOException {
    byte[] bytes = { '\n', 'a', '\n', 'b', 'c', '\n', 'z' };
    findEndOfLines(bytes, false, 0, 2, 5, bytes.length);
  }

  @Test
  public void findEndOfLines_smallFile_with_lineFeed_at_end() throws IOException {
    byte[] bytes = { 'a', '\n', 'b', 'c', '\n', 'z', '\n' };
    findEndOfLines(bytes, false, 1, 4, 6);
  }

  @Test
  public void findEndOfLines_smallFile_with_lineFeed() throws IOException {
    byte[] bytes = { 'a', '\n', 'b', 'c', '\n', 'z' };
    findEndOfLines(bytes, false, 1, 4, bytes.length);
  }

  @Test
  public void findEndOfLines_bigFile_with_lineFeed() throws IOException {
    byte[] bytes = bytes('a', '\n', 'b', 'c', '\n', 'z', '\n').repeat(' ', MAX_CHAR_BUFFER_SIZE).append('\n', 'H').bytes();
    findEndOfLines(bytes, false, 1, 4, 6, 6 + MAX_CHAR_BUFFER_SIZE + 1, bytes.length);
  }

  @Test
  public void findEndOfLines_smallFile_with_carriageReturn_and_lineFeed_at_begin() throws IOException {
    byte[] bytes = { '\r', '\n', 'a', '\r', '\n', 'b', 'c', '\r', '\n', 'z' };
    findEndOfLines(bytes, true, 0, 3, 7, bytes.length);
  }

  @Test
  public void findEndOfLines_smallFile_with_carriageReturn_and_lineFeed_at_end() throws IOException {
    byte[] bytes = { 'a', '\r', '\n', 'b', 'c', '\r', '\n', 'z', '\r', '\n' };
    findEndOfLines(bytes, true, 1, 5, 8);
  }

  @Test
  public void findEndOfLines_smallFile_with_carriageReturn_and_lineFeed() throws IOException {
    byte[] bytes = { 'a', '\r', '\n', 'b', 'c', '\r', '\n', 'z' };
    findEndOfLines(bytes, true, 1, 5, bytes.length);
  }

  @Test
  public void findEndOfLines_bigFile_with_carriageReturn_and_lineFeed() throws IOException {
    byte[] bytes = bytes('a', '\r', '\n', 'b', 'c', '\r', '\n', 'z', '\r', '\n').repeat(' ', MAX_CHAR_BUFFER_SIZE)
                                                                                .append('\r', '\n', 'H').bytes();
    findEndOfLines(bytes, true, 1, 5, 8, 8 + 1 + MAX_CHAR_BUFFER_SIZE + 1, bytes.length);
  }

  @Test
  public void findEndOfLines_smallFile_with_carriageReturn_at_begin() throws IOException {
    byte[] bytes = { '\r', 'a', '\r', 'b', 'c', '\r', 'z' };
    findEndOfLines(bytes, false, 0, 2, 5, bytes.length);
  }

  @Test
  public void findEndOfLines_smallFile_with_carriageReturn_at_end() throws IOException {
    byte[] bytes = { 'a', '\r', 'b', 'c', '\r', 'z', '\r' };
    findEndOfLines(bytes, false, 1, 4, 6);
  }

  @Test
  public void findEndOfLines_smallFile_with_carriageReturn() throws IOException {
    byte[] bytes = { 'a', '\r', 'b', 'c', '\r', 'z' };
    findEndOfLines(bytes, false, 1, 4, bytes.length);
  }

  @Test
  public void findEndOfLines_bigFile_with_carriageReturn() throws IOException {
    byte[] bytes = bytes('a', '\r', 'b', 'c', '\r', 'z', '\r').repeat(' ', MAX_CHAR_BUFFER_SIZE).append('\r', 'H').bytes();
    findEndOfLines(bytes, false, 1, 4, 6, 6 + MAX_CHAR_BUFFER_SIZE + 1, bytes.length);
  }

  @Test
  public void findEndOfLines_turkishCharset() throws IOException {
    Path path = createTempPathWithContent("Gerçek", forName("windows-1254")); // TODO use other PR for turkish charset
    byte[] bytes = Files.readAllBytes(path);
    findEndOfLines(bytes, false, 6);
  }

  @Test
  public void findEndOfLines_defaultCharset() throws IOException {
    Path path = createTempPathWithContent("Gerçek", defaultCharset());
    byte[] bytes = Files.readAllBytes(path);
    findEndOfLines(bytes, false, 6);
  }

  private void findEndOfLines(byte[] bytes, boolean twoCharacters, Integer... endOfLines) throws IOException {
    Path temporaryFile = newTemporaryFile().toPath();
    try {
      write(temporaryFile, bytes);
      try (FileInputStream fileInputStream = new FileInputStream(temporaryFile.toFile());
          FileChannel fileChannel = fileInputStream.getChannel()) {
        EndOfLines actualEndOfLines = defaultEndOfLineFinder.findEndOfLines(fileChannel);
        softly.assertThat(actualEndOfLines).extracting(EndOfLines::size).isEqualTo(endOfLines.length);
        if (softly.wasSuccess()) {
          range(0, actualEndOfLines.size()).forEach(index -> {
            softly.assertThat(actualEndOfLines.endOfLine(index)).as("endOfLine(%d)", index).isEqualTo(endOfLines);
            softly.assertThat(actualEndOfLines.twoCharacters(index)).as("twoCharacters(%d)", index).isEqualTo(twoCharacters);
          });
        }
      }
    } finally {
      delete(temporaryFile);
    }
  }

  static class ByteArrayBuilder {
    private final List<Byte> bytes = new ArrayList<>();

    static ByteArrayBuilder bytes(char... chars) {
      return new ByteArrayBuilder(chars);
    }

    private ByteArrayBuilder(char... chars) {
      append(chars);
    }

    private ByteArrayBuilder append(char... chars) {
      for (char c : chars) {
        bytes.add((byte) c);
      }
      return this;
    }

    private ByteArrayBuilder repeat(char c, int count) {
      byte b = (byte) c;
      for (int i = 0; i < count; i++) {
        bytes.add(b);
      }
      return this;
    }

    private byte[] bytes() {
      byte[] bytes = new byte[this.bytes.size()];
      int i = 0;
      for (byte b : this.bytes) {
        bytes[i++] = b;
      }
      return bytes;
    }
  }
}
