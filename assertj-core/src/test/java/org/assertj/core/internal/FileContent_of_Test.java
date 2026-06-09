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

import static org.assertj.core.api.BDDAssertions.then;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileContent_of_Test {

  @TempDir
  Path tempDir;

  @Test
  void should_create_file_content_from_file() throws IOException {
    // GIVEN
    byte[] bytes = "hello".getBytes();
    File file = Files.write(tempDir.resolve("actual.txt"), bytes).toFile();
    // WHEN
    FileContent fileContent = FileContent.of(file);
    // THEN
    then(fileContent.path()).isEqualTo(file.getAbsolutePath());
    then(fileContent.contents()).isEqualTo(bytes);
  }

  @Test
  void should_create_file_content_from_path() throws IOException {
    // GIVEN
    byte[] bytes = "hello".getBytes();
    Path path = Files.write(tempDir.resolve("actual.txt"), bytes);
    // WHEN
    FileContent fileContent = FileContent.of(path);
    // THEN
    then(fileContent.path()).isEqualTo(path.toAbsolutePath().toString());
    then(fileContent.contents()).isEqualTo(bytes);
  }
}
