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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Path;

public class StreamViews {
  private StreamViews() {
    // utility class
  }

  public static StreamView createStreamView(Path input, Charset charset) throws FileNotFoundException {
    return createFileStreamView(new PathSource(input), charset);
  }

  public static StreamView createStreamView(InputStream input, Charset charset) throws IOException {
    FileInputStreamSource source = (input instanceof FileInputStream) ? new FileInputStreamSource((FileInputStream) input)
        : new FileInputStreamSource(input);
    return createFileStreamView(source, charset);
  }

  public static StreamView createStreamView(String string) {
    return new StringStreamView(string);
  }

  private static FileStreamView createFileStreamView(FileStreamViewSource source, Charset charset) {
    return new FileStreamView(source, charset, DefaultEndOfLineFinder::new);
  }
}
