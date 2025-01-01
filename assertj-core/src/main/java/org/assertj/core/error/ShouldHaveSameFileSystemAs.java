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

import java.nio.file.FileSystem;
import java.nio.file.Path;

/**
 * Creates an error message indicating that an assertion that verifies the {@link FileSystem} for
 * a given path does not match a given path's file system.
 *
 * @author Ashley Scopes
 */
public class ShouldHaveSameFileSystemAs extends BasicErrorMessageFactory {
  private static final String PATH_SHOULD_HAVE_SAME_FILE_SYSTEM_AS_PATH = "%nExpecting path:%n  %s%nto have the same file system as path:%n  %s";

  public static ErrorMessageFactory shouldHaveSameFileSystemAs(final Path actual, final Path expected) {
    return new ShouldHaveSameFileSystemAs(actual, expected);
  }

  private ShouldHaveSameFileSystemAs(final Path actual, final Path expected) {
    super(PATH_SHOULD_HAVE_SAME_FILE_SYSTEM_AS_PATH, actual, expected);
  }
}
