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
package org.assertj.core.internal.paths;

import static org.assertj.core.error.ShouldBeDirectory.shouldBeDirectory;
import static org.assertj.core.error.ShouldContainRecursively.directoryShouldContainRecursively;
import static org.assertj.core.error.ShouldExist.shouldExist;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.Lists.emptyList;
import static org.assertj.core.util.Lists.list;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.verify;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Paths;
import org.assertj.core.internal.PathsSimpleBaseTest;
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
public class Paths_assertIsDirectoryRecursivelyContaining_Predicate_Test extends PathsSimpleBaseTest {

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
    void should_pass_if_actual_contains_any_paths_matching_the_given_predicate(Predicate<Path> predicate) {
      paths.assertIsDirectoryRecursivelyContaining(INFO, tempDir, predicate);
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
  void should_fail_if_actual_does_not_exist() {
    // GIVEN
    Path notExistingPath = tempDir.resolve("doesnt-exist-file");
    Predicate<Path> anyPredicate = f -> true;
    // WHEN
    expectAssertionError(() -> paths.assertIsDirectoryRecursivelyContaining(INFO, notExistingPath, anyPredicate));
    // THEN
    verify(failures).failure(INFO, shouldExist(notExistingPath));
  }

  @Test
  void should_fail_if_actual_exists_but_is_not_a_directory() {
    // GIVEN
    Path rootDir = createDirectoryWithDefaultParent("root", "foo2.data");
    Path existingPath = rootDir.resolve("foo2.data");
    Predicate<Path> alwaysTrue = f -> true;
    // WHEN
    expectAssertionError(() -> paths.assertIsDirectoryRecursivelyContaining(INFO, existingPath, alwaysTrue));
    // THEN
    verify(failures).failure(INFO, shouldBeDirectory(existingPath));
  }

  @Test
  void should_fail_if_actual_is_empty() {
    // GIVEN
    Predicate<Path> alwaysTrue = f -> true;
    // WHEN
    expectAssertionError(() -> paths.assertIsDirectoryRecursivelyContaining(INFO, tempDir, alwaysTrue));
    // THEN
    verify(failures).failure(INFO, directoryShouldContainRecursively(tempDir, emptyList(), THE_GIVEN_FILTER_DESCRIPTION));
  }

  @Test
  void should_fail_if_actual_does_not_contain_any_paths_matching_the_given_predicate() {
    // GIVEN
    Path fooDir = createDirectory(tempDir, "foo", "foo2.data");
    createDirectory(fooDir, "foo3");
    Predicate<Path> alwaysFalse = f -> false;
    // WHEN
    expectAssertionError(() -> paths.assertIsDirectoryRecursivelyContaining(INFO, tempDir, alwaysFalse));
    // THEN
    List<Path> fooDirContent = list(fooDir, fooDir.resolve("foo2.data"), fooDir.resolve("foo3"));
    verify(failures).failure(INFO, directoryShouldContainRecursively(tempDir, fooDirContent, THE_GIVEN_FILTER_DESCRIPTION));
  }

}
