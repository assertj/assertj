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
package org.assertj.core.api.path;

import static java.nio.file.Files.createDirectory;
import static java.nio.file.Files.createFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Predicate;

import org.assertj.core.api.PathAssert;
import org.assertj.core.api.PathAssertBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for <code>{@link PathAssert#isDirectoryContaining(Predicate)}</code>
 *
 * @author Valeriy Vyrva
 */
class PathAssert_isDirectoryContaining_Predicate_Test extends PathAssertBaseTest {

  @TempDir
  private Path tempDir;
  private final Predicate<Path> filter = path -> path.getParent() != null;

  @Override
  protected PathAssert invoke_api_method() {
    return assertions.isDirectoryContaining(filter);
  }

  @Override
  protected void verify_internal_effects() {
    verify(paths).assertIsDirectoryContaining(getInfo(assertions), getActual(assertions), filter);
  }

  @Test
  void error_message_should_list_actual_directory_sorted_content() throws IOException {
    // GIVEN
    createDirectory(tempDir.resolve("directory1"));
    createDirectory(tempDir.resolve("directory2"));
    createFile(tempDir.resolve("file1.txt"));
    createFile(tempDir.resolve("file2.txt"));
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(tempDir).isDirectoryContaining(x -> false));
    // THEN
    then(assertionError).hasMessageContainingAll("The directory content was:",
                                                 "[directory1, directory2, file1.txt, file2.txt]");
  }
}
