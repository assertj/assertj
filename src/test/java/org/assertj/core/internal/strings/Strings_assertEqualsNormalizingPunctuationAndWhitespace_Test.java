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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.internal.strings;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldBeEqualNormalizingPunctuation.shouldBeEqualNormalizingPunctuation;
import static org.assertj.core.test.TestData.someInfo;
import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link org.assertj.core.internal.Strings#assertEqualsNormalizingPunctuationAndWhitespace(org.assertj.core.api.AssertionInfo, CharSequence, CharSequence)} </code>.
 *
 * @author Harisha Talanki
 */
public class Strings_assertEqualsNormalizingPunctuationAndWhitespace_Test extends StringsBaseTest {

  @Test
  void should_fail_if_expected_string_is_empty() {
    // GIVEN
    String actual = "{Game} of} Thrones{)";
    String expected = "";

    AssertionInfo info = someInfo();
    // WHEN/THEN
    Throwable error = catchThrowable(() -> strings.assertEqualsNormalizingPunctuationAndWhitespace(info, actual, expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verifyFailureThrownWhenStringsAreNotEqualNormalizingPunctuationAndWhitespace(info, actual, expected);
  }

  @Test
  void should_fail_if_actual_string_contain_punctuation_instead_of_space_comparing_the_same_string_with_spaces() {
    // GIVEN
    String actual = "{Game:of:Thrones}";
    String expected = "Game of Thrones";

    AssertionInfo info = someInfo();
    // WHEN/THEN
    Throwable error = catchThrowable(() -> strings.assertEqualsNormalizingPunctuationAndWhitespace(info, actual, expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verifyFailureThrownWhenStringsAreNotEqualNormalizingPunctuationAndWhitespace(info, actual, expected);
  }

  @ParameterizedTest
  @MethodSource("equalNormalizingPunctuationAndWhitespaceGenerator")
  public void should_pass_if_both_Strings_are_equal_normalizing_punctuation(String actual, String expected) {
    strings.assertEqualsNormalizingPunctuationAndWhitespace(someInfo(), actual, expected);
  }

  public static Stream<Arguments> equalNormalizingPunctuationAndWhitespaceGenerator() {
    return Stream.of(Arguments.of("Game of Thrones {})()!' 98402938409283904230948", "Game of Thrones 98402938409283904230948"), // strings containing numbers
      Arguments.of("Game of    {})()!' Thrones", "Game of Thrones"),
      Arguments.of("{(Game)-(of)-(Thrones)!!!}", "GameofThrones"), // strings with no whitespaces but contains punctuations
      Arguments.of("(Game of Thrones)", "Game of Thrones"),
      Arguments.of("{Game} of} Thrones{)", "Game of Thrones"),
      Arguments.of("{Game} of} Thrones{)", "Game   of   Thrones"),
      Arguments.of("{}(}'{)!!!!!,.;", ""), // strings with only punctuation
      Arguments.of("Game of Thrones{})()!' season1-8", "Game of Thrones season1-8"), // strings containing characters not part of punctuation
      Arguments.of(null, null), // null
      Arguments.of(" \t \t", " "), // comparing tabs and spaces
      Arguments.of("Game of Thrones", "Game of Thrones"), // without punctuation and no spaces
      Arguments.of("Game {} of () Thrones {})()!'", "Game  of  Thrones ")); // comparing strings with punctuation and additional spaces
  }

  private void verifyFailureThrownWhenStringsAreNotEqualNormalizingPunctuationAndWhitespace(AssertionInfo info, String actual,
                                                                                            String expected) {
    verify(failures).failure(info, shouldBeEqualNormalizingPunctuation(actual, expected), actual, expected);
  }
}
