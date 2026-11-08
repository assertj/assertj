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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeHidden.shouldBeHidden;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.api.path.PathFactory.createUnixPaths;
import static org.assertj.tests.core.api.path.PathFactory.createWindowsPaths;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.junit.jupiter.api.condition.OS.WINDOWS;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class PathAssert_isHidden_Test {

  @TempDir
  static Path tempDir;

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Path actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isHidden());
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @DisabledOnOs(WINDOWS)
  @ParameterizedTest
  @MethodSource
  void should_fail_if_actual_is_not_hidden_on_Unix(Path actual) {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isHidden());
    // THEN
    then(assertionError).hasMessage(shouldBeHidden(actual).create());
  }

  static List<Path> should_fail_if_actual_is_not_hidden_on_Unix() throws IOException {
    return createUnixPaths(tempDir, false);
  }

  @EnabledOnOs(WINDOWS)
  @ParameterizedTest
  @MethodSource
  void should_fail_if_actual_is_not_hidden_on_Windows(Path actual) {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isHidden());
    // THEN
    then(assertionError).hasMessage(shouldBeHidden(actual).create());
  }

  static List<Path> should_fail_if_actual_is_not_hidden_on_Windows() throws IOException {
    return createWindowsPaths(tempDir, false);
  }

  @DisabledOnOs(WINDOWS)
  @ParameterizedTest
  @MethodSource
  void should_pass_if_actual_is_hidden_on_Unix(Path actual) {
    // WHEN/THEN
    assertThat(actual).isHidden();
  }

  static List<Path> should_pass_if_actual_is_hidden_on_Unix() throws IOException {
    return createUnixPaths(tempDir, true);
  }

  @EnabledOnOs(WINDOWS)
  @ParameterizedTest
  @MethodSource
  void should_pass_if_actual_is_hidden_on_Windows(Path actual) {
    // WHEN/THEN
    assertThat(actual).isHidden();
  }

  static List<Path> should_pass_if_actual_is_hidden_on_Windows() throws IOException {
    return createWindowsPaths(tempDir, true);
  }

}
