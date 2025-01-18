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
package org.assertj.core.internal.strings;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.internal.Strings;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.assertj.core.error.ShouldNotContainSubsequenceOfCharSequence.shouldNotContainSubsequence;
import static org.assertj.core.internal.ErrorMessages.arrayOfValuesToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.arrayOfValuesToLookForIsNull;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

/**
 * Tests for {@link Strings#assertDoesNotContainSubsequence(AssertionInfo, CharSequence, CharSequence[])}.
 */
class Strings_assertDoesNotContainSubsequence_Test extends StringsBaseTest {

  @Test
  void should_pass_if_actual_does_not_contain_all_subsequence_items() {
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
    String[] subsequence = { "A Game of ", "Shadows" };

    strings.assertDoesNotContainSubsequence(someInfo(), actual, subsequence);
  }

  @Test
  void should_pass_if_actual_contains_all_subsequence_items_but_some_of_them_overlap_with_some_others() {
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
    String[] subsequence = { "A Game", "Game of Thrones", "George Martin" };

    strings.assertDoesNotContainSubsequence(someInfo(), actual, subsequence);
  }

  @Test
  void should_pass_if_actual_contains_all_subsequence_items_but_not_in_the_correct_order() {
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
    String[] subsequence = { "George Martin", "A Game of Thrones" };

    strings.assertDoesNotContainSubsequence(someInfo(), actual, subsequence);
  }

  @Test
  void should_pass_if_actual_is_empty_and_subsequence_contains_non_empty_strings() {
    String actual = "";
    String[] subsequence = { "A", "Game", "of", "Thrones" };

    strings.assertDoesNotContainSubsequence(someInfo(), actual, subsequence);
  }

  @Test
  void should_fail_if_actual_contains_subsequence_without_characters_between() {
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
    String[] subsequence = { "A Game of ", "Thrones" };

    AssertionError assertionError = expectAssertionError(() -> strings.assertDoesNotContainSubsequence(someInfo(), actual,
                                                                                                       subsequence));
    then(assertionError).hasMessage(shouldNotContainSubsequence(actual, subsequence, new int[] { 11, 21 }).create());
  }

  @Test
  void should_fail_if_actual_contains_subsequence_without_characters_between_according_to_the_custom_strategy() {
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
    String[] subsequence = { "A GAME OF ", "THRONES" };

    AssertionError assertionError = expectAssertionError(() -> stringsWithCaseInsensitiveComparisonStrategy.assertDoesNotContainSubsequence(someInfo(),
                                                                                                                                            actual,
                                                                                                                                            subsequence));
    then(assertionError).hasMessage(shouldNotContainSubsequence(actual, subsequence, new int[] { 11, 21 },
                                                                comparisonStrategy).create());
  }

  @Test
  void should_fail_if_actual_contains_subsequence_with_characters_between() {
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
    String[] subsequence = { "{", "A Game of ", "George Martin", "}" };

    AssertionError assertionError = expectAssertionError(() -> strings.assertDoesNotContainSubsequence(someInfo(), actual,
                                                                                                       subsequence));
    then(assertionError).hasMessage(shouldNotContainSubsequence(actual, subsequence, new int[] { 0, 11, 41, 55 }).create());
  }

  @Test
  void should_fail_if_actual_contains_subsequence_with_characters_between_according_to_the_custom_strategy() {
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
    String[] subsequence = { "{", "A GAME OF ", "GEORGE MARTIN", "}" };

    AssertionError assertionError = expectAssertionError(() -> stringsWithCaseInsensitiveComparisonStrategy.assertDoesNotContainSubsequence(someInfo(),
                                                                                                                                            actual,
                                                                                                                                            subsequence));
    then(assertionError).hasMessage(shouldNotContainSubsequence(actual, subsequence, new int[] { 0, 11, 41, 55 },
                                                                comparisonStrategy).create());
  }

  @Test
  void should_fail_if_actual_contains_subsequence_and_subsequence_has_empty_strings() {
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
    String[] subsequence = { "", "", "A Game of ", "", "George Martin", "" };

    AssertionError assertionError = expectAssertionError(() -> strings.assertDoesNotContainSubsequence(someInfo(), actual,
                                                                                                       subsequence));
    then(assertionError).hasMessage(shouldNotContainSubsequence(actual, subsequence,
                                                                new int[] { 0, 0, 11, 21, 41, 54 }).create());
  }

  @Test
  void should_fail_if_actual_is_not_empty_and_subsequence_contains_only_empty_strings() {
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
    String[] subsequence = { "", "", "" };

    AssertionError assertionError = expectAssertionError(() -> strings.assertDoesNotContainSubsequence(someInfo(), actual,
                                                                                                       subsequence));
    then(assertionError).hasMessage(shouldNotContainSubsequence(actual, subsequence, new int[] { 0, 0, 0 }).create());
  }

  @Test
  void should_fail_if_actual_is_empty_and_subsequence_contains_only_empty_strings() {
    String actual = "";
    String[] subsequence = { "", "" };

    AssertionError assertionError = expectAssertionError(() -> strings.assertDoesNotContainSubsequence(someInfo(), actual,
                                                                                                       subsequence));
    then(assertionError).hasMessage(shouldNotContainSubsequence(actual, subsequence, new int[] { 0, 0 }).create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    String actual = null;
    String[] subsequence = { "A Game of Thrones" };

    AssertionError assertionError = expectAssertionError(() -> strings.assertDoesNotContainSubsequence(someInfo(), actual,
                                                                                                       subsequence));
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_throw_error_if_subsequence_is_null() {
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
    String[] subsequence = null;

    ThrowingCallable assertionCall = () -> strings.assertDoesNotContainSubsequence(someInfo(), actual, subsequence);
    thenThrownBy(assertionCall).isInstanceOf(NullPointerException.class)
                               .hasMessage(arrayOfValuesToLookForIsNull());
  }

  @Test
  void should_throw_error_if_subsequence_is_empty() {
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
    String[] subsequence = {};

    ThrowingCallable assertionCall = () -> strings.assertDoesNotContainSubsequence(someInfo(), actual, subsequence);
    thenThrownBy(assertionCall).isInstanceOf(IllegalArgumentException.class)
                               .hasMessage(arrayOfValuesToLookForIsEmpty());
  }

  @Test
  void should_throw_error_if_subsequence_has_null_item() {
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
    String[] subsequence = { "A Game of ", null, "Thrones" };

    ThrowingCallable assertionCall = () -> strings.assertDoesNotContainSubsequence(someInfo(), actual, subsequence);
    thenThrownBy(assertionCall).isInstanceOf(NullPointerException.class)
                               .hasMessage("Expecting CharSequence elements not to be null but found one at index 1");
  }
}
