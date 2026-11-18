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
package org.assertj.tests.core.api.file;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeDirectory.shouldBeDirectory;
import static org.assertj.core.error.ShouldContainRecursively.directoryShouldContainRecursively;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.assertj.tests.core.testkit.PathFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class FileAssert_isDirectoryRecursivelyContaining_SyntaxAndPattern_Test {

  private static final String TXT_EXTENSION_PATTERN = "regex:.+\\.txt";
  private static final String TXT_EXTENSION_PATTERN_DESCRIPTION = "the '%s' pattern".formatted(TXT_EXTENSION_PATTERN);

  @TempDir
  Path tempDir;

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    File actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isDirectoryRecursivelyContaining(TXT_EXTENSION_PATTERN));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_throw_error_if_syntax_and_pattern_is_null() {
    // GIVEN
    File actual = tempDir.toFile();
    String syntaxAndPattern = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).isDirectoryRecursivelyContaining(syntaxAndPattern));
    // THEN
    then(npe).hasMessage("The syntax and pattern should not be null");
  }

  @Test
  void should_fail_if_actual_does_not_exist() {
    // GIVEN
    File actual = PathFactory.createPath(tempDir).toFile();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isDirectoryRecursivelyContaining(TXT_EXTENSION_PATTERN));
    // THEN
    then(assertionError).hasMessage(shouldBeDirectory(actual).create());
  }

  @Test
  void should_fail_if_actual_exists_but_is_not_a_directory() throws IOException {
    // GIVEN
    File actual = PathFactory.createFile(tempDir).toFile();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isDirectoryRecursivelyContaining(TXT_EXTENSION_PATTERN));
    // THEN
    then(assertionError).hasMessage(shouldBeDirectory(actual).create());
  }

  @Test
  void should_fail_if_actual_is_empty() {
    // GIVEN
    File actual = tempDir.toFile();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isDirectoryRecursivelyContaining(TXT_EXTENSION_PATTERN));
    // THEN
    then(assertionError).hasMessage(directoryShouldContainRecursively(actual, emptyList(),
                                                                      TXT_EXTENSION_PATTERN_DESCRIPTION).create());
  }

  @Test
  void should_fail_if_the_file_matching_the_given_pattern_is_in_a_sub_directory_of_actual() throws IOException {
    // GIVEN
    File actual = tempDir.toFile();
    createDirectory(tempDir, "dir", "test.txt");
    Files.createFile(tempDir.resolve("test.java"));
    String syntaxAndPattern = "glob:*.txt";
    // WHEN glob:*.txt does not traverse directories thus dir/test.txt does not match glob:*.txt but matches glob:**.txt
    var assertionError = expectAssertionError(() -> assertThat(actual).isDirectoryRecursivelyContaining(syntaxAndPattern));
    // THEN
    then(assertionError).hasMessage(directoryShouldContainRecursively(actual, sortedFiles("dir", "dir/test.txt", "test.java"),
                                                                      "the 'glob:*.txt' pattern").create());
  }

  @ParameterizedTest
  @ValueSource(strings = { "regex:.+oo2\\.data", "regex:.+\\.json", "regex:.+bar2\\.json" })
  void should_pass_if_actual_contains_one_file_matching_the_given_pattern(String syntaxAndPattern) throws IOException {
    // GIVEN
    File actual = tempDir.toFile();
    createDefaultFixturePaths();
    // WHEN/THEN
    then(actual).isDirectoryRecursivelyContaining(syntaxAndPattern);
  }

  @ParameterizedTest
  @ValueSource(strings = { "regex:.+\\.data", "regex:.+foobar.*", "regex:root.+foo.*" })
  void should_pass_if_actual_contains_some_files_matching_the_given_pattern(String syntaxAndPattern) throws IOException {
    // GIVEN
    File actual = tempDir.toFile();
    createDefaultFixturePaths();
    // WHEN/THEN
    then(actual).isDirectoryRecursivelyContaining(syntaxAndPattern);
  }

  @Test
  void should_pass_if_all_actual_files_match_the_given_pattern() throws IOException {
    // GIVEN
    File actual = tempDir.toFile();
    Path fooDir = createDirectory(tempDir, "foo", "foo2.data");
    createDirectory(fooDir, "foo3");
    // WHEN/THEN
    then(actual).isDirectoryRecursivelyContaining("regex:.*foo.*|.*tmp");
  }

  @Test
  void should_pass_if_the_file_matching_the_given_pattern_is_in_a_sub_directory_of_actual() throws IOException {
    // GIVEN
    File actual = tempDir.toFile();
    createDirectory(tempDir, "dir", "test.txt");
    Files.createFile(tempDir.resolve("test.java"));
    Path docs = createDirectory(tempDir, "docs");
    createDirectory(docs, "music");
    createDirectory(docs, "images", "sun.png");
    // WHEN/THEN
    then(actual).isDirectoryRecursivelyContaining("glob:*.java")
                .isDirectoryRecursivelyContaining("glob:**.txt")
                .isDirectoryRecursivelyContaining("glob:**.png")
                .isDirectoryRecursivelyContaining("glob:**mus*")
                .isDirectoryRecursivelyContaining("glob:dir");
  }

  private void createDefaultFixturePaths() throws IOException {
    // @format:off
    // The layout:
    //  root
    //  |—— foo
    //  |    |—— foobar
    //  |         |—— foobar1.data
    //  |         |—— foobar2.json
    //  |—— foo2.data
    // @format:on
    Path rootDir = createDirectory(tempDir, "root", "foo2.data");
    Path fooDir = createDirectory(rootDir, "foo");
    createDirectory(fooDir, "foobar", "foobar1.data", "foobar2.json");
  }

  private static Path createDirectory(Path parent, String name, String... fileNames) throws IOException {
    Path directory = Files.createDirectory(parent.resolve(name));
    for (String fileName : fileNames) {
      Files.createFile(directory.resolve(fileName));
    }
    return directory;
  }

  // relative content is sorted with File natural ordering, which is platform dependent
  private static List<File> sortedFiles(String... relativePaths) {
    return Stream.of(relativePaths).map(File::new).sorted().toList();
  }

}
