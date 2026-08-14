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
package org.assertj.core.api.file;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.Files.newFile;
import static org.assertj.core.util.Files.newFolder;
import static org.mockito.Mockito.verify;

import java.io.File;

import org.assertj.core.api.FileAssert;
import org.assertj.core.api.FileAssertBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for <code>{@link FileAssert#isDirectoryContaining(String)}</code>
 *
 * @author Valeriy Vyrva
 */
class FileAssert_isDirectoryContaining_SyntaxAndPattern_Test extends FileAssertBaseTest {

  @TempDir
  private File tempDir;
  private final String syntaxAndPattern = "glob:*.java";

  @Override
  protected FileAssert invoke_api_method() {
    return assertions.isDirectoryContaining(syntaxAndPattern);
  }

  @Override
  protected void verify_internal_effects() {
    verify(files).assertIsDirectoryContaining(getInfo(assertions), getActual(assertions), syntaxAndPattern);
  }

  // test for https://github.com/assertj/assertj/issues/2329
  @Test
  void should_check_files_relative_to_actual_directory() {
    // GIVEN
    File actual = newFolder(tempDir.getAbsolutePath() + "/junit12854012982575283912");
    newFolder(actual.getAbsolutePath() + "/dir");
    newFile(actual.getAbsolutePath() + "/test-6e672c4ba0be4f0886d434125a0d062c.txt");
    // WHEN/THEN
    then(actual).isDirectoryContaining("glob:test-[0-9a-f]*.txt")
                .isDirectoryContaining("glob:*.txt")
                .isDirectoryContaining("regex:d.r");
  }

  @Test
  void should_fail_as_actual_sub_directories_are_not_explored_whatever_the_given_pattern_is() {
    // GIVEN
    File subDirectory = newFolder(tempDir.getAbsolutePath() + "/dir");
    newFile(subDirectory.getAbsolutePath() + "/test.txt");
    newFile(tempDir.getAbsolutePath() + "/test.java");
    String[] patterns = { "glob:*.txt", "glob:**.txt" };
    for (String pattern : patterns) {
      // WHEN
      AssertionError assertionError = expectAssertionError(() -> assertThat(tempDir).isDirectoryContaining(pattern));
      // THEN
      then(assertionError).hasMessageContainingAll("The directory content was:", "[dir, test.java]");
    }
  }

  @Test
  void error_message_should_list_actual_directory_sorted_content() {
    // GIVEN
    newFolder(tempDir.getAbsolutePath() + "/directory1");
    newFolder(tempDir.getAbsolutePath() + "/directory2");
    newFile(tempDir.getAbsolutePath() + "/file1.txt");
    newFile(tempDir.getAbsolutePath() + "/file2.txt");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(tempDir).isDirectoryContaining("glob:*.java"));
    // THEN
    then(assertionError).hasMessageContainingAll("The directory content was:",
                                                 "[directory1, directory2, file1.txt, file2.txt]");
  }

}
