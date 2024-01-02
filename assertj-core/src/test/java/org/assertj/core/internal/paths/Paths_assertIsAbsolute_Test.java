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

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeAbsolutePath.shouldBeAbsolutePath;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.assertj.core.internal.PathsBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class Paths_assertIsAbsolute_Test extends PathsBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertIsAbsolute(INFO, null));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @ParameterizedTest
  @MethodSource("nonAbsolutePaths")
  void should_fail_if_actual_is_not_absolute() {
    // GIVEN
    Path actual = Paths.get("relative");
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertIsAbsolute(INFO, actual));
    // THEN
    then(error).hasMessage(shouldBeAbsolutePath(actual).create());
  }

  private static Stream<Path> nonAbsolutePaths() {
    return Stream.of(Paths.get("foo"),
                     Paths.get("foo", "bar"));
  }

  @Test
  void should_pass_if_actual_is_absolute() {
    // GIVEN
    Path actual = tempDir.getRoot().resolve("foo").resolve("bar");
    // WHEN/THEN
    underTest.assertIsAbsolute(INFO, actual);
  }

}
