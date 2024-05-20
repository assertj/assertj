/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

/**
 * Utility methods for creating a temporary file for the tests.
 * @author Nikolaos Georgiou
 */
public final class TempFileUtil {
  private TempFileUtil() {}

  /**
   * Creates a temporary file with the given content. The file will be deleted automatically when the tests finish.
   * @param content The content of the file.
   * @return The newly created file instance.
   * @throws IOException If an IO error occurs.
   */
  public static File createTempFileWithContent(String content) throws IOException {
    Path tempFile = java.nio.file.Files.createTempFile("test", "test");
    tempFile.toFile().deleteOnExit();
    Files.write(tempFile, content.getBytes());
    return tempFile.toFile();
  }

  /**
   * Creates a temporary file with the given content. The file will be deleted automatically when the tests finish.
   * @param content The content of the file.
   * @param charset The charset to use.
   * @return The newly created path instance.
   * @throws IOException If an IO error occurs.
   */
  public static Path createTempPathWithContent(String content, Charset charset) throws IOException {
    Path tempFile = java.nio.file.Files.createTempFile("test", "test");
    tempFile.toFile().deleteOnExit();
    Files.write(tempFile, Collections.singletonList(content), charset);
    return tempFile;
  }

  /**
   * Creates a temporary file with the given content. The file will be deleted automatically when the tests finish.
   * @param content The content of the file.
   * @param charset The charset to use.
   * @return The newly created file instance.
   * @throws IOException If an IO error occurs.
   */
  public static File createTempFileWithContent(String content, Charset charset) throws IOException {
    return createTempPathWithContent(content, charset).toFile();
  }
}
