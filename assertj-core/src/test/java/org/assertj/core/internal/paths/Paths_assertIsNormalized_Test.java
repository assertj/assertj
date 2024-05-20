/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.internal.paths;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeNormalized.shouldBeNormalized;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.junit.jupiter.api.condition.OS.WINDOWS;

import java.nio.file.Path;

import org.assertj.core.internal.PathsBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class Paths_assertIsNormalized_Test extends PathsBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertIsNormalized(INFO, null));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @ParameterizedTest
  @DisabledOnOs(WINDOWS)
  @CsvSource({
      "/a/./b",
      "c/d/..",
      "/../../e",
  })
  void should_fail_on_unix_if_actual_is_not_normalized(Path actual) {
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertIsNormalized(INFO, actual));
    // THEN
    then(error).hasMessage(shouldBeNormalized(actual).create());
  }

  @ParameterizedTest
  @EnabledOnOs(WINDOWS)
  @CsvSource({
      "C:\\a\\.\\b",
      "c\\d\\..",
      "C:\\..\\..\\e",
  })
  void should_fail_on_windows_if_actual_is_not_normalized(Path actual) {
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.assertIsNormalized(INFO, actual));
    // THEN
    then(error).hasMessage(shouldBeNormalized(actual).create());
  }

  @ParameterizedTest
  @DisabledOnOs(WINDOWS)
  @CsvSource({
      "/usr/lib",
      "a/b/c",
      "../d",
  })
  void should_pass_on_unix_if_actual_is_normalized(Path actual) {
    // WHEN/THEN
    underTest.assertIsNormalized(INFO, actual);
  }

  @ParameterizedTest
  @EnabledOnOs(WINDOWS)
  @CsvSource({
      "C:\\usr\\lib",
      "a\\b\\c",
      "..\\d",
  })
  void should_pass_on_windows_if_actual_is_normalized(Path actual) {
    // WHEN/THEN
    underTest.assertIsNormalized(INFO, actual);
  }

}
