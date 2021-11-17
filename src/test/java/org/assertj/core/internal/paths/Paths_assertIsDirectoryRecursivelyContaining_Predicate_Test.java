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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.internal.paths;

import static java.nio.file.Files.createFile;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeDirectory.shouldBeDirectory;
import static org.assertj.core.error.ShouldContainRecursively.directoryShouldContainRecursively;
import static org.assertj.core.error.ShouldExist.shouldExist;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.Lists.list;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.assertj.core.api.AbstractThrowableAssert;
import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.ThrowableAssert;
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.internal.Paths;
import org.assertj.core.internal.PathsBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for <code>{@link Paths#assertIsDirectoryRecursivelyContaining(AssertionInfo, Path, java.util.function.Predicate)}</code>
 *
 * @author David Haccoun
 */
class Paths_assertIsDirectoryRecursivelyContaining_Predicate_Test extends PathsBaseTest {
  /**
   * AssertionInfo
   */
  private static final AssertionInfo INFO = someInfo();
  /**
   * Filter
   */
  private static final String THE_GIVEN_FILTER_DESCRIPTION = "the given filter";

  /**
   * Actual matches class
   */
  @TestInstance(PER_CLASS)
  @Nested
    /* default */ class Actual_matches {
    @BeforeEach
      /* default */ void createFixturePaths() throws IOException {
      // @format:off
      // The layout:
      //  root
      //  |—— foo
      //  |    |—— foobar
      //  |         |—— foobar1.data
      //  |         |—— foobar2.json
      //  |—— foo2.data
      // @format:on
      final Path rootDirPath = tempDir.resolve("root");
      final Path rootDir = Files.createDirectory(rootDirPath);
      createFile(createFoo2DataPath(rootDir));
      final Path fooDir = Files.createDirectory(createFooDataPath(rootDir));
      final Path foobar = Files.createDirectory(createFooBarPath(fooDir));
      createFile(createFooBar1Path(foobar));
      createFile(createFooBar2Path(foobar));
    }

    private Path createFoo2DataPath(final Path rootDir) { return rootDir.resolve("foo2.data"); }
    private Path createFooDataPath(final Path rootDir) { return rootDir.resolve("foo"); }
    private Path createFooBarPath(final Path fooDir) { return fooDir.resolve("foobar"); }
    private Path createFooBar1Path(final Path foobar) { return foobar.resolve("foobar1.data"); }
    private Path createFooBar2Path(final Path foobar) { return foobar.resolve("foobar2.json"); }

    @ParameterizedTest
    @MethodSource("foundMatchProvider")
    /* default */ void should_pass_if_actual_contains_any_paths_matching_the_given_predicate(final Predicate<Path> predicate) {
      paths.assertIsDirectoryRecursivelyContaining(someInfo(), tempDir, predicate);
    }

    private Stream<Predicate<Path>> foundMatchProvider() {
      return Stream.of(path -> path.toString().contains("bar2"), // one match
                       path -> path.toString().endsWith("foobar2.json"), // one match
                       path -> path.toString().contains("foobar"), // 3 matches
                       path -> path.getParent().toString().endsWith("foobar"), // one match
                       path -> path.toString().contains("foo")); // all matches
    }
  }

  @Test
  /* default */ void should_fail_if_actual_does_not_exist() {
    // GIVEN
    final Path notExistingPath = tempDir.resolve("non-existent-file");
    final Predicate<Path> anyPredicate = f -> true;
    // WHEN
    final AssertionError error = expectAssertionError(() -> paths.assertIsDirectoryRecursivelyContaining(INFO, notExistingPath, anyPredicate));
    // THEN
    final ErrorMessageFactory message = shouldExist(notExistingPath);
    final String text = createMessage(message);
    makeThrowableAssert(then(error), text);
  }

  @Test
  /* default */ void should_fail_if_actual_exists_but_is_not_a_directory() throws IOException {
    // GIVEN
    final Path rootDirPath = tempDir.resolve("root");
    final Path rootDir = Files.createDirectory(rootDirPath);
    final Path existingPath = createFile(createFoo2DataPath(rootDir));
    final Predicate<Path> alwaysTrue = path -> true;
    // WHEN
    final AssertionError error = expectAssertionError(() -> paths.assertIsDirectoryRecursivelyContaining(INFO, existingPath, alwaysTrue));
    // THEN
    final ErrorMessageFactory message = shouldBeDirectory(existingPath);
    final String text = createMessage(message);
    makeThrowableAssert(then(error), text);
  }

  @Test
  /* default */ void should_fail_if_actual_is_empty() {
    // GIVEN
    final Predicate<Path> alwaysTrue = f -> true;
    // WHEN
    final AssertionError error = expectAssertionError(() -> paths.assertIsDirectoryRecursivelyContaining(INFO, tempDir, alwaysTrue));
    // THEN
    final ErrorMessageFactory message = directoryShouldContainRecursively(tempDir, emptyList(), THE_GIVEN_FILTER_DESCRIPTION);
    final String text = createMessage(message);
    makeThrowableAssert(then(error), text);
  }

  @Test
  /* default */ void should_fail_if_actual_does_not_contain_any_paths_matching_the_given_predicate() throws IOException {
    // GIVEN
    final Path fooDirPath = tempDir.resolve("foo");
    final Path fooDir = Files.createDirectory(fooDirPath);
    final Path foo2Path = createFoo2DataPath(fooDir);
    createFile(foo2Path);
    final Path foo3Path = createFoo3DataPath(fooDir);
    Files.createDirectory(foo3Path);
    final Predicate<Path> alwaysFalse = f -> false;
    // WHEN
    final AssertionError error = expectAssertionError(() -> paths.assertIsDirectoryRecursivelyContaining(INFO, tempDir, alwaysFalse));
    // THEN
    final List<Path> fooDirContent = list(fooDir, foo2Path, foo3Path);
    final ErrorMessageFactory message = directoryShouldContainRecursively(tempDir, fooDirContent, THE_GIVEN_FILTER_DESCRIPTION);
    final String text = createMessage(message);
    makeThrowableAssert(then(error), text);
  }
  private String createMessage(final ErrorMessageFactory error) { return error.create(); }
  private ThrowableAssert makeThrowableAssert(final AbstractThrowableAssert throwableAssert, final String text) {
    return (ThrowableAssert) throwableAssert.hasMessage(text);
  }
  private Path createFoo2DataPath(final Path dir) { return dir.resolve("foo2.data"); }
  private Path createFoo3DataPath(final Path dir) { return dir.resolve("foo3"); }

}
