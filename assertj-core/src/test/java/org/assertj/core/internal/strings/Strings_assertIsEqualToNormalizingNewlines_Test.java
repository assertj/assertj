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
package org.assertj.core.internal.strings;

import static org.assertj.core.error.ShouldBeEqualIgnoringNewLineDifferences.shouldBeEqualIgnoringNewLineDifferences;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import java.util.stream.Stream;

import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for
 * <code>{@link org.assertj.core.internal.Strings#assertIsEqualToNormalizingNewlines(org.assertj.core.api.AssertionInfo, CharSequence, CharSequence)}</code>
 * .
 *
 * @author Mauricio Aniche
 */
class Strings_assertIsEqualToNormalizingNewlines_Test extends StringsBaseTest {

  @ParameterizedTest
  @MethodSource
  void should_pass_if_both_strings_are_equals_after_normalizing_newlines(String actual, String expected) {
    strings.assertEqualsIgnoringWhitespace(someInfo(), actual, expected);
  }

  public static Stream<Arguments> should_pass_if_both_strings_are_equals_after_normalizing_newlines() {
    return Stream.of(Arguments.of("Lord of the Rings\r\nis a classic", "Lord of the Rings\nis a classic"),
                     Arguments.of("Lord of the Rings\nis a classic", "Lord of the Rings\r\nis a classic"),
                     Arguments.of("Lord of the Rings\nis a classic", "Lord of the Rings\nis a classic"),
                     Arguments.of("Lord of the Rings\r\nis a classic", "Lord of the Rings\r\nis a classic"),
                     Arguments.of("With \n several \r\n new lines", "With \n several \r\n new lines"),
                     Arguments.of("With \n several \r\n new lines", "With \r\n several \r\n new lines"),
                     Arguments.of("With \n several \r\n new lines", "With \r\n several \n new lines"),
                     Arguments.of("With \n several \r\n new lines", "With \n several \n new lines"),
                     Arguments.of("With \r\n several \r\n new lines", "With \n several \n new lines"),
                     Arguments.of("With \r\n several \r\n new lines", "With \r\n several \r\n new lines"),
                     Arguments.of("\n", "\r\n"),
                     Arguments.of("\r\n", "\n"),
                     Arguments.of("\r\n", "\r\n"),
                     Arguments.of("\n", "\n"),
                     Arguments.of(null, null));
  }

  @Test
  void should_fail_if_newlines_are_different_in_both_strings() {
    // GIVEN
    String actual = "Lord of the Rings\r\n\r\nis cool";
    String expected = "Lord of the Rings\nis cool";
    // WHEN
    expectAssertionError(() -> strings.assertIsEqualToNormalizingNewlines(someInfo(), actual, expected));
    // THEN
    verify(failures).failure(someInfo(), shouldBeEqualIgnoringNewLineDifferences(actual, expected),
                             "Lord of the Rings\n\nis cool", expected);
  }

  @Test
  void should_fail_if_actual_is_null_and_expected_is_not() {
    // GIVEN
    String actual = null;
    String expected = "";
    // WHEN
    expectAssertionError(() -> strings.assertIsEqualToNormalizingNewlines(someInfo(), actual, expected));
    // THEN
    verify(failures).failure(someInfo(), shouldBeEqualIgnoringNewLineDifferences(null, expected), null, expected);
  }

  @Test
  void should_fail_if_actual_is_not_null_but_expected_is_null() {
    // GIVEN
    String actual = "";
    String expected = null;
    // WHEN
    expectAssertionError(() -> strings.assertIsEqualToNormalizingNewlines(someInfo(), actual, expected));
    // THEN
    verify(failures).failure(someInfo(), shouldBeEqualIgnoringNewLineDifferences(actual, null), actual, null);
  }
}
