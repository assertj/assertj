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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.internal.strings;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.ShouldNotContainAnyWhitespaces.shouldNotContainAnyWhitespaces;
import static org.assertj.core.test.TestData.someInfo;

import java.util.stream.Stream;

import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class Strings_assertDoesNotContainAnyWhitespaces_Test extends StringsBaseTest {

  public static Stream<String> doesNotContainAnyWhitespaces() {
    return Stream.of(null,
                     "",
                     "a",
                     "bc");
  }

  @ParameterizedTest
  @MethodSource("doesNotContainAnyWhitespaces")
  void should_pass_if_string_does_not_contain_any_whitespaces(String actual) {
    strings.assertDoesNotContainAnyWhitespaces(someInfo(), actual);
  }

  public static Stream<String> containsWithspaces() {
    return Stream.of("a ",
                     "a b",
                     "a  b",
                     "a\u005Ctb", // tab
                     "a\u005Cnb", // line feed
                     "a\u005Crb", // carriage return
                     "a \u005Cn\u005Cr  b");
  }

  @ParameterizedTest
  @MethodSource("containsWithspaces")
  void should_fail_if_string_contains_whitespaces(String actual) {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertDoesNotContainAnyWhitespaces(someInfo(),
                                                                                                                actual))
                                                   .withMessage(shouldNotContainAnyWhitespaces(actual).create());
  }
}
