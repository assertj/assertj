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

import static java.nio.file.Files.copy;
import static java.nio.file.Files.delete;
import static java.nio.file.Files.exists;
import static org.assertj.core.util.Files.newTemporaryFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.Objects;

class FileInputStreamSource implements FileStreamViewSource {
  private final FileInputStream input;
  private final Path path;

  FileInputStreamSource(FileInputStream input) {
    path = null;
    this.input = input;
  }

  FileInputStreamSource(InputStream input) throws IOException {
    path = copyIntoTemporaryFile(input);
    this.input = new FileInputStream(path.toFile());
  }

  @Override
  public File getFile() {
    return path.toFile();
  }

  @Override
  public FileChannel getChannel() {
    return input.getChannel();
  }

  @Override
  public void close() throws IOException {
    input.close();
    if (path != null) {
      delete(path);
    }
  }

  private static Path copyIntoTemporaryFile(InputStream input) throws IOException {
    Path temporaryFile = newTemporaryFile().toPath();
    Path parent = temporaryFile.getParent();
    String fileName = temporaryFile.getFileName().toString();
    if (exists(temporaryFile)) {
      parent.resolve(fileName + Objects.hashCode(input));
    }
    if (exists(temporaryFile)) {
      int count = 0;
      do {
        temporaryFile = parent.resolve(fileName + count);
      } while (exists(temporaryFile));
    }
    copy(input, temporaryFile);
    return temporaryFile;
  }
}
