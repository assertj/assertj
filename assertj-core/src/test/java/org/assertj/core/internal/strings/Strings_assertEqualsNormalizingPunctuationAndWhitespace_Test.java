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

import static org.assertj.core.error.ShouldBeEqualNormalizingPunctuationAndWhitespace.shouldBeEqualNormalizingPunctuationAndWhitespace;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import java.util.stream.Stream;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for <code>{@link org.assertj.core.internal.Strings#normalizePunctuationAndWhitespace(org.assertj.core.api.AssertionInfo, CharSequence, CharSequence)} </code>.
 *
 * @author Harisha Talanki
 */
class Strings_assertEqualsNormalizingPunctuationAndWhitespace_Test extends StringsBaseTest {

  @Test
  void should_fail_if_expected_string_is_empty() {
    // GIVEN
    String actual = "{Game} of} Thrones{)";
    String expected = "";
    AssertionInfo info = someInfo();
    // WHEN
    expectAssertionError(() -> strings.assertEqualsNormalizingPunctuationAndWhitespace(info, "{Game} of} Thrones{)", expected));
    // THEN
    verify(failures).failure(info, shouldBeEqualNormalizingPunctuationAndWhitespace(actual, expected), "Game of Thrones",
                             expected);
  }

  @Test
  void should_fail_if_actual_string_contain_punctuation_instead_of_space_comparing_the_same_string_with_spaces() {
    // GIVEN
    String actual = "{Game:of:Thrones}";
    String expected = "Game of Thrones";
    AssertionInfo info = someInfo();
    // WHEN
    expectAssertionError(() -> strings.assertEqualsNormalizingPunctuationAndWhitespace(info, actual, expected));
    // THEN
    verify(failures).failure(info, shouldBeEqualNormalizingPunctuationAndWhitespace(actual, expected), "GameofThrones", expected);
  }

  @ParameterizedTest
  @MethodSource("equalNormalizingPunctuationAndWhitespaceGenerator")
  void should_pass_if_both_Strings_are_equal_normalizing_punctuation_and_whitespace(String actual, String expected) {
    strings.assertEqualsNormalizingPunctuationAndWhitespace(someInfo(), actual, expected);
  }

  public static Stream<Arguments> equalNormalizingPunctuationAndWhitespaceGenerator() {
    return Stream.of(// strings containing numbers
                     Arguments.of("Game of Thrones {})()!' 98402938409283904230948", "Game of Thrones 98402938409283904230948"),
                     Arguments.of("Game of    {})()!' Thrones", "Game of Thrones"),
                     // strings with no whitespaces but contains punctuations
                     Arguments.of("{(Game)-(of)-(Thrones)!!!}", "GameofThrones"),
                     Arguments.of("(Game of Thrones)", "Game of Thrones"),
                     Arguments.of("{Game} of} Thrones{)", "Game of Thrones"),
                     Arguments.of("{Game} of} Thrones{)", "Game   of   Thrones"),
                     // strings with only punctuation
                     Arguments.of("{}(}'{)!!!!!,.;", ""),
                     // strings containing characters not part of punctuation
                     Arguments.of("Game of Thrones{})()!' season1-8", "Game of Thrones season1-8"),
                     Arguments.of(null, null),
                     // comparing tabs and spaces
                     Arguments.of(" \t \t", " "),
                     // without punctuation and no spaces
                     Arguments.of("Game of Thrones", "Game of Thrones"),
                     // comparing strings with punctuation and additional spaces
                     Arguments.of("Game {} of () Thrones {})()!'", "Game  of  Thrones "));
  }

}
