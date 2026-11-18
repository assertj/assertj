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
import static org.assertj.core.error.ShouldContain.directoryShouldContain;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

import org.assertj.tests.core.testkit.PathFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileAssert_isDirectoryContaining_Predicate_Test {

  private static final Predicate<File> JAVA_SOURCE = file -> file.getName().endsWith(".java");

  @TempDir
  Path tempDir;

  @Test
  void should_fail_if_filter_is_null() {
    // GIVEN
    File actual = tempDir.toFile();
    Predicate<File> filter = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).isDirectoryContaining(filter));
    // THEN
    then(npe).hasMessage("The files filter should not be null");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    File actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isDirectoryContaining(JAVA_SOURCE));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_exist() {
    // GIVEN
    File actual = PathFactory.createPath(tempDir).toFile();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isDirectoryContaining(JAVA_SOURCE));
    // THEN
    then(assertionError).hasMessage(shouldBeDirectory(actual).create());
  }

  @Test
  void should_fail_if_actual_exists_but_is_not_a_directory() throws IOException {
    // GIVEN
    File actual = PathFactory.createFile(tempDir).toFile();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isDirectoryContaining(JAVA_SOURCE));
    // THEN
    then(assertionError).hasMessage(shouldBeDirectory(actual).create());
  }

  @Test
  void should_fail_if_actual_is_empty() throws IOException {
    // GIVEN
    File actual = createDirectory();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isDirectoryContaining(JAVA_SOURCE));
    // THEN
    then(assertionError).hasMessage(directoryShouldContain(actual, emptyList(), "the given filter").create());
  }

  @Test
  void should_fail_if_actual_does_not_contain_any_files_matching_the_given_filter() throws IOException {
    // GIVEN
    File actual = createDirectory("Test.class");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isDirectoryContaining(JAVA_SOURCE));
    // THEN
    then(assertionError).hasMessage(directoryShouldContain(actual, List.of(new File(actual, "Test.class")),
                                                           "the given filter").create());
  }

  @Test
  void should_fail_with_error_message_listing_actual_directory_sorted_content() throws IOException {
    // GIVEN
    File actual = createDirectory("file2.txt", "file1.txt");
    Files.createDirectory(actual.toPath().resolve("directory2"));
    Files.createDirectory(actual.toPath().resolve("directory1"));
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isDirectoryContaining(file -> false));
    // THEN
    then(assertionError).hasMessageContainingAll("The directory content was:",
                                                 "[directory1, directory2, file1.txt, file2.txt]");
  }

  @Test
  void should_pass_if_actual_contains_a_file_matching_the_given_filter() throws IOException {
    // GIVEN
    File actual = createDirectory("Test.java");
    // WHEN/THEN
    then(actual).isDirectoryContaining(JAVA_SOURCE);
  }

  @Test
  void should_pass_if_all_actual_files_match_the_given_filter() throws IOException {
    // GIVEN
    File actual = createDirectory("Test.java", "Utils.java");
    // WHEN/THEN
    then(actual).isDirectoryContaining(JAVA_SOURCE);
  }

  @Test
  void should_pass_if_actual_contains_at_least_one_file_matching_the_given_filter() throws IOException {
    // GIVEN
    File actual = createDirectory("Test.java", "Test.class", "Utils.class", "Utils.java", "application.yml");
    // WHEN/THEN
    then(actual).isDirectoryContaining(JAVA_SOURCE);
  }

  private File createDirectory(String... fileNames) throws IOException {
    Path directory = Files.createDirectory(PathFactory.createPath(tempDir));
    for (String fileName : fileNames) {
      Files.createFile(directory.resolve(fileName));
    }
    return directory.toFile();
  }

}
