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
package org.assertj.core.api.path;

import static java.nio.file.Files.createDirectory;
import static java.nio.file.Files.createFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.nio.file.Path;

import org.assertj.core.api.PathAssert;
import org.assertj.core.api.PathAssertBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for <code>{@link PathAssert#isDirectoryContaining(String)}</code>
 *
 * @author Valeriy Vyrva
 */
class PathAssert_isDirectoryContaining_SyntaxAndPattern_Test extends PathAssertBaseTest {

  @TempDir
  private Path tempDir;
  private final String syntaxAndPattern = "glob:*.java";

  @Override
  protected PathAssert invoke_api_method() {
    return assertions.isDirectoryContaining(syntaxAndPattern);
  }

  @Override
  protected void verify_internal_effects() {
    verify(paths).assertIsDirectoryContaining(getInfo(assertions), getActual(assertions), syntaxAndPattern);
  }

  // test for https://github.com/assertj/assertj/issues/2329
  @Test
  void should_check_files_relative_to_actual_directory() throws IOException {
    // GIVEN
    Path actual = createDirectory(tempDir.resolve("junit12854012982575283912"));
    createDirectory(actual.resolve("directory"));
    createFile(actual.resolve("test-6e672c4ba0be4f0886d434125a0d062c.txt"));
    // WHEN/THEN
    then(actual).isDirectoryContaining("glob:test-[0-9a-f]*.txt")
                .isDirectoryContaining("glob:*.txt")
                .isDirectoryContaining("regex:dir.*");
  }

  @Test
  void should_fail_as_actual_sub_directories_are_not_explored_whatever_the_given_pattern_is() throws IOException {
    // GIVEN
    Path subDirectory = createDirectory(tempDir.resolve("dir"));
    createFile(subDirectory.resolve("test.txt"));
    String[] patterns = { "glob:*.txt", "glob:**.txt" };
    for (String pattern : patterns) {
      // WHEN
      AssertionError assertionError = expectAssertionError(() -> assertThat(tempDir).isDirectoryContaining(pattern));
      // THEN
      then(assertionError).hasMessageContainingAll("The directory content was:", "[dir]");
    }
  }

  @Test
  void error_message_should_list_actual_directory_sorted_content() throws IOException {
    // GIVEN
    createDirectory(tempDir.resolve("directory1"));
    createDirectory(tempDir.resolve("directory2"));
    createFile(tempDir.resolve("file1.txt"));
    createFile(tempDir.resolve("file2.txt"));
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(tempDir).isDirectoryContaining("glob:*.java"));
    // THEN
    then(assertionError).hasMessageContainingAll("The directory content was:",
                                                 "[directory1, directory2, file1.txt, file2.txt]");
  }

}
