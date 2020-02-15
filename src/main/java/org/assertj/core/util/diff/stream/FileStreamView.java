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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.assertj.core.util.VisibleForTesting;
import org.assertj.core.util.diff.stream.EndOfLineFinder.EndOfLines;

class FileStreamView implements StreamView {
  private final Supplier<EndOfLineFinder> endOfLineFinderSupplier;
  private final CharSequenceSupplier charSequenceSupplier;
  private final FileStreamViewSource source;

  FileStreamView(FileStreamViewSource source, Charset charset, Supplier<EndOfLineFinder> endOfLineFinderSupplier) {
    this(source, endOfLineFinderSupplier, new DefaultCharSequenceSupplier(charset, source.getChannel()));
  }

  @VisibleForTesting
  FileStreamView(FileStreamViewSource source, Supplier<EndOfLineFinder> endOfLineFinderSupplier,
                 CharSequenceSupplier charSequenceSupplier) {
    this.source = source;
    this.endOfLineFinderSupplier = endOfLineFinderSupplier;
    this.charSequenceSupplier = charSequenceSupplier;
  }

  @Override
  public List<CharSequence> lines() throws IOException {
    FileChannel channel = source.getChannel();
    channel.position(0);
    EndOfLines endOfLines = endOfLineFinderSupplier.get().findEndOfLines(channel);
    List<CharSequence> lines = new ArrayList<>();
    long begin = 0;
    long end;
    for (int endIndex = 0; endIndex < endOfLines.size(); endIndex++) {
      end = endOfLines.endOfLine(endIndex);
      lines.add(charSequenceSupplier.get(begin, end));
      begin = end + 1;
      if (endOfLines.twoCharacters(endIndex)) {
        begin++;
      }
    }
    return lines;
  }

  @Override
  public void close() throws IOException {
    source.close();
  }
}
