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
package org.assertj.core.error;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

/** Creates errors for directories expected to be empty. */
public class ShouldBeEmptyDirectory extends BasicErrorMessageFactory {

  private static final String SHOULD_BE_EMPTY_DIRECTORY = "%nExpecting actual:%n  %s%nto be an empty directory but it contained:%n  %s";

  /**
   * Creates an error for a non-empty path directory.
   *
   * @param actual the actual path
   * @param directoryContent the directory content
   * @return the error message factory
   */
  public static ErrorMessageFactory shouldBeEmptyDirectory(final Path actual, List<Path> directoryContent) {
    return new ShouldBeEmptyDirectory(actual, directoryContent);
  }

  /**
   * Creates an error for a non-empty file directory.
   *
   * @param actual the actual file
   * @param directoryContent the directory content
   * @return the error message factory
   */
  public static ErrorMessageFactory shouldBeEmptyDirectory(final File actual, List<File> directoryContent) {
    return new ShouldBeEmptyDirectory(actual, directoryContent);
  }

  private ShouldBeEmptyDirectory(final Path actual, List<Path> directoryContent) {
    super(SHOULD_BE_EMPTY_DIRECTORY, actual, directoryContent);
  }

  private ShouldBeEmptyDirectory(final File actual, List<File> directoryContent) {
    super(SHOULD_BE_EMPTY_DIRECTORY, actual, directoryContent);
  }
}
