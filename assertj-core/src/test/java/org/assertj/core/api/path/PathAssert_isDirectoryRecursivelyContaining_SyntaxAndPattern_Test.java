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
 * Tests for <code>{@link PathAssert#isDirectoryRecursivelyContaining(String)}</code>
 *
 * @author David Haccoun
 */
class PathAssert_isDirectoryRecursivelyContaining_SyntaxAndPattern_Test extends PathAssertBaseTest {

  private final String syntaxAndPattern = "glob:*.java";
  @TempDir
  private Path tempDir;

  @Override
  protected PathAssert invoke_api_method() {
    return assertions.isDirectoryRecursivelyContaining(syntaxAndPattern);
  }

  @Override
  protected void verify_internal_effects() {
    verify(paths).assertIsDirectoryRecursivelyContaining(getInfo(assertions), getActual(assertions), syntaxAndPattern);
  }

  @Test
  void should_pass_if_the_file_matching_the_given_pattern_is_in_a_sub_directory_of_actual() throws IOException {
    // GIVEN
    Path dir = createDirectory(tempDir.resolve("dir"));
    createFile(dir.resolve("test.txt"));
    createFile(tempDir.resolve("test.java"));
    Path docs = createDirectory(tempDir.resolve("docs"));
    createDirectory(docs.resolve("music"));
    Path images = createDirectory(docs.resolve("images"));
    createFile(images.resolve("sun.png"));
    // WHEN/THEN
    then(tempDir).isDirectoryRecursivelyContaining("glob:*.java")
                 .isDirectoryRecursivelyContaining("glob:**.txt")
                 .isDirectoryRecursivelyContaining("glob:**.png")
                 .isDirectoryRecursivelyContaining("glob:**mus*")
                 .isDirectoryRecursivelyContaining("glob:dir");
  }

  @Test
  void should_fail_if_the_file_matching_the_given_pattern_is_in_a_sub_directory_of_actual() throws IOException {
    // GIVEN
    Path dir = createDirectory(tempDir.resolve("dir"));
    createFile(dir.resolve("test.txt"));
    createFile(tempDir.resolve("test.java"));
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(tempDir).isDirectoryRecursivelyContaining("glob:*.txt"));
    // THEN
    then(assertionError).hasMessageContainingAll("The directory content was:", "[dir, dir/test.txt, test.java]");
  }

  @Test
  void should_fail_if_actual_does_not_contain_any_paths_matching_the_given_predicate() throws IOException {
    // GIVEN
    Path fooDir = createDirectory(tempDir.resolve("foo"));
    createDirectory(fooDir.resolve("foo3"));
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(tempDir).isDirectoryRecursivelyContaining("glob:*.txt"));
    // THEN
    then(assertionError).hasMessageContainingAll("The directory content was:", "[foo, foo/foo3]");
  }
}
