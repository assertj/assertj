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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.internal.paths;

import static java.nio.file.Files.createFile;
import static java.nio.file.Files.createSymbolicLink;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldExist.shouldExistNoFollowLinks;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.io.IOException;
import java.nio.file.Path;

import org.assertj.core.internal.PathsBaseTest;
import org.junit.jupiter.api.Test;

class Paths_assertExistsNoFollowLinks_Test extends PathsBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertExistsNoFollowLinks(INFO, null));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_exist() {
    // GIVEN
    Path actual = tempDir.resolve("non-existent");
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertExistsNoFollowLinks(INFO, actual));
    // THEN
    then(error).hasMessage(shouldExistNoFollowLinks(actual).create());
  }

  @Test
  void should_pass_if_actual_exists() throws IOException {
    // GIVEN
    Path actual = createFile(tempDir.resolve("actual"));
    // WHEN/THEN
    underTest.assertExistsNoFollowLinks(INFO, actual);
  }

  @Test
  void should_pass_if_actual_is_a_symbolic_link_and_target_exists() throws IOException {
    // GIVEN
    Path target = createFile(tempDir.resolve("target"));
    Path actual = createSymbolicLink(tempDir.resolve("actual"), target);
    // WHEN/THEN
    underTest.assertExistsNoFollowLinks(INFO, actual);
  }

  @Test
  void should_pass_if_actual_is_a_symbolic_link_and_target_does_not_exist() throws IOException {
    // GIVEN
    Path target = tempDir.resolve("non-existent");
    Path actual = createSymbolicLink(tempDir.resolve("actual"), target);
    // WHEN/THEN
    underTest.assertExistsNoFollowLinks(INFO, actual);
  }

}
