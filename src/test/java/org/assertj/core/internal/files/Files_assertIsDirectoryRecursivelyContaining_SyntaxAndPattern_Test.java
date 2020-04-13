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

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Files;
import org.assertj.core.internal.FilesSimpleBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static java.lang.String.format;
import static org.assertj.core.error.ShouldBeDirectory.shouldBeDirectory;
import static org.assertj.core.error.ShouldContain.directoryShouldContain;
import static org.assertj.core.internal.Files.toAbsolutePaths;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.Lists.emptyList;
import static org.assertj.core.util.Lists.list;
import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link Files#assertIsDirectoryRecursivelyContaining(AssertionInfo, File, String)}</code>
 *
 * @author David Haccoun
 */
public class Files_assertIsDirectoryRecursivelyContaining_SyntaxAndPattern_Test extends FilesSimpleBaseTest {

  private static final String TXT_EXTENSION_PATTERN = "regex:.+\\.txt";
  private static final String TXT_EXTENSION_PATTERN_DESCRIPTION = format("the '%s' pattern",
                                                                         TXT_EXTENSION_PATTERN);

  @ParameterizedTest
  @ValueSource(strings = { "regex:.+oo2\\.data", "regex:.+\\.json", "regex:.+bar2\\.json" })
  void should_pass_if_actual_contains_one_file_matching_the_given_pathMatcherPattern(String pattern) {
    // GIVEN
    createDefaultFixturePaths();
    // WHEN-THEN
    files.assertIsDirectoryRecursivelyContaining(INFO, tempDirAsFile, pattern);
  }

  @ParameterizedTest
  @ValueSource(strings = { "regex:.+\\.data", "regex:.+foobar.*", "regex:.+root.+foo.*" })
  void should_pass_if_actual_contains_some_files_matching_the_given_pathMatcherPattern(String pattern) {
    // GIVEN
    createDefaultFixturePaths();
    // WHEN-THEN
    files.assertIsDirectoryRecursivelyContaining(INFO, tempDirAsFile, pattern);
  }

  private void createDefaultFixturePaths() {
    // @format:off
    // The layout :
    //  root
    //  |-- foo
    //  |    |-- foobar
    //  |         |-- foobar1.data
    //  |         |-- foobar2.json
    //  |-- foo2.data
    // @format:on
    Path rootDir = createDirectoryWithDefaultParent("root", "foo2.data");
    Path fooDir = createDirectory(rootDir, "foo");
    createDirectory(fooDir, "foobar", "foobar1.data", "foobar2.json");
  }

  @Test
  void should_pass_if_all_actual_files_matching_the_given_pathMatcherPattern() {
    // GIVEN
    Path fooDir = createDirectory(tempDir, "foo", "foo2.data");
    createDirectory(fooDir, "foo3");
    // WHEN-THEN
    files.assertIsDirectoryRecursivelyContaining(INFO, tempDirAsFile, "regex:.*foo.*|.*tmp");
  }

  @Test
  void should_fail_if_actual_does_not_exist() {
    // GIVEN
    File notExistingFile = new File("foo/bar/doesnt-exist-file");
    // WHEN
    expectAssertionError(() -> files.assertIsDirectoryRecursivelyContaining(INFO, notExistingFile,
                                                                            TXT_EXTENSION_PATTERN));
    // THEN
    verify(failures).failure(INFO, shouldBeDirectory(notExistingFile));
  }

  @Test
  void should_fail_if_actual_exists_but_is_not_a_directory() throws IOException {
    // GIVEN
    File existingFile = java.nio.file.Files.createFile(tempDir.resolve("FooFile.txt")).toFile();
    // WHEN
    expectAssertionError(
        () -> files.assertIsDirectoryRecursivelyContaining(INFO, existingFile, TXT_EXTENSION_PATTERN));
    // THEN
    verify(failures).failure(INFO, shouldBeDirectory(existingFile));
  }

  @Test
  void should_fail_if_actual_is_empty() {
    // WHEN
    expectAssertionError(
        () -> files.assertIsDirectoryRecursivelyContaining(INFO, tempDirAsFile, TXT_EXTENSION_PATTERN));
    // THEN
    verify(failures)
        .failure(INFO, directoryShouldContain(tempDirAsFile, emptyList(), TXT_EXTENSION_PATTERN_DESCRIPTION));
  }

  @Test
  void should_fail_if_actual_does_not_contain_any_files_matching_the_given_pathMatcherPattern() {
    // GIVEN
    Path fooDir = createDirectory(tempDir, "foo", "foo2.data");
    createDirectory(fooDir, "foo3");
    // WHEN
    expectAssertionError(
        () -> files.assertIsDirectoryRecursivelyContaining(INFO, tempDirAsFile, TXT_EXTENSION_PATTERN));
    // THEN
    verify(failures)
        .failure(INFO,
                 directoryShouldContain(tempDirAsFile, toAbsolutePaths(list(new File(tempDirAsFile, "foo"),
                                                                            new File(tempDirAsFile,
                                                                                     "foo/foo2.data"),
                                                                            new File(tempDirAsFile, "foo/foo3")
                                        )),
                                        TXT_EXTENSION_PATTERN_DESCRIPTION));
  }

}
