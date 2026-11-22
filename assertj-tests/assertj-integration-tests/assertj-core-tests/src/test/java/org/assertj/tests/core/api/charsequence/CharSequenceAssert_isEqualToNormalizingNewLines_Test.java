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
package org.assertj.tests.core.api.charsequence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeEqualIgnoringNewLineDifferences.shouldBeEqualIgnoringNewLineDifferences;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/// @author Mauricio Aniche
/// @author Sára Juhošová
class CharSequenceAssert_isEqualToNormalizingNewLines_Test {

  @ParameterizedTest
  @MethodSource
  void should_pass_if_both_strings_are_equals_after_normalizing_newlines(String actual, String expected) {
    assertThat(actual).isEqualToNormalizingNewlines(expected);
  }

  static Stream<Arguments> should_pass_if_both_strings_are_equals_after_normalizing_newlines() {
    return Stream.of(arguments("Lord of the Rings\r\nis a classic", "Lord of the Rings\nis a classic"),
                     arguments("Lord of the Rings\nis a classic", "Lord of the Rings\r\nis a classic"),
                     arguments("Lord of the Rings\nis a classic", "Lord of the Rings\nis a classic"),
                     arguments("Lord of the Rings\r\nis a classic", "Lord of the Rings\r\nis a classic"),
                     arguments("With \n several \r\n new lines", "With \n several \r\n new lines"),
                     arguments("With \n several \r\n new lines", "With \r\n several \r\n new lines"),
                     arguments("With \n several \r\n new lines", "With \r\n several \n new lines"),
                     arguments("With \n several \r\n new lines", "With \n several \n new lines"),
                     arguments("With \r\n several \r\n new lines", "With \n several \n new lines"),
                     arguments("With \r\n several \r\n new lines", "With \r\n several \r\n new lines"),
                     arguments("\n", "\r\n"),
                     arguments("\r\n", "\n"),
                     arguments("\r\n", "\r\n"),
                     arguments("\n", "\n"),
                     arguments(null, null));
  }

  @Test
  void should_fail_if_newlines_are_different_in_both_strings() {
    // GIVEN
    String actual = "Lord of the Rings\r\n\r\nis cool";
    String expected = "Lord of the Rings\nis cool";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isEqualToNormalizingNewlines(expected));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualIgnoringNewLineDifferences(actual, expected).create());
  }

  @Test
  void should_fail_if_actual_is_null_and_expected_is_not() {
    // GIVEN
    String actual = null;
    String expected = "";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isEqualToNormalizingNewlines(expected));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualIgnoringNewLineDifferences(null, expected).create());
  }

  @Test
  void should_fail_if_actual_is_not_null_but_expected_is_null() {
    // GIVEN
    String actual = "";
    String expected = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isEqualToNormalizingNewlines(expected));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualIgnoringNewLineDifferences(actual, null).create());
  }

}
