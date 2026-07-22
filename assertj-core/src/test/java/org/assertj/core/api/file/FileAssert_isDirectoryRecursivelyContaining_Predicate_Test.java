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
package org.assertj.core.api.file;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.Files.newFile;
import static org.assertj.core.util.Files.newFolder;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.util.function.Predicate;

import org.assertj.core.api.FileAssert;
import org.assertj.core.api.FileAssertBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for <code>{@link FileAssert#isDirectoryRecursivelyContaining(Predicate)}</code>
 *
 * @author David Haccoun
 */
class FileAssert_isDirectoryRecursivelyContaining_Predicate_Test extends FileAssertBaseTest {

  private final Predicate<File> anyFilter = path -> true;
  @TempDir
  private File tempDir;

  @Override
  protected FileAssert invoke_api_method() {
    return assertions.isDirectoryRecursivelyContaining(anyFilter);
  }

  @Override
  protected void verify_internal_effects() {
    verify(files).assertIsDirectoryRecursivelyContaining(getInfo(assertions), getActual(assertions), anyFilter);
  }

  @Test
  void should_pass_if_the_file_matching_the_given_pattern_is_in_a_sub_directory_of_actual() {
    // GIVEN
    File subDirectory = newFolder(tempDir.getAbsolutePath() + "/dir");
    newFile(subDirectory.getAbsolutePath() + "/test.txt");
    newFile(tempDir.getAbsolutePath() + "/test.java");
    File docs = newFolder(tempDir.getAbsolutePath() + "/docs");
    newFolder(docs + "/music");
    File images = newFolder(docs + "/images");
    newFile(images + "/sun.png");
    // WHEN/THEN
    then(tempDir).isDirectoryRecursivelyContaining(f -> f.getName().endsWith(".txt"))
                 .isDirectoryRecursivelyContaining(f -> f.getName().equals("dir"))
                 .isDirectoryRecursivelyContaining(f -> f.getName().endsWith("java"))
                 .isDirectoryRecursivelyContaining(f -> f.getName().endsWith(".png"))
                 .isDirectoryRecursivelyContaining(f -> f.getName().startsWith("mus"));
  }

  @Test
  void error_message_should_list_actual_directory_sorted_relative_content() {
    // GIVEN
    File dir1 = newFolder(tempDir.getAbsolutePath() + "/directory1");
    newFolder(tempDir.getAbsolutePath() + "/directory2");
    newFile(tempDir.getAbsolutePath() + "/file1.txt");
    newFile(tempDir.getAbsolutePath() + "/file2.txt");
    newFile(dir1.getAbsolutePath() + "/file3.txt");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(tempDir).isDirectoryRecursivelyContaining(file -> false));
    // THEN
    then(assertionError).hasMessageContainingAll("The directory content was:",
                                                 "[directory1, directory1/file3.txt, directory2, file1.txt, file2.txt]".replace('/',
                                                                                                                                File.separatorChar));
  }

  @Test
  void should_fail_if_actual_does_not_contain_any_files_matching_the_given_predicate() {
    // GIVEN
    File fooDir = newFolder(tempDir.getAbsolutePath() + "/foo");
    newFile(fooDir.getAbsolutePath() + "/foo2.txt");
    newFolder(tempDir.getAbsolutePath() + "/foo3");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(tempDir).isDirectoryRecursivelyContaining(f -> f.getName()
                                                                                                                          .equals("foo2")));
    // THEN
    then(assertionError).hasMessageContainingAll("The directory content was:",
                                                 "[foo, foo/foo2.txt, foo3]".replace('/', File.separatorChar));
  }

}
