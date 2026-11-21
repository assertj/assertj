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
package org.assertj.tests.core.api.path;

import static java.nio.file.Files.createFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveNoParent.shouldHaveNoParent;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.testkit.PathFactory.tryToCreateSymbolicLink;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class PathAssert_hasNoParentRaw_Test {

  @TempDir
  Path tempDir;

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Path actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasNoParentRaw());
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_has_parent() throws IOException {
    // GIVEN
    Path actual = createFile(tempDir.resolve("actual"));
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasNoParentRaw());
    // THEN
    then(assertionError).hasMessage(shouldHaveNoParent(actual).create());
  }

  @Test
  void should_pass_if_actual_has_no_parent() {
    // GIVEN
    Path actual = tempDir.getRoot();
    // WHEN/THEN
    then(actual).hasNoParentRaw();
  }

  @Test
  void should_fail_if_actual_is_not_canonical() {
    // GIVEN
    Path root = tempDir.getRoot();
    Path actual = tryToCreateSymbolicLink(tempDir.resolve("actual"), root);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasNoParentRaw());
    // THEN
    then(assertionError).hasMessage(shouldHaveNoParent(actual).create());
  }

}
