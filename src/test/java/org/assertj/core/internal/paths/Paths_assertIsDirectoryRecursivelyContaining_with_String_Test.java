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

import static java.lang.String.format;
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

import org.assertj.core.api.AbstractThrowableAssert;
import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.ThrowableAssert;
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.internal.Paths;
import org.assertj.core.internal.PathsBaseTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for <code>{@link Paths#assertIsDirectoryRecursivelyContaining(AssertionInfo, Path, String)}</code>
 *
 * @author David Haccoun
 */
class Paths_assertIsDirectoryRecursivelyContaining_with_String_Test extends PathsBaseTest {
  /**
   * AssertionInfo
   */
  private static final AssertionInfo INFO = someInfo();
  private static final String TXT_EXTENSION_PATTERN = "regex:.+\\.txt";
  private static final String TXT_EXTENSION_PATTERN_DESCRIPTION = format("the '%s' pattern",
                                                                         TXT_EXTENSION_PATTERN);

  /**
   * Class containing tests that pass
   */
  @TestInstance(PER_CLASS)
  @Nested
  /* default */ class ActualMatches {
    /**
     * AssertionInfo
     */
    private final AssertionInfo INFO = someInfo();

    private ActualMatches() {}

    @ParameterizedTest
    @ValueSource(strings = { "regex:.+oo2\\.data", "regex:.+\\.json", "regex:.+bar2\\.json" })
    void should_pass_if_actual_contains_one_file_matching_the_given_pathMatcherPattern(final String pattern) throws IOException {
      // GIVEN
      createDefaultFixturePaths();
      // WHEN-THEN
      paths.assertIsDirectoryRecursivelyContaining(INFO, tempDir, pattern);
    }

    @ParameterizedTest
    @ValueSource(strings = { "regex:.+\\.data", "regex:.+foobar.*", "regex:.+root.+foo.*" })
    void should_pass_if_actual_contains_some_paths_matching_the_given_pathMatcherPattern(final String pattern) throws IOException {
      // GIVEN
      createDefaultFixturePaths();
      // WHEN-THEN
      paths.assertIsDirectoryRecursivelyContaining(INFO, tempDir, pattern);
    }

    private void createDefaultFixturePaths() throws IOException {
      // @format:off
      // The layout :
      // root
      // |—— foo
      // |    |—— foobar
      // |         |—— foobar1.data
      // |         |—— foobar2.json
      // |—— foo2.data
      // @format:on
      final Path rootDirPath = tempDir.resolve("root");
      final Path rootDir = Files.createDirectory(rootDirPath);
      createFile(createFoo2DataPath(rootDir));
      final Path fooDir = Files.createDirectory(createFooPath(rootDir));
      final Path foobar = Files.createDirectory(createFooBarPath(fooDir));
      createFile(createFooBar1Path(foobar));
      createFile(createFooBar2Path(foobar));
    }

    private Path createFoo2DataPath(final Path rootDir) {
      return rootDir.resolve("foo2.data");
    }

    private Path createFooPath(final Path dir) {
      return dir.resolve("foo");
    }

    private Path createFoo3Path(final Path dir) {
      return dir.resolve("foo3");
    }

    private Path createFooBarPath(final Path fooDir) {
      return fooDir.resolve("foobar");
    }

    private Path createFooBar1Path(final Path foobar) {
      return foobar.resolve("foobar1.data");
    }

    private Path createFooBar2Path(final Path foobar) {
      return foobar.resolve("foobar2.json");
    }

    @Test
    void should_pass_if_all_actual_paths_matching_the_given_pathMatcherPattern() throws IOException {
      // @format:off
      // The layout :
      // tempDir
      // |—— foo
      // |    |—— foo3
      // |—— foo2.data
      // @format:on/*
      // GIVEN
      final Path fooDir = Files.createDirectory(createFooPath(tempDir));
      Files.createDirectory(createFoo3Path(fooDir));
      createFile(createFoo2DataPath(tempDir));
      // WHEN-THEN
      paths.assertIsDirectoryRecursivelyContaining(INFO, tempDir, "regex:.*foo.*|.*tmp");
    }
  }

  @Test
  void should_fail_if_actual_does_not_exist() {
    // GIVEN
    final Path notExistingPath = tempDir.resolve("non-existent-file");
    // WHEN
    final AssertionError error = expectAssertionError(() -> paths.assertIsDirectoryRecursivelyContaining(INFO, notExistingPath,
                                                                                                         TXT_EXTENSION_PATTERN));
    // THEN
    final ErrorMessageFactory message = shouldExist(notExistingPath);
    final String text = createMessage(message);
    makeThrowableAssert(then(error), text);
  }

  @Test
  void should_fail_if_actual_exists_but_is_not_a_directory() throws IOException {
    // GIVEN
    final Path rootDirPath = tempDir.resolve("root");
    final Path rootDir = Files.createDirectory(rootDirPath);
    final Path existingPath = createFile(createFoo2DataPath(rootDir));
    // WHEN
    final AssertionError error = expectAssertionError(() -> paths.assertIsDirectoryRecursivelyContaining(INFO, existingPath,
                                                                                                         TXT_EXTENSION_PATTERN));
    // THEN
    final ErrorMessageFactory message = shouldBeDirectory(existingPath);
    final String text = createMessage(message);
    makeThrowableAssert(then(error), text);
  }

  @Test
  void should_fail_if_actual_is_empty() {
    // WHEN
    final AssertionError error = expectAssertionError(() -> paths.assertIsDirectoryRecursivelyContaining(INFO, tempDir,
                                                                                                         TXT_EXTENSION_PATTERN));
    // THEN
    final ErrorMessageFactory message = directoryShouldContainRecursively(tempDir, emptyList(),
                                                                          TXT_EXTENSION_PATTERN_DESCRIPTION);
    final String text = createMessage(message);
    makeThrowableAssert(then(error), text);
  }

  @Test
  void should_fail_if_actual_does_not_contain_any_paths_matching_the_given_pathMatcherPattern() throws IOException {
    // GIVEN
    final Path fooDir = Files.createDirectory(createFooPath(tempDir));
    final Path foo3Dir = Files.createDirectory(createFoo3Path(fooDir));
    final Path foo2data = createFile(createFoo2DataPath(tempDir));
    // WHEN
    final AssertionError error = expectAssertionError(() -> paths.assertIsDirectoryRecursivelyContaining(INFO, tempDir,
                                                                                                         TXT_EXTENSION_PATTERN));
    // THEN
    final List<Path> fooDirContent = list(fooDir, foo2data, foo3Dir);
    final ErrorMessageFactory message = directoryShouldContainRecursively(tempDir, fooDirContent,
                                                                          TXT_EXTENSION_PATTERN_DESCRIPTION);
    final String text = createMessage(message);
    makeThrowableAssert(then(error), text);
  }

  private String createMessage(final ErrorMessageFactory error) {
    return error.create();
  }

  private ThrowableAssert makeThrowableAssert(final AbstractThrowableAssert throwableAssert, final String text) {
    return (ThrowableAssert) throwableAssert.hasMessage(text);
  }

  private Path createFoo2DataPath(final Path rootDir) {
    return rootDir.resolve("foo2.data");
  }

  private Path createFooPath(final Path dir) {
    return dir.resolve("foo");
  }

  private Path createFoo3Path(final Path dir) {
    return dir.resolve("foo3");
  }
}
