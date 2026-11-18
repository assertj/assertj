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
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.assertj.tests.core.testkit.PathFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class FileAssert_isDirectoryRecursivelyContaining_Predicate_Test {

  private static final String THE_GIVEN_FILTER_DESCRIPTION = "the given filter";

  @TempDir
  Path tempDir;

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    File actual = null;
    Predicate<File> anyFilter = f -> true;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isDirectoryRecursivelyContaining(anyFilter));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_throw_error_if_filter_is_null() {
    // GIVEN
    File actual = tempDir.toFile();
    Predicate<File> filter = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).isDirectoryRecursivelyContaining(filter));
    // THEN
    then(npe).hasMessage("The files filter should not be null");
  }

  @Test
  void should_fail_if_actual_does_not_exist() {
    // GIVEN
    File actual = PathFactory.createPath(tempDir).toFile();
    Predicate<File> anyFilter = f -> true;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isDirectoryRecursivelyContaining(anyFilter));
    // THEN
    then(assertionError).hasMessage(shouldBeDirectory(actual).create());
  }

  @Test
  void should_fail_if_actual_exists_but_is_not_a_directory() throws IOException {
    // GIVEN
    File actual = PathFactory.createFile(tempDir).toFile();
    Predicate<File> anyFilter = f -> true;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isDirectoryRecursivelyContaining(anyFilter));
    // THEN
    then(assertionError).hasMessage(shouldBeDirectory(actual).create());
  }

  @Test
  void should_fail_if_actual_is_empty() {
    // GIVEN
    File actual = tempDir.toFile();
    Predicate<File> alwaysTrue = f -> true;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isDirectoryRecursivelyContaining(alwaysTrue));
    // THEN
    then(assertionError).hasMessage(directoryShouldContainRecursively(actual, emptyList(),
                                                                      THE_GIVEN_FILTER_DESCRIPTION).create());
  }

  @Test
  void should_fail_if_actual_does_not_contain_any_files_matching_the_given_predicate() throws IOException {
    // GIVEN
    File actual = tempDir.toFile();
    createDirectory(tempDir, "foo", "foo2.txt");
    createDirectory(tempDir, "foo3");
    Predicate<File> filter = f -> f.getName().equals("foo2");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isDirectoryRecursivelyContaining(filter));
    // THEN
    then(assertionError).hasMessage(directoryShouldContainRecursively(actual, sortedFiles("foo", "foo/foo2.txt", "foo3"),
                                                                      THE_GIVEN_FILTER_DESCRIPTION).create());
  }

  @Test
  void should_fail_and_list_actual_directory_sorted_relative_content() throws IOException {
    // GIVEN
    File actual = tempDir.toFile();
    createDirectory(tempDir, "directory1", "file3.txt");
    createDirectory(tempDir, "directory2");
    Files.createFile(tempDir.resolve("file1.txt"));
    Files.createFile(tempDir.resolve("file2.txt"));
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isDirectoryRecursivelyContaining(file -> false));
    // THEN
    List<File> expectedContent = sortedFiles("directory1", "directory1/file3.txt", "directory2", "file1.txt", "file2.txt");
    then(assertionError).hasMessage(directoryShouldContainRecursively(actual, expectedContent,
                                                                      THE_GIVEN_FILTER_DESCRIPTION).create());
  }

  @ParameterizedTest
  @MethodSource("matchingPredicates")
  void should_pass_if_actual_contains_any_files_matching_the_given_predicate(Predicate<File> predicate) throws IOException {
    // GIVEN
    File actual = tempDir.toFile();
    createDefaultFixturePaths();
    // WHEN/THEN
    then(actual).isDirectoryRecursivelyContaining(predicate);
  }

  static Stream<Predicate<File>> matchingPredicates() {
    return Stream.of(f -> f.getName().contains("bar2"), // one match
                     f -> f.getName().equals("foobar2.json"), // one match
                     f -> f.getName().contains("foobar"), // some matches
                     f -> f.getParentFile() != null && f.getParentFile().getName().equals("foobar"), // some matches
                     f -> f.getName().contains("foo")); // all matches
  }

  @Test
  void should_pass_if_the_file_matching_the_given_predicate_is_in_a_sub_directory_of_actual() throws IOException {
    // GIVEN
    File actual = tempDir.toFile();
    createDirectory(tempDir, "dir", "test.txt");
    Files.createFile(tempDir.resolve("test.java"));
    Path docs = createDirectory(tempDir, "docs");
    createDirectory(docs, "music");
    createDirectory(docs, "images", "sun.png");
    // WHEN/THEN
    then(actual).isDirectoryRecursivelyContaining(f -> f.getName().endsWith(".txt"))
                .isDirectoryRecursivelyContaining(f -> f.getName().equals("dir"))
                .isDirectoryRecursivelyContaining(f -> f.getName().endsWith("java"))
                .isDirectoryRecursivelyContaining(f -> f.getName().endsWith(".png"))
                .isDirectoryRecursivelyContaining(f -> f.getName().startsWith("mus"));
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
