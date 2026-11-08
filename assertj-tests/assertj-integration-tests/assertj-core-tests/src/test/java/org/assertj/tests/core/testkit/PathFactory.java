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
package org.assertj.tests.core.testkit;

import static java.util.UUID.randomUUID;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributeView;

/// [Path] factory methods for filesystem-based tests.
public class PathFactory {

  /// Creates a unique file under the given parent.
  ///
  /// @param parent the directory in which to create the file
  /// @return the created file path
  /// @throws IOException if the file cannot be created
  public static Path createFile(Path parent) throws IOException {
    return Files.createFile(createPath(parent));
  }

  /// Creates a unique path under the given parent, without creating it on the file system.
  ///
  /// @param parent the directory containing the path
  /// @return the generated path
  public static Path createPath(Path parent) {
    return parent.resolve(randomUUID().toString());
  }

  /// Provides filesystem [Path] factories specific to Unix.
  public static class Unix {

    /// Creates a unique hidden file under the given parent.
    ///
    /// @param parent the directory in which to create the file
    /// @return the created hidden file path
    /// @throws IOException if the file cannot be created
    /// @see Files#isHidden(Path)
    public static Path createHiddenFile(Path parent) throws IOException {
      return Files.createFile(createHiddenPath(parent));
    }

    /// Creates a unique hidden path under the given parent, without creating it on the file system.
    ///
    /// @param parent the directory containing the path
    /// @return the generated hidden path
    /// @see Files#isHidden(Path)
    public static Path createHiddenPath(Path parent) {
      return parent.resolve("." + randomUUID());
    }

  }

  /// Provides filesystem [Path] factories specific to Windows.
  public static class Windows {

    /// Creates a unique file under the given parent and marks it as hidden.
    ///
    /// @param parent the directory in which to create the file
    /// @return the created hidden file path
    /// @throws IOException if the file cannot be created or its hidden attribute cannot be set
    /// @see Files#isHidden(Path)
    public static Path createHiddenFile(Path parent) throws IOException {
      Path file = createFile(parent);
      DosFileAttributeView existingFileAttributeView = Files.getFileAttributeView(file, DosFileAttributeView.class);
      existingFileAttributeView.setHidden(true);
      return file;
    }

  }

}
