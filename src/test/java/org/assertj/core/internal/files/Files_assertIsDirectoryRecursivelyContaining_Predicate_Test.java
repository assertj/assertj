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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.internal.files;

import static org.assertj.core.error.ShouldBeDirectory.shouldBeDirectory;
import static org.assertj.core.error.ShouldContainRecursively.directoryShouldContainRecursively;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.Lists.emptyList;
import static org.assertj.core.util.Lists.list;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Files;
import org.assertj.core.internal.FilesSimpleBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for <code>{@link Files#assertIsDirectoryRecursivelyContaining(AssertionInfo, File, java.util.function.Predicate)}</code>
 *
 * @author David Haccoun
 */
public class Files_assertIsDirectoryRecursivelyContaining_Predicate_Test extends FilesSimpleBaseTest {

  private static final String THE_GIVEN_FILTER_DESCRIPTION = "the given filter";

  @TestInstance(PER_CLASS)
  @Nested
  class Actual_matches {

    @BeforeEach
    void createFixturePaths() {
      // @format:off
      // The layout:
      //  root
      //  |—— foo
      //  |    |—— foobar
      //  |         |—— foobar1.data
      //  |         |—— foobar2.json
      //  |—— foo2.data
      // @format:on
      Path rootDir = createDirectoryWithDefaultParent("root", "foo2.data");
      Path fooDir = createDirectory(rootDir, "foo");
      createDirectory(fooDir, "foobar", "foobar1.data", "foobar2.json");
    }

    @ParameterizedTest
    @MethodSource("foundMatchProvider")
    void should_pass_if_actual_contains_any_files_matching_the_given_predicate(Predicate<File> predicate) {
      files.assertIsDirectoryRecursivelyContaining(INFO, tempDirAsFile, predicate);
    }

    private Stream<Predicate<File>> foundMatchProvider() {
      return Stream.of(f -> f.getName().contains("bar2"), // one match
                       f -> f.getName().equals("foobar2.json"), // one match
                       f -> f.getName().contains("foobar"), // some matches
                       f -> f.getParentFile().getName().equals("foobar"), // some matches
                       f -> f.getName().contains("foo")); // all matches
    }

  }

  @Test
  void should_fail_if_actual_does_not_exist() {
    // GIVEN
    File notExistingFile = new File("foo/bar/doesnt-exist-file");
    Predicate<File> anyPredicate = f -> true;
    // WHEN
    expectAssertionError(() -> files.assertIsDirectoryRecursivelyContaining(INFO, notExistingFile, anyPredicate));
    // THEN
    verify(failures).failure(INFO, shouldBeDirectory(notExistingFile));
  }

  @Test
  void should_fail_if_actual_exists_but_is_not_a_directory() throws IOException {
    // GIVEN
    File existingFile = java.nio.file.Files.createFile(tempDir.resolve("FooFile.txt")).toFile();
    Predicate<File> anyPredicate = f -> true;
    // WHEN
    expectAssertionError(() -> files.assertIsDirectoryRecursivelyContaining(INFO, existingFile, anyPredicate));
    // THEN
    verify(failures).failure(INFO, shouldBeDirectory(existingFile));
  }

  @Test
  void should_fail_if_actual_is_empty() {
    // GIVEN
    Predicate<File> alwaysTrue = f -> true;
    // WHEN
    expectAssertionError(() -> files.assertIsDirectoryRecursivelyContaining(INFO, tempDirAsFile, alwaysTrue));
    // THEN
    verify(failures).failure(INFO, directoryShouldContainRecursively(tempDirAsFile, emptyList(), THE_GIVEN_FILTER_DESCRIPTION));
  }

  @Test
  void should_fail_if_actual_does_not_contain_any_files_matching_the_given_pathMatcherPattern() {
    // GIVEN
    Path fooDir = createDirectory(tempDir, "foo", "foo2.data");
    createDirectory(fooDir, "foo3");
    // WHEN
    expectAssertionError(() -> files.assertIsDirectoryRecursivelyContaining(INFO, tempDirAsFile,
                                                                            f -> f.getName().equals("foo2")));
    // THEN
    verify(failures).failure(INFO, directoryShouldContainRecursively(tempDirAsFile,
                                                                     list(new File(tempDirAsFile, "foo"),
                                                                          new File(tempDirAsFile,
                                                                                   "foo/foo2.data"),
                                                                          new File(tempDirAsFile, "foo/foo3")),
                                                                     THE_GIVEN_FILTER_DESCRIPTION));
  }

}
