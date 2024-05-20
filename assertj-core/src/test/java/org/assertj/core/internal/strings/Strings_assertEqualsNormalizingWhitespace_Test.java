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

import static java.lang.String.format;
import static java.util.stream.Stream.concat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.error.ShouldBeEqualNormalizingWhitespace.shouldBeEqualNormalizingWhitespace;
import static org.assertj.core.internal.ErrorMessages.charSequenceToLookForIsNull;
import static org.assertj.core.testkit.CharArrays.arrayOf;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Alexander Bischof
 * @author Dan Corder
 */
class Strings_assertEqualsNormalizingWhitespace_Test extends StringsBaseTest {

  @Test
  void should_fail_if_actual_is_null_and_expected_is_not() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertEqualsNormalizingWhitespace(someInfo(),
                                                                                                               null,
                                                                                                               "Luke"))
                                                   .withMessage(format(shouldBeEqualNormalizingWhitespace(null,
                                                                                                          "Luke").create()));
  }

  @Test
  void should_fail_if_actual_is_not_null_and_expected_is_null() {
    assertThatNullPointerException().isThrownBy(() -> strings.assertEqualsNormalizingWhitespace(someInfo(), "Luke",
                                                                                                null))
                                    .withMessage(charSequenceToLookForIsNull());
  }

  @Test
  void should_fail_if_both_Strings_are_not_equal_after_whitespace_is_normalized() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertEqualsNormalizingWhitespace(someInfo(),
                                                                                                               "Yoda",
                                                                                                               "Luke"))
                                                   .withMessage(format(shouldBeEqualNormalizingWhitespace("Yoda",
                                                                                                          "Luke").create()));
  }

  @ParameterizedTest
  @MethodSource("equalNormalizingWhitespaceGenerator")
  void should_pass_if_both_Strings_are_equal_after_whitespace_is_normalized(String actual, String expected) {
    strings.assertEqualsNormalizingWhitespace(someInfo(), actual, expected);
  }

  static Stream<Arguments> equalNormalizingWhitespaceGenerator() {
    Stream<Arguments> regularWhiteSpaces = Stream.of(arguments("my   foo bar", "my foo bar"),
                                                     arguments("  my foo bar  ", "my foo bar"),
                                                     arguments(" my\tfoo bar ", " my foo bar"),
                                                     arguments(" my foo    bar ", "my foo bar"),
                                                     arguments(" my foo    bar ", "  my foo bar   "),
                                                     arguments("       ", " "),
                                                     arguments(" my\tfoo bar ",
                                                               new String(arrayOf(' ', 'm', 'y', ' ', 'f', 'o', 'o', ' ', 'b',
                                                                                  'a', 'r'))),
                                                     arguments(" my\tfoo bar ", " my\tfoo bar "),   // same
                                                     arguments(null, null),   // null
                                                     arguments(" \t \t", " "),
                                                     arguments(" abc", "abc "));

    Stream<Arguments> nonBreakingSpaces = NON_BREAKING_SPACES.stream()
                                                             .map(nonBreakingSpace -> arguments("my" + nonBreakingSpace
                                                                                                + "foo bar", "my foo bar"));

    return concat(regularWhiteSpaces, nonBreakingSpaces);
  }

}
