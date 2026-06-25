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

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Carries the absolute path and full byte contents of a file so that, on assertion failure,
 * {@link org.assertj.core.error.AssertionErrorCreator} can reflectively build an
 * {@code org.opentest4j.FileInfo} for IDE file-diff rendering. Holds no opentest4j dependency.
 */
public final class FileContent {

  private final String path;
  private final byte[] contents;

  public FileContent(String path, byte[] contents) {
    this.path = path;
    this.contents = contents;
  }

  public String path() {
    return path;
  }

  public byte[] contents() {
    return contents;
  }

  public static FileContent of(File file) throws IOException {
    return new FileContent(file.getAbsolutePath(), java.nio.file.Files.readAllBytes(file.toPath()));
  }

  public static FileContent of(Path path) throws IOException {
    return new FileContent(path.toAbsolutePath().toString(), java.nio.file.Files.readAllBytes(path));
  }

  @Override
  public String toString() {
    return path;
  }
}
