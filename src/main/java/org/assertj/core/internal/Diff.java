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
package org.assertj.core.internal;

import static java.nio.charset.Charset.defaultCharset;
import static java.util.Collections.unmodifiableList;
import static org.assertj.core.util.Closeables.closeQuietly;
import static org.assertj.core.util.diff.stream.StreamViews.createStreamView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;

import org.assertj.core.util.VisibleForTesting;
import org.assertj.core.util.diff.Delta;
import org.assertj.core.util.diff.DiffUtils;
import org.assertj.core.util.diff.Patch;
import org.assertj.core.util.diff.stream.StreamView;


/**
 * Compares the contents of two files, inputStreams or paths.
 * 
 * @author David DIDIER
 * @author Alex Ruiz
 * @author Yvonne Wang
 * @author Matthieu Baechler
 * @author Olivier Michallat
 * @author Joel Costigliola
 * @author Stephan Windmüller
 */
@VisibleForTesting
public class Diff {
  public static final int MAX_CHAR_BUFFER_SIZE = 4096;

  @VisibleForTesting
  public List<Delta<CharSequence>> diff(InputStream actual, InputStream expected) throws IOException {
    return diff(createStreamView(actual, defaultCharset()), createStreamView(expected, defaultCharset()));
  }

  @VisibleForTesting
  public List<Delta<CharSequence>> diff(InputStream actual, String expected) throws IOException {
    return diff(createStreamView(actual, defaultCharset()), createStreamView(expected));
  }

  @VisibleForTesting
  public List<Delta<CharSequence>> diff(File actual, Charset actualCharset, File expected, Charset expectedCharset) throws IOException {
    return diff(actual.toPath(), actualCharset, expected.toPath(), expectedCharset);
  }

  @VisibleForTesting
  public List<Delta<CharSequence>> diff(Path actual, Charset actualCharset, Path expected, Charset expectedCharset) throws IOException {
    return diff(createStreamView(actual, actualCharset), createStreamView(expected, expectedCharset));
  }

  @VisibleForTesting
  public List<Delta<CharSequence>> diff(File actual, String expected, Charset charset) throws IOException {
    return diff(actual.toPath(), expected, charset);
  }

  @VisibleForTesting
  public List<Delta<CharSequence>> diff(Path actual, String expected, Charset charset) throws IOException {
    return diff(createStreamView(actual, charset), createStreamView(expected));
  }

  @VisibleForTesting
  private List<Delta<CharSequence>> diff(StreamView actual, StreamView expected) throws IOException {
    try {
      List<CharSequence> actualLines = actual.lines();
      List<CharSequence> expectedLines = expected.lines();

      Patch<CharSequence> patch = DiffUtils.diff(expectedLines, actualLines);
      return unmodifiableList(patch.getDeltas());
    } finally {
      closeQuietly(actual, expected);
    }
  }
}
