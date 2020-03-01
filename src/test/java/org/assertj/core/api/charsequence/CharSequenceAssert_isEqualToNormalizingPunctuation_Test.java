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
package org.assertj.core.api.charsequence;

import org.assertj.core.api.CharSequenceAssert;
import org.assertj.core.api.CharSequenceAssertBaseTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

/**
 * Created by harisha talanki on 2/29/20
 */
public class CharSequenceAssert_isEqualToNormalizingPunctuation_Test extends CharSequenceAssertBaseTest {

  @Override
  protected CharSequenceAssert invoke_api_method() {
    return assertions.isEqualToNormalizingPunctuation("Game of Thrones");
  }

  @Override
  protected void verify_internal_effects() {
    verify(strings).assertEqualsNormalizingWPunctuation(getInfo(assertions), getActual(assertions), "Game of Thrones");
  }

  @Test
  void should_fail_if_expected_string_is_empty() {
    // GIVEN
    String actual = "{Game} of} Thrones{)";
    String expected = "";
    // WHEN/THEN
    expectAssertionError(() -> {
      assertThat(actual).isEqualToNormalizingPunctuation(expected);
    });
  }


  @Test
  void should_fail_if_actual_string_contain_punctuation_instead_of_space_comparing_the_same_string_with_spaces() {
    // GIVEN
    String actual = "{Game:of:Thrones}";
    String expected = "Game of Thrones";
    // WHEN/THEN
    expectAssertionError(() -> {
      assertThat(actual).isEqualToNormalizingPunctuation(expected);
    });
  }

  @Test
  void should_fail_if_actual_string_contain_punctuation_and_numbers_comparing_with_just_string() {
    // GIVEN
    String actual = "Game of Thrones {})()!' 98402938409283904230948";
    String expected = "Game of Thrones";
    // WHEN/THEN
    expectAssertionError(() -> {
      assertThat(actual).isEqualToNormalizingPunctuation(expected);
    });
  }

  @Test
  void should_pass_if_actual_string_contain_punctuation_separated_by_spaces_comparing_with_just_string_with_one_space() {
    // GIVEN
    String actual = "Game of    {})()!' Thrones";
    String expected = "Game of Thrones";
    // WHEN/THEN
    assertThat(actual).isEqualToNormalizingPunctuation(expected);
  }

  @Test
  void should_pass_if_actual_has_no_spaces_with_lot_of_punctuation_and_expected_has_no_spaces() {
    // GIVEN
    String actual = "{(Game)-(of)-(Thrones)!!!}";
    String expected = "GameofThrones";
    // WHEN/THEN
    assertThat(actual).isEqualToNormalizingPunctuation(expected);
  }

  @Test
  void should_pass_if_strings_contain_parenthesis() {
    // GIVEN
    String actual = "(Game of Thrones)";
    String expected = "Game of Thrones";
    // WHEN/THEN
    assertThat(actual).as("Not ignoring parenthesis").
      isEqualToNormalizingPunctuation(expected);
  }

  @Test
  void should_pass_if_strings_contain_flower_braces() {
    // GIVEN
    String actual = "{Game} of} Thrones{)";
    String expected = "Game of Thrones";
    // WHEN/THEN
    assertThat(actual).as("Not ignoring flower braces").
      isEqualToNormalizingPunctuation(expected);
  }

  @Test
  void should_pass_even_if_expected_string_has_an_extra_space() {
    // GIVEN
    String actual = "{Game} of} Thrones{)";
    String expected = "Game   of   Thrones";
    // WHEN/THEN
    assertThat(actual).isEqualToNormalizingPunctuation(expected);
  }

  @Test
  void should_pass_if_strings_contain_only_punctuation_and_comparing_with_empty_string() {
    // GIVEN
    String actual = "{}(}'{)!!!!!,.;";
    String expected = "";
    // WHEN/THEN
    assertThat(actual).isEqualToNormalizingPunctuation(expected);
  }

  @Test
  void should_pass_if_strings_contains_digits_along_with_punctuation_and_comparing_with_string_without_punctuation() {
    // GIVEN
    String actual = "Game of Thrones{})()!' season1-8";
    String expected = "Game of Thrones season1-8";
    // WHEN/THEN
    assertThat(actual).isEqualToNormalizingPunctuation(expected);
  }

  @Test
  void should_pass_if_actual_string_contain_punctuation_separated_by_spaces_with_string_with_correct_number_of_spaces() {
    // GIVEN
    String actual = "Game {} of () Thrones {})()!'";
    String expected = "Game  of  Thrones ";
    // WHEN/THEN
    assertThat(actual).isEqualToNormalizingPunctuation(expected);
  }
}
