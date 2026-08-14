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

import org.assertj.core.api.FileAssert;
import org.assertj.core.api.FileAssertBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for <code>{@link FileAssert#isDirectoryRecursivelyContaining(String)}</code>
 *
 * @author David Haccoun
 */
class FileAssert_isDirectoryRecursivelyContaining_SyntaxAndPattern_Test extends FileAssertBaseTest {

  private final String syntaxAndPattern = "glob:*.java";
  @TempDir
  private File tempDir;

  @Override
  protected FileAssert invoke_api_method() {
    return assertions.isDirectoryRecursivelyContaining(syntaxAndPattern);
  }

  @Override
  protected void verify_internal_effects() {
    verify(files).assertIsDirectoryRecursivelyContaining(getInfo(assertions), getActual(assertions), syntaxAndPattern);
  }

  @Test
  void should_pass_if_the_file_matching_the_given_pattern_is_in_a_sub_directory_of_actual() {
    // GIVEN
    File dir = newFolder(tempDir.getAbsolutePath() + "/dir");
    newFile(dir.getAbsolutePath() + "/test.txt");
    newFile(tempDir.getAbsolutePath() + "/test.java");
    File docs = newFolder(tempDir.getAbsolutePath() + "/docs");
    newFolder(docs + "/music");
    File images = newFolder(docs + "/images");
    newFile(images + "/sun.png");
    // WHEN/THEN
    then(tempDir).isDirectoryRecursivelyContaining("glob:*.java")
                 .isDirectoryRecursivelyContaining("glob:**.txt")
                 .isDirectoryRecursivelyContaining("glob:**.png")
                 .isDirectoryRecursivelyContaining("glob:**mus*")
                 .isDirectoryRecursivelyContaining("glob:dir");
  }

  @Test
  void should_fail_if_the_file_matching_the_given_pattern_is_in_a_sub_directory_of_actual() {
    // GIVEN
    File subDirectory = newFolder(tempDir.getAbsolutePath() + "/dir");
    newFile(subDirectory.getAbsolutePath() + "/test.txt");
    newFile(tempDir.getAbsolutePath() + "/test.java");
    // WHEN glob:*.txt does not traverse directories thus dir/test.txt does not match glob:*.txt but matches glob:**.txt
    AssertionError assertionError = expectAssertionError(() -> assertThat(tempDir).isDirectoryRecursivelyContaining("glob:*.txt"));
    // THEN
    then(assertionError).hasMessageContainingAll("The directory content was:",
                                                 "[dir, dir/test.txt, test.java]".replace('/', File.separatorChar));
  }

}
