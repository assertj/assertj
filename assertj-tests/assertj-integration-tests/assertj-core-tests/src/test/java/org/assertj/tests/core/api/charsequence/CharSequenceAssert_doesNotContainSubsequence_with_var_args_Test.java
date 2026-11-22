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
import static org.assertj.core.api.Assertions.catchIllegalArgumentException;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.ShouldNotContainSubsequenceOfCharSequence.shouldNotContainSubsequence;
import static org.assertj.core.internal.ErrorMessages.arrayOfValuesToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.arrayOfValuesToLookForIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.tests.core.testkit.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.Test;

class CharSequenceAssert_doesNotContainSubsequence_with_var_args_Test {

  private static final String ACTUAL = "{ 'title':'A Game of Thrones', 'author':'George Martin' }";
  private static final ComparatorBasedComparisonStrategy CASE_INSENSITIVE_COMPARISON = new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.INSTANCE);

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).doesNotContainSubsequence("A Game of Thrones"));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_error_if_subsequence_is_null() {
    // GIVEN
    String[] subsequence = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(ACTUAL).doesNotContainSubsequence(subsequence));
    // THEN
    then(exception).hasMessage(arrayOfValuesToLookForIsNull());
  }

  @Test
  void should_throw_error_if_subsequence_is_empty() {
    // GIVEN
    String[] subsequence = {};
    // WHEN
    var exception = catchIllegalArgumentException(() -> assertThat(ACTUAL).doesNotContainSubsequence(subsequence));
    // THEN
    then(exception).hasMessage(arrayOfValuesToLookForIsEmpty());
  }

  @Test
  void should_throw_error_if_subsequence_has_null_item() {
    // GIVEN
    String[] subsequence = { "A Game of ", null, "Thrones" };
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(ACTUAL).doesNotContainSubsequence(subsequence));
    // THEN
    then(exception).hasMessage("Expecting CharSequence elements not to be null but found one at index 1");
  }

  @Test
  void should_pass_if_actual_does_not_contain_all_subsequence_items() {
    assertThat(ACTUAL).doesNotContainSubsequence("A Game of ", "Shadows");
  }

  @Test
  void should_pass_if_actual_contains_all_subsequence_items_but_some_of_them_overlap_with_some_others() {
    assertThat(ACTUAL).doesNotContainSubsequence("A Game", "Game of Thrones", "George Martin");
  }

  @Test
  void should_pass_if_actual_contains_all_subsequence_items_but_not_in_the_correct_order() {
    assertThat(ACTUAL).doesNotContainSubsequence("George Martin", "A Game of Thrones");
  }

  @Test
  void should_pass_if_actual_is_empty_and_subsequence_contains_non_empty_strings() {
    assertThat("").doesNotContainSubsequence("A", "Game", "of", "Thrones");
  }

  @Test
  void should_fail_if_actual_contains_subsequence_without_characters_between() {
    // GIVEN
    String[] subsequence = { "A Game of ", "Thrones" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).doesNotContainSubsequence(subsequence));
    // THEN
    then(assertionError).hasMessage(shouldNotContainSubsequence(ACTUAL, subsequence, new int[] { 11, 21 }).create());
  }

  @Test
  void should_fail_if_actual_contains_subsequence_without_characters_between_according_to_the_custom_strategy() {
    // GIVEN
    String[] subsequence = { "A GAME OF ", "THRONES" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                      .doesNotContainSubsequence(subsequence));
    // THEN
    then(assertionError).hasMessage(shouldNotContainSubsequence(ACTUAL, subsequence, new int[] { 11, 21 },
                                                                CASE_INSENSITIVE_COMPARISON).create());
  }

  @Test
  void should_fail_if_actual_contains_subsequence_with_characters_between() {
    // GIVEN
    String[] subsequence = { "{", "A Game of ", "George Martin", "}" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).doesNotContainSubsequence(subsequence));
    // THEN
    then(assertionError).hasMessage(shouldNotContainSubsequence(ACTUAL, subsequence, new int[] { 0, 11, 41, 56 }).create());
  }

  @Test
  void should_fail_if_actual_contains_subsequence_with_characters_between_according_to_the_custom_strategy() {
    // GIVEN
    String[] subsequence = { "{", "A GAME OF ", "GEORGE MARTIN", "}" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                      .doesNotContainSubsequence(subsequence));
    // THEN
    then(assertionError).hasMessage(shouldNotContainSubsequence(ACTUAL, subsequence, new int[] { 0, 11, 41, 56 },
                                                                CASE_INSENSITIVE_COMPARISON).create());
  }

  @Test
  void should_fail_if_actual_contains_subsequence_and_subsequence_has_empty_strings() {
    // GIVEN
    String[] subsequence = { "", "", "A Game of ", "", "George Martin", "" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).doesNotContainSubsequence(subsequence));
    // THEN
    then(assertionError).hasMessage(shouldNotContainSubsequence(ACTUAL, subsequence,
                                                                new int[] { 0, 0, 11, 21, 41, 54 }).create());
  }

  @Test
  void should_fail_if_actual_is_not_empty_and_subsequence_contains_only_empty_strings() {
    // GIVEN
    String[] subsequence = { "", "", "" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).doesNotContainSubsequence(subsequence));
    // THEN
    then(assertionError).hasMessage(shouldNotContainSubsequence(ACTUAL, subsequence, new int[] { 0, 0, 0 }).create());
  }

  @Test
  void should_fail_if_actual_is_empty_and_subsequence_contains_only_empty_strings() {
    // GIVEN
    String actual = "";
    String[] subsequence = { "", "" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).doesNotContainSubsequence(subsequence));
    // THEN
    then(assertionError).hasMessage(shouldNotContainSubsequence(actual, subsequence, new int[] { 0, 0 }).create());
  }

}
