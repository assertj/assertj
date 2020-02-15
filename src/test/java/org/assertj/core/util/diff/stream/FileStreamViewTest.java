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

import static java.nio.CharBuffer.wrap;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.delete;
import static java.nio.file.Files.write;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.util.Files.newTemporaryFile;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.BitSet;
import java.util.List;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.assertj.core.util.diff.stream.EndOfLineFinder.EndOfLines;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

@ExtendWith({ MockitoExtension.class, SoftAssertionsExtension.class })
class FileStreamViewTest {
  @Mock
  private EndOfLineFinder endOfLineFinder;
  @Mock
  private CharSequenceSupplier charSequenceSupplier;

  private Path file;
  private FileStreamViewSource source;
  private FileStreamView view;

  @AfterEach
  void tearDown() throws IOException {
    source.close();
    view.close();
    delete(file);
  }

  @Test
  void lines(SoftAssertions softly) throws IOException {
    byte[] bytes = { 'a', '\r', '\n', 'b', 'c', '\r', '\n', 'z' };
    file = newTemporaryFile().toPath();
    write(file, bytes);
    source = createPathSourceSettingChannelPositionAtOne();
    final long[] channelPositionBeforeCallToFindEndOfLines = new long[] { -1 };
    when(endOfLineFinder.findEndOfLines(notNull())).then(mockFindEndOfLines(bytes, channelPositionBeforeCallToFindEndOfLines));
    when(charSequenceSupplier.get(anyLong(), anyLong())).then(mockApply(bytes));
    view = new FileStreamView(source, () -> endOfLineFinder, charSequenceSupplier);

    List<CharSequence> lines = view.lines();

    softly.assertThat(lines).extracting(CharSequence::toString).containsExactly("a", "bc", "z");
    softly.assertThat(channelPositionBeforeCallToFindEndOfLines).containsExactly(0);
  }

  private PathSource createPathSourceSettingChannelPositionAtOne() throws FileNotFoundException {
    return new PathSource(this.file) {
      @Override
      public FileChannel getChannel() {
        FileChannel channel = super.getChannel();
        try {
          channel.position(1);
        } catch (IOException e) {
          fail(e.getMessage(), e);
        }
        return channel;
      }
    };
  }

  private Answer<CharBuffer> mockApply(byte[] bytes) {
    return invocationOnMock -> {
      long begin = invocationOnMock.getArgument(0);
      long end = invocationOnMock.getArgument(1);
      return wrap(new String(bytes, UTF_8).substring((int) begin, (int) end));
    };
  }

  private Answer<EndOfLines> mockFindEndOfLines(byte[] bytes, long[] channelPositionBeforeCallToFindEndOfLines) {
    return invocationOnMock -> {
      FileChannel fileChannel = invocationOnMock.getArgument(0);
      channelPositionBeforeCallToFindEndOfLines[0] = fileChannel.position();
      BitSet twoCharacters = new BitSet();
      twoCharacters.set(0, 2);
      return new EndOfLines(asList(1L, 5L, (long) bytes.length), twoCharacters);
    };
  }
}
