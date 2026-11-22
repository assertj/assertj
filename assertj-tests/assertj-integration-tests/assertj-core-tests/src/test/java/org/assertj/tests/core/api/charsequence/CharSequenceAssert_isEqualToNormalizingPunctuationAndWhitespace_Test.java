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

import static java.util.stream.Stream.concat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeEqualNormalizingPunctuationAndWhitespace.shouldBeEqualNormalizingPunctuationAndWhitespace;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/// @author Harisha Talanki
class CharSequenceAssert_isEqualToNormalizingPunctuationAndWhitespace_Test {

  private static final Set<Character> NON_BREAKING_SPACES = Set.of(' ', ' ', ' ');

  @Test
  void should_fail_if_expected_string_is_empty() {
    // GIVEN
    String actual = "{Game} of} Thrones{)";
    String expected = "";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isEqualToNormalizingPunctuationAndWhitespace(expected));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualNormalizingPunctuationAndWhitespace(actual, expected).create());
  }

  @Test
  void should_fail_if_actual_string_contain_punctuation_instead_of_space_comparing_the_same_string_with_spaces() {
    // GIVEN
    String actual = "{Game:of:Thrones}";
    String expected = "Game of Thrones";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isEqualToNormalizingPunctuationAndWhitespace(expected));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualNormalizingPunctuationAndWhitespace(actual, expected).create());
  }

  @ParameterizedTest
  @MethodSource
  void should_pass_if_both_Strings_are_equal_normalizing_punctuation_and_whitespace(String actual, String expected) {
    assertThat(actual).isEqualToNormalizingPunctuationAndWhitespace(expected);
  }

  static Stream<Arguments> should_pass_if_both_Strings_are_equal_normalizing_punctuation_and_whitespace() {
    Stream<Arguments> punctuationAndWhitespaces = Stream.of(// strings containing numbers
                                                            arguments("Game of Thrones {})()!' 98402938409283904230948",
                                                                      "Game of Thrones 98402938409283904230948"),
                                                            arguments("Game of    {})()!' Thrones", "Game of Thrones"),
                                                            // strings with no whitespaces but contains punctuations
                                                            arguments("{(Game)-(of)-(Thrones)!!!}", "GameofThrones"),
                                                            arguments("(Game of Thrones)", "Game of Thrones"),
                                                            arguments("{Game} of} Thrones{)", "Game of Thrones"),
                                                            arguments("{Game} of} Thrones{)", "Game   of   Thrones"),
                                                            // strings with only punctuation
                                                            arguments("{}(}'{)!!!!!,.;", ""),
                                                            // strings containing characters not part of punctuation
                                                            arguments("Game of Thrones{})()!' season1-8",
                                                                      "Game of Thrones season1-8"),
                                                            arguments(null, null),
                                                            // comparing tabs and spaces
                                                            arguments(" \t \t", " "),
                                                            // without punctuation and no spaces
                                                            arguments("Game of Thrones", "Game of Thrones"),
                                                            // comparing strings with punctuation and additional spaces
                                                            arguments("Game {} of () Thrones {})()!'", "Game  of  Thrones "));
    Stream<Arguments> nonBreakingSpaces = NON_BREAKING_SPACES.stream()
                                                             .map(nonBreakingSpace -> arguments("my" + nonBreakingSpace
                                                                                                + "foo bar",
                                                                                                "my foo bar"));
    return concat(punctuationAndWhitespaces, nonBreakingSpaces);
  }

}
