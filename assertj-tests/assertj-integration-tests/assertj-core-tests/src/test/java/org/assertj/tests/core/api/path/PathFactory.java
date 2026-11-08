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
package org.assertj.tests.core.api.path;

import static java.nio.file.Files.createFile;
import static java.nio.file.Files.getFileAttributeView;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributeView;
import java.util.List;

class PathFactory {

  static List<Path> createUnixPaths(Path root, boolean hidden) throws IOException {
    String prefix = hidden ? "." : "";
    Path existing = createFile(root.resolve(prefix + "existing"));
    Path nonExisting = root.resolve(prefix + "non-existing");
    return List.of(existing, nonExisting);
  }

  static List<Path> createWindowsPaths(Path root, boolean hidden) throws IOException {
    Path existing = createFile(root.resolve("existing"));
    DosFileAttributeView existingFileAttributeView = getFileAttributeView(existing, DosFileAttributeView.class);
    existingFileAttributeView.setHidden(hidden);

    Path nonExisting = root.resolve("non-existing");
    DosFileAttributeView nonExistingFileAttributeView = getFileAttributeView(nonExisting, DosFileAttributeView.class);
    nonExistingFileAttributeView.setHidden(hidden);

    return List.of(existing, nonExisting);
  }

}
