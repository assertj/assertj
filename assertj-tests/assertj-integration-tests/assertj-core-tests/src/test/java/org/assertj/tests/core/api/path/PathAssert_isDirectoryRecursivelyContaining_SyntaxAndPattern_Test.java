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

import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeDirectory.shouldBeDirectory;
import static org.assertj.core.error.ShouldContainRecursively.directoryShouldContainRecursively;
import static org.assertj.core.error.ShouldExist.shouldExist;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PathAssert_isDirectoryRecursivelyContaining_SyntaxAndPattern_Test {

  private static final String TXT_EXTENSION_PATTERN = "regex:.+\\.txt";
  private static final String TXT_EXTENSION_PATTERN_DESCRIPTION = "the '%s' pattern".formatted(TXT_EXTENSION_PATTERN);

  @TempDir
  Path tempDir;

  @ParameterizedTest
  @ValueSource(strings = { "regex:.+oo2\\.data", "regex:.+\\.json", "regex:.+bar2\\.json" })
  void should_pass_if_actual_contains_one_file_matching_the_given_pathMatcherPattern(String pattern) {
    // GIVEN
    createDefaultFixturePaths();
    // WHEN/THEN
    then(tempDir).isDirectoryRecursivelyContaining(pattern);
  }

  @ParameterizedTest
  @ValueSource(strings = { "regex:.+\\.data", "regex:.+foobar.*", "regex:root.+foo.*" })
  void should_pass_if_actual_contains_some_paths_matching_the_given_pathMatcherPattern(String pattern) {
    // GIVEN
    createDefaultFixturePaths();
    // WHEN/THEN
    then(tempDir).isDirectoryRecursivelyContaining(pattern);
  }

  @Test
  void should_pass_if_all_actual_paths_matching_the_given_pathMatcherPattern() {
    // GIVEN
    Path fooDir = createDirectory(tempDir, "foo", "foo2.data");
    createDirectory(fooDir, "foo3");
    // WHEN/THEN
    then(tempDir).isDirectoryRecursivelyContaining("regex:.*foo.*|.*tmp");
  }

  @Test
  void should_fail_if_actual_does_not_exist() {
    // GIVEN
    Path notExistingPath = tempDir.resolve("doesnt-exist-file");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(notExistingPath).isDirectoryRecursivelyContaining(TXT_EXTENSION_PATTERN));
    // THEN
    then(assertionError).hasMessage(shouldExist(notExistingPath).create());
  }

  @Test
  void should_fail_if_actual_exists_but_is_not_a_directory() {
    // GIVEN
    Path rootDir = createDirectory(tempDir, "root", "foo2.data");
    Path existingPath = rootDir.resolve("foo2.data");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(existingPath).isDirectoryRecursivelyContaining(TXT_EXTENSION_PATTERN));
    // THEN
    then(assertionError).hasMessage(shouldBeDirectory(existingPath).create());
  }

  @Test
  void should_fail_if_actual_is_empty() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(tempDir).isDirectoryRecursivelyContaining(TXT_EXTENSION_PATTERN));
    // THEN
    then(assertionError).hasMessage(directoryShouldContainRecursively(tempDir, emptyList(),
                                                                      TXT_EXTENSION_PATTERN_DESCRIPTION).create());
  }

  private void createDefaultFixturePaths() {
    // @format:off
    // The layout:
    // tempDir
    // |—— root
    //      |—— foo
    //      |    |—— foobar
    //      |         |—— foobar1.data
    //      |         |—— foobar2.json
    //      |—— foo2.data
    // @format:on
    Path fooDir = createDirectory(createDirectory(tempDir, "root", "foo2.data"), "foo");
    createDirectory(fooDir, "foobar", "foobar1.data", "foobar2.json");
  }

  private static Path createDirectory(Path parent, String name, String... files) {
    Path directory = parent.resolve(name);
    try {
      Files.createDirectory(directory);
    } catch (IOException e) {
      throw new UncheckedIOException("error during fixture directory creation", e);
    }
    stream(files).forEach(file -> createFile(directory, file));
    return directory;
  }

  private static void createFile(Path directory, String name) {
    try {
      Files.createFile(directory.resolve(name));
    } catch (IOException e) {
      throw new UncheckedIOException("error during fixture file creation", e);
    }
  }

  @Test
  void should_pass_if_the_path_matching_the_given_pattern_is_in_a_sub_directory_of_actual() throws IOException {
    // GIVEN
    Path dir = Files.createDirectory(tempDir.resolve("dir"));
    Files.createFile(dir.resolve("test.txt"));
    Files.createFile(tempDir.resolve("test.java"));
    Path docs = Files.createDirectory(tempDir.resolve("docs"));
    Files.createDirectory(docs.resolve("music"));
    Path images = Files.createDirectory(docs.resolve("images"));
    Files.createFile(images.resolve("sun.png"));
    // WHEN/THEN
    then(tempDir).isDirectoryRecursivelyContaining("glob:*.java")
                 .isDirectoryRecursivelyContaining("glob:**.txt")
                 .isDirectoryRecursivelyContaining("glob:**.png")
                 .isDirectoryRecursivelyContaining("glob:**mus*")
                 .isDirectoryRecursivelyContaining("glob:dir");
  }

  @Test
  void should_fail_and_list_sorted_relative_content_if_no_path_matches_the_given_pattern() throws IOException {
    // GIVEN
    Path dir = Files.createDirectory(tempDir.resolve("dir"));
    Files.createFile(dir.resolve("test.txt"));
    Files.createFile(tempDir.resolve("test.java"));
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(tempDir).isDirectoryRecursivelyContaining("glob:*.txt"));
    // THEN
    then(assertionError).hasMessageContainingAll("The directory content was:",
                                                 "[dir, dir/test.txt, test.java]".replace('/', File.separatorChar));
  }

}
