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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.error;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import org.assertj.core.util.VisibleForTesting;

/**
 * Creates an error message indicating that an assertion that verifies the {@link FileSystem} for
 * a given path does not match a given file system.
 *
 * @author Ashley Scopes
 */
public class ShouldHaveSameFileSystem extends BasicErrorMessageFactory {
  @VisibleForTesting
  public static final String PATH_SHOULD_HAVE_SAME_FILE_SYSTEM =
    "%nExpecting path:%n  %s%nto have file system:%n  %s";
  public static final String PATH_SHOULD_HAVE_SAME_FILE_SYSTEM_AS_PATH =
    "%nExpecting path:%n  %s%nto have file system as path:%n  %s";

  public static ErrorMessageFactory shouldHaveSameFileSystem(final Path actual, final FileSystem fileSystem) {
    return new ShouldHaveSameFileSystem(actual, fileSystem);
  }

  public static ErrorMessageFactory shouldHaveSameFileSystemAsPath(final Path actual, final Path expected) {
    return new ShouldHaveSameFileSystem(actual, expected);
  }

  private ShouldHaveSameFileSystem(final Path actual, final FileSystem expected) {
    super(PATH_SHOULD_HAVE_SAME_FILE_SYSTEM, actual, expected);
  }

  private ShouldHaveSameFileSystem(final Path actual, final Path expected) {
    super(PATH_SHOULD_HAVE_SAME_FILE_SYSTEM_AS_PATH, actual, expected);
  }
}
