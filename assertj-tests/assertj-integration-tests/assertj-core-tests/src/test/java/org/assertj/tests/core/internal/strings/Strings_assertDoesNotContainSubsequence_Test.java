/*
 * Copyright 2012-2025 the original author or authors.
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
package org.assertj.tests.core.internal.strings;

import static org.assertj.core.api.Assertions.catchIllegalArgumentException;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotContainSubsequenceOfCharSequence.shouldNotContainSubsequence;
import static org.assertj.core.internal.ErrorMessages.arrayOfValuesToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.arrayOfValuesToLookForIsNull;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.testkit.TestData.someInfo;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Strings;
import org.assertj.tests.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Strings#assertDoesNotContainSubsequence(AssertionInfo, CharSequence, CharSequence[])}.
 */
class Strings_assertDoesNotContainSubsequence_Test extends StringsBaseTest {

  @Test
  void should_pass_if_actual_does_not_contain_all_subsequence_items() {
    // GIVEN
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin' }";
    String[] subsequence = { "A Game of ", "Shadows" };
    // WHEN/THEN
    strings.assertDoesNotContainSubsequence(someInfo(), actual, subsequence);
  }

  @Test
  void should_pass_if_actual_contains_all_subsequence_items_but_some_of_them_overlap_with_some_others() {
    // GIVEN
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin' }";
    String[] subsequence = { "A Game", "Game of Thrones", "George Martin" };
    // WHEN/THEN
    strings.assertDoesNotContainSubsequence(someInfo(), actual, subsequence);
  }

  @Test
  void should_pass_if_actual_contains_all_subsequence_items_but_not_in_the_correct_order() {
    // GIVEN
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin' }";
    String[] subsequence = { "George Martin", "A Game of Thrones" };
    // WHEN/THEN
    strings.assertDoesNotContainSubsequence(someInfo(), actual, subsequence);
  }

  @Test
  void should_pass_if_actual_is_empty_and_subsequence_contains_non_empty_strings() {
    // GIVEN
    String actual = "";
    String[] subsequence = { "A", "Game", "of", "Thrones" };
    // WHEN/THEN
    strings.assertDoesNotContainSubsequence(someInfo(), actual, subsequence);
  }

  @Test
  // GIVEN
  void should_fail_if_actual_contains_subsequence_without_characters_between() {
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin' }";
    String[] subsequence = { "A Game of ", "Thrones" };
    // WHEN
    var assertionError = expectAssertionError(() -> strings.assertDoesNotContainSubsequence(someInfo(), actual, subsequence));
    // THEN
    then(assertionError).hasMessage(shouldNotContainSubsequence(actual, subsequence, new int[] { 11, 21 }).create());
  }

  @Test
  void should_fail_if_actual_contains_subsequence_without_characters_between_according_to_the_custom_strategy() {
    // GIVEN
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin' }";
    String[] subsequence = { "A GAME OF ", "THRONES" };
    // WHEN
    var assertionError = expectAssertionError(() -> stringsWithCaseInsensitiveComparisonStrategy.assertDoesNotContainSubsequence(someInfo(),
                                                                                                                                 actual,
                                                                                                                                 // THEN
                                                                                                                                 subsequence));
    then(assertionError).hasMessage(shouldNotContainSubsequence(actual, subsequence, new int[] { 11, 21 },
                                                                comparisonStrategy).create());
  }

  @Test
  void should_fail_if_actual_contains_subsequence_with_characters_between() {
    // GIVEN
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin' }";
    String[] subsequence = { "{", "A Game of ", "George Martin", "}" };
    // WHEN
    var assertionError = expectAssertionError(() -> strings.assertDoesNotContainSubsequence(someInfo(), actual, subsequence));
    // THEN
    then(assertionError).hasMessage(shouldNotContainSubsequence(actual, subsequence, new int[] { 0, 11, 41, 56 }).create());
  }

  @Test
  void should_fail_if_actual_contains_subsequence_with_characters_between_according_to_the_custom_strategy() {
    // GIVEN
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin' }";
    String[] subsequence = { "{", "A GAME OF ", "GEORGE MARTIN", "}" };
    // WHEN
    var assertionError = expectAssertionError(() -> stringsWithCaseInsensitiveComparisonStrategy.assertDoesNotContainSubsequence(someInfo(),
                                                                                                                                 actual,
                                                                                                                                 subsequence));
    // THEN
    then(assertionError).hasMessage(shouldNotContainSubsequence(actual, subsequence, new int[] { 0, 11, 41, 56 },
                                                                comparisonStrategy).create());
  }

  @Test
  void should_fail_if_actual_contains_subsequence_and_subsequence_has_empty_strings() {
    // GIVEN
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin' }";
    String[] subsequence = { "", "", "A Game of ", "", "George Martin", "" };
    // WHEN
    var assertionError = expectAssertionError(() -> strings.assertDoesNotContainSubsequence(someInfo(), actual, subsequence));
    // THEN
    then(assertionError).hasMessage(shouldNotContainSubsequence(actual, subsequence,
                                                                new int[] { 0, 0, 11, 21, 41, 54 }).create());
  }

  @Test
  void should_fail_if_actual_is_not_empty_and_subsequence_contains_only_empty_strings() {
    // GIVEN
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin' }";
    String[] subsequence = { "", "", "" };
    // WHEN
    var assertionError = expectAssertionError(() -> strings.assertDoesNotContainSubsequence(someInfo(), actual, subsequence));
    // THEN
    then(assertionError).hasMessage(shouldNotContainSubsequence(actual, subsequence, new int[] { 0, 0, 0 }).create());
  }

  @Test
  void should_fail_if_actual_is_empty_and_subsequence_contains_only_empty_strings() {
    // GIVEN
    String actual = "";
    String[] subsequence = { "", "" };
    // WHEN
    var assertionError = expectAssertionError(() -> strings.assertDoesNotContainSubsequence(someInfo(), actual, subsequence));
    // THEN
    then(assertionError).hasMessage(shouldNotContainSubsequence(actual, subsequence, new int[] { 0, 0 }).create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String actual = null;
    String[] subsequence = { "A Game of Thrones" };
    // WHEN
    var assertionError = expectAssertionError(() -> strings.assertDoesNotContainSubsequence(someInfo(), actual, subsequence));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_throw_error_if_subsequence_is_null() {
    // GIVEN
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin' }";
    String[] subsequence = null;
    // WHEN
    var npe = catchNullPointerException(() -> strings.assertDoesNotContainSubsequence(INFO, actual, subsequence));
    // THEN
    then(npe).hasMessage(arrayOfValuesToLookForIsNull());
  }

  @Test
  void should_throw_error_if_subsequence_is_empty() {
    // GIVEN
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin' }";
    String[] subsequence = {};
    // WHEN
    var iae = catchIllegalArgumentException(() -> strings.assertDoesNotContainSubsequence(INFO, actual, subsequence));
    // THEN
    then(iae).hasMessage(arrayOfValuesToLookForIsEmpty());
  }

  @Test
  void should_throw_error_if_subsequence_has_null_item() {
    // GIVEN
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin' }";
    String[] subsequence = { "A Game of ", null, "Thrones" };
    // WHEN
    var npe = catchNullPointerException(() -> strings.assertDoesNotContainSubsequence(INFO, actual, subsequence));
    // THEN
    then(npe).hasMessage("Expecting CharSequence elements not to be null but found one at index 1");

  }
}
