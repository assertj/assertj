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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.internal.paths;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static org.assertj.core.error.ShouldBeDirectory.shouldBeDirectory;
import static org.assertj.core.error.ShouldContainRecursively.directoryShouldContainRecursively;
import static org.assertj.core.error.ShouldExist.shouldExist;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.Lists.list;
import static org.mockito.Mockito.verify;

import java.nio.file.Path;
import java.util.List;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Paths;
import org.assertj.core.internal.PathsSimpleBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for <code>{@link Paths#assertIsDirectoryRecursivelyContaining(AssertionInfo, Path, String)}</code>
 *
 * @author David Haccoun
 */
class Paths_assertIsDirectoryRecursivelyContaining_SyntaxAndPattern_Test extends PathsSimpleBaseTest {

  private static final String TXT_EXTENSION_PATTERN = "regex:.+\\.txt";
  private static final String TXT_EXTENSION_PATTERN_DESCRIPTION = format("the '%s' pattern",
                                                                         TXT_EXTENSION_PATTERN);

  @ParameterizedTest
  @ValueSource(strings = { "regex:.+oo2\\.data", "regex:.+\\.json", "regex:.+bar2\\.json" })
  void should_pass_if_actual_contains_one_file_matching_the_given_pathMatcherPattern(String pattern) {
    // GIVEN
    createDefaultFixturePaths();
    // WHEN-THEN
    paths.assertIsDirectoryRecursivelyContaining(INFO, tempDir, pattern);
  }

  @ParameterizedTest
  @ValueSource(strings = { "regex:.+\\.data", "regex:.+foobar.*", "regex:.+root.+foo.*" })
  void should_pass_if_actual_contains_some_paths_matching_the_given_pathMatcherPattern(String pattern) {
    // GIVEN
    createDefaultFixturePaths();
    // WHEN-THEN
    paths.assertIsDirectoryRecursivelyContaining(INFO, tempDir, pattern);
  }

  private void createDefaultFixturePaths() {
    // @format:off
    // The layout :
    // root
    // |—— foo
    // |    |—— foobar
    // |         |—— foobar1.data
    // |         |—— foobar2.json
    // |—— foo2.data
    // @format:on
    Path rootDir = createDirectoryWithDefaultParent("root", "foo2.data");
    Path fooDir = createDirectory(rootDir, "foo");
    createDirectory(fooDir, "foobar", "foobar1.data", "foobar2.json");
  }

  @Test
  void should_pass_if_all_actual_paths_matching_the_given_pathMatcherPattern() {
    // GIVEN
    Path fooDir = createDirectory(tempDir, "foo", "foo2.data");
    createDirectory(fooDir, "foo3");
    // WHEN-THEN
    paths.assertIsDirectoryRecursivelyContaining(INFO, tempDir, "regex:.*foo.*|.*tmp");
  }

  @Test
  void should_fail_if_actual_does_not_exist() {
    // GIVEN
    Path notExistingPath = tempDir.resolve("doesnt-exist-file");
    // WHEN
    expectAssertionError(() -> paths.assertIsDirectoryRecursivelyContaining(INFO, notExistingPath, TXT_EXTENSION_PATTERN));
    // THEN
    verify(failures).failure(INFO, shouldExist(notExistingPath));
  }

  @Test
  void should_fail_if_actual_exists_but_is_not_a_directory() {
    // GIVEN
    Path rootDir = createDirectoryWithDefaultParent("root", "foo2.data");
    Path existingPath = rootDir.resolve("foo2.data");
    // WHEN
    expectAssertionError(() -> paths.assertIsDirectoryRecursivelyContaining(INFO, existingPath, TXT_EXTENSION_PATTERN));
    // THEN
    verify(failures).failure(INFO, shouldBeDirectory(existingPath));
  }

  @Test
  void should_fail_if_actual_is_empty() {
    // WHEN
    expectAssertionError(() -> paths.assertIsDirectoryRecursivelyContaining(INFO, tempDir, TXT_EXTENSION_PATTERN));
    // THEN
    verify(failures).failure(INFO, directoryShouldContainRecursively(tempDir, emptyList(), TXT_EXTENSION_PATTERN_DESCRIPTION));
  }

  @Test
  void should_fail_if_actual_does_not_contain_any_paths_matching_the_given_pathMatcherPattern() {
    // GIVEN
    Path fooDir = createDirectory(tempDir, "foo", "foo2.data");
    createDirectory(fooDir, "foo3");
    // WHEN
    expectAssertionError(() -> paths.assertIsDirectoryRecursivelyContaining(INFO, tempDir, TXT_EXTENSION_PATTERN));
    // THEN
    List<Path> fooDirContent = list(fooDir, fooDir.resolve("foo2.data"), fooDir.resolve("foo3"));
    verify(failures).failure(INFO, directoryShouldContainRecursively(tempDir, fooDirContent, TXT_EXTENSION_PATTERN_DESCRIPTION));
  }

}
