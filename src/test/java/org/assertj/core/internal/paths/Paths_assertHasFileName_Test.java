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

import static java.nio.file.Files.createDirectory;
import static java.nio.file.Files.createFile;
import static java.nio.file.Files.createSymbolicLink;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.io.IOException;
import java.nio.file.Path;

import org.assertj.core.internal.PathsBaseTest;
import org.junit.jupiter.api.Test;

class Paths_assertHasFileName_Test extends PathsBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    AssertionError error = expectAssertionError(() -> paths.assertHasFileName(info, null, "actual"));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_fileName_is_null() {
    // GIVEN
    Path actual = tempDir.resolve("actual");
    // WHEN
    Throwable thrown = catchThrowable(() -> paths.assertHasFileName(info, actual, null));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage("expected fileName should not be null");
  }

  @Test
  void should_pass_if_non_existing_actual_has_given_fileName() {
    // GIVEN
    Path actual = tempDir.resolve("actual");
    // WHEN/THEN
    paths.assertHasFileName(info, actual, "actual");
  }

  @Test
  void should_pass_if_existing_actual_file_has_given_fileName() throws IOException {
    // GIVEN
    Path actual = createFile(tempDir.resolve("actual"));
    // WHEN/THEN
    paths.assertHasFileName(info, actual, "actual");
  }

  @Test
  void should_pass_if_existing_actual_directory_has_given_fileName() throws IOException {
    // GIVEN
    Path actual = createDirectory(tempDir.resolve("actual"));
    // WHEN/THEN
    paths.assertHasFileName(info, actual, "actual");
  }

  @Test
  void should_pass_if_existing_actual_symbolic_link_has_given_fileName() throws IOException {
    // GIVEN
    Path actual = createSymbolicLink(tempDir.resolve("actual"), tempDir);
    // WHEN/THEN
    paths.assertHasFileName(info, actual, "actual");
  }

}
