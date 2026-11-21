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
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class PathAssert_isDirectoryRecursivelyContaining_Predicate_Test {

  private static final String THE_GIVEN_FILTER_DESCRIPTION = "the given filter";

  @TempDir
  Path tempDir;

  @ParameterizedTest
  @MethodSource
  void should_pass_if_actual_contains_any_paths_matching_the_given_predicate(Predicate<Path> predicate) {
    // GIVEN
    // @format:off
    // The layout:
    //  tempDir
    //  |—— foo
    //  |    |—— foobar
    //  |         |—— foobar1.data
    //  |         |—— foobar2.json
    //  |—— foo2.data
    // @format:on
    createFile(tempDir, "foo2.data");
    Path fooDir = createDirectory(tempDir, "foo");
    createDirectory(fooDir, "foobar", "foobar1.data", "foobar2.json");
    // WHEN/THEN
    then(tempDir).isDirectoryRecursivelyContaining(predicate);
  }

  static Stream<Predicate<Path>> should_pass_if_actual_contains_any_paths_matching_the_given_predicate() {
    return Stream.of(path -> path.toString().contains("bar2"), // one match
                     path -> path.toString().endsWith("foobar2.json"), // one match
                     path -> path.toString().contains("foobar"), // 3 matches
                     path -> path.getParent() != null && path.getParent().toString().endsWith("foobar"), // two matches
                     path -> path.toString().contains("foo")); // all matches
  }

  @Test
  void should_fail_if_actual_does_not_exist() {
    // GIVEN
    Path notExistingPath = tempDir.resolve("doesnt-exist-file");
    Predicate<Path> anyPredicate = f -> true;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(notExistingPath).isDirectoryRecursivelyContaining(anyPredicate));
    // THEN
    then(assertionError).hasMessage(shouldExist(notExistingPath).create());
  }

  @Test
  void should_fail_if_actual_exists_but_is_not_a_directory() {
    // GIVEN
    Path rootDir = createDirectory(tempDir, "root", "foo2.data");
    Path existingPath = rootDir.resolve("foo2.data");
    Predicate<Path> alwaysTrue = f -> true;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(existingPath).isDirectoryRecursivelyContaining(alwaysTrue));
    // THEN
    then(assertionError).hasMessage(shouldBeDirectory(existingPath).create());
  }

  @Test
  void should_fail_if_actual_is_empty() {
    // GIVEN
    Predicate<Path> alwaysTrue = f -> true;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(tempDir).isDirectoryRecursivelyContaining(alwaysTrue));
    // THEN
    then(assertionError).hasMessage(directoryShouldContainRecursively(tempDir, emptyList(),
                                                                      THE_GIVEN_FILTER_DESCRIPTION).create());
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
  void should_pass_if_the_path_matching_the_given_predicate_is_in_a_sub_directory_of_actual() throws IOException {
    // GIVEN
    Path dir = Files.createDirectory(tempDir.resolve("dir"));
    Files.createFile(dir.resolve("test.txt"));
    Files.createFile(tempDir.resolve("test.java"));
    Path docs = Files.createDirectory(tempDir.resolve("docs"));
    Files.createDirectory(docs.resolve("music"));
    Path images = Files.createDirectory(docs.resolve("images"));
    Files.createFile(images.resolve("sun.png"));
    // WHEN/THEN
    then(tempDir).isDirectoryRecursivelyContaining(path -> path.toString().endsWith(".txt"))
                 .isDirectoryRecursivelyContaining(path -> path.toString().equals("dir"))
                 .isDirectoryRecursivelyContaining(path -> path.toString().endsWith("java"))
                 .isDirectoryRecursivelyContaining(path -> path.toString().endsWith(".png"))
                 .isDirectoryRecursivelyContaining(path -> path.toString()
                                                               .startsWith("docs/mus".replace('/', File.separatorChar)));
  }

  @Test
  void should_fail_and_list_sorted_relative_content_if_no_path_matches_the_given_predicate() throws IOException {
    // GIVEN
    Path dir = Files.createDirectory(tempDir.resolve("dir"));
    Files.createFile(dir.resolve("test.txt"));
    Files.createFile(tempDir.resolve("test.java"));
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(tempDir).isDirectoryRecursivelyContaining(path -> path.toString()
                                                                                                                     .equals("test.txt")));
    // THEN
    then(assertionError).hasMessageContainingAll("The directory content was:",
                                                 "[dir, dir/test.txt, test.java]".replace('/', File.separatorChar));
  }

}
