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
package org.assertj.core.internal;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;

/**
 * Wrapper for <code>{@link java.nio.file.Files}</code> to test methods throwing {@link IOException}.
 */
public class NioFilesWrapper {

  private static final NioFilesWrapper INSTANCE = new NioFilesWrapper();

  static NioFilesWrapper instance() {
    return INSTANCE;
  }

  private NioFilesWrapper() {}

  /**
   * Opens an input stream for a path.
   *
   * @param path the path to open
   * @param options the open options
   * @return the input stream
   * @throws IOException if the stream cannot be opened
   */
  public InputStream newInputStream(Path path, OpenOption... options) throws IOException {
    return Files.newInputStream(path, options);
  }

  /**
   * Opens a filtered directory stream.
   *
   * @param dir the directory path
   * @param filter the entry filter
   * @return the directory stream
   * @throws IOException if the directory cannot be opened
   */
  public DirectoryStream<Path> newDirectoryStream(Path dir, Filter<? super Path> filter) throws IOException {
    return Files.newDirectoryStream(dir, filter);
  }

  /**
   * Returns the size of a file.
   *
   * @param path the file path
   * @return the file size
   * @throws IOException if the size cannot be read
   */
  public long size(Path path) throws IOException {
    return Files.size(path);
  }

}
