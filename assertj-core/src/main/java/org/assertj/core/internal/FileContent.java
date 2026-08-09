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

import static java.nio.file.Files.readAllBytes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.assertj.core.error.AssertionErrorCreator;

/**
 * Carries the absolute path and full byte contents of a file so that, on assertion failure,
 * {@link AssertionErrorCreator} can reflectively build an
 * {@code org.opentest4j.FileInfo} for IDE file-diff rendering. Holds no opentest4j dependency.
 */
public record FileContent(String path, byte[] contents) {

  public FileContent(String path, byte[] contents) {
    this.path = path;
    this.contents = contents == null ? null : contents.clone();
  }

  public static FileContent of(File file) throws IOException {
    return FileContent.of(file.toPath());
  }

  public static FileContent of(Path path) throws IOException {
    return new FileContent(path.toAbsolutePath().toString(), readAllBytes(path));
  }

  @Override
  public String toString() {
    return path;
  }
}
