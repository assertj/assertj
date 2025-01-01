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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.error;

import java.io.File;
import java.nio.file.Path;

/**
 * Creates an error message indicating that a {@code Path} should have no extension.
 */
public class ShouldHaveNoExtension extends BasicErrorMessageFactory {

  private static final String PATH_HAS_EXTENSION = "%nExpected actual path:%n  %s%n not to have an extension, but extension was: %s";
  private static final String FILE_HAS_EXTENSION = "%nExpected actual file:%n  %s%n not to have an extension, but extension was: %s";

  public static ShouldHaveNoExtension shouldHaveNoExtension(Path actual, String extension) {
    return new ShouldHaveNoExtension(actual, extension);
  }

  public static ShouldHaveNoExtension shouldHaveNoExtension(File actual, String extension) {
    return new ShouldHaveNoExtension(actual, extension);
  }

  private ShouldHaveNoExtension(Path actual, String extension) {
    super(PATH_HAS_EXTENSION, actual, extension);
  }

  private ShouldHaveNoExtension(File actual, String extension) {
    super(FILE_HAS_EXTENSION, actual, extension);
  }
}
