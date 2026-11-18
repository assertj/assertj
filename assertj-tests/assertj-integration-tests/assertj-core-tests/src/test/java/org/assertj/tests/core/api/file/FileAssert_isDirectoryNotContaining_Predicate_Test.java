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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeDirectory.shouldBeDirectory;
import static org.assertj.core.error.ShouldNotContain.directoryShouldNotContain;
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

class FileAssert_isDirectoryNotContaining_Predicate_Test {

  private static final Predicate<File> JAVA_SOURCE = file -> file.getName().endsWith(".java");

  @TempDir
  Path tempDir;

  @Test
  void should_fail_if_filter_is_null() {
    // GIVEN
    File actual = tempDir.toFile();
    Predicate<File> filter = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).isDirectoryNotContaining(filter));
    // THEN
    then(npe).hasMessage("The files filter should not be null");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    File actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isDirectoryNotContaining(JAVA_SOURCE));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_exist() {
    // GIVEN
    File actual = PathFactory.createPath(tempDir).toFile();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isDirectoryNotContaining(JAVA_SOURCE));
    // THEN
    then(assertionError).hasMessage(shouldBeDirectory(actual).create());
  }

  @Test
  void should_fail_if_actual_exists_but_is_not_a_directory() throws IOException {
    // GIVEN
    File actual = PathFactory.createFile(tempDir).toFile();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isDirectoryNotContaining(JAVA_SOURCE));
    // THEN
    then(assertionError).hasMessage(shouldBeDirectory(actual).create());
  }

  @Test
  void should_fail_if_one_actual_file_matches_the_filter() throws IOException {
    // GIVEN
    File actual = createDirectory("Test.java");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isDirectoryNotContaining(JAVA_SOURCE));
    // THEN
    then(assertionError).hasMessage(directoryShouldNotContain(actual, List.of(new File(actual, "Test.java")),
                                                              "the given filter").create());
  }

  @Test
  void should_fail_if_all_actual_files_match_the_filter() throws IOException {
    // GIVEN
    File actual = createDirectory("Utils.java", "Test.java");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isDirectoryNotContaining(JAVA_SOURCE));
    // THEN
    List<File> matchingFiles = List.of(new File(actual, "Test.java"), new File(actual, "Utils.java"));
    then(assertionError).hasMessage(directoryShouldNotContain(actual, matchingFiles, "the given filter").create());
  }

  @Test
  void should_fail_if_some_actual_files_match_the_filter() throws IOException {
    // GIVEN
    File actual = createDirectory("Test.java", "Test.class", "Utils.class", "Utils.java", "application.yml");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isDirectoryNotContaining(JAVA_SOURCE));
    // THEN
    List<File> matchingFiles = List.of(new File(actual, "Test.java"), new File(actual, "Utils.java"));
    then(assertionError).hasMessage(directoryShouldNotContain(actual, matchingFiles, "the given filter").create());
  }

  @Test
  void should_pass_if_actual_does_not_contain_files_matching_the_given_filter() throws IOException {
    // GIVEN
    File actual = createDirectory("Test.class");
    // WHEN/THEN
    then(actual).isDirectoryNotContaining(JAVA_SOURCE);
  }

  @Test
  void should_pass_if_actual_is_empty() throws IOException {
    // GIVEN
    File actual = createDirectory();
    // WHEN/THEN
    then(actual).isDirectoryNotContaining(JAVA_SOURCE);
  }

  private File createDirectory(String... fileNames) throws IOException {
    Path directory = Files.createDirectory(PathFactory.createPath(tempDir));
    for (String fileName : fileNames) {
      Files.createFile(directory.resolve(fileName));
    }
    return directory.toFile();
  }

}
