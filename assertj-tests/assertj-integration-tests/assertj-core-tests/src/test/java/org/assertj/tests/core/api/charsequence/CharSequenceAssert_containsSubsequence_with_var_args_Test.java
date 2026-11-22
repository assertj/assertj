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
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContainCharSequence.shouldContain;
import static org.assertj.core.error.ShouldContainSubsequenceOfCharSequence.shouldContainSubsequence;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.internal.ErrorMessages.arrayOfValuesToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.arrayOfValuesToLookForIsNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.assertj.tests.core.testkit.Maps.mapOf;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.tests.core.testkit.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.Test;

/// @author Filip Hrisafov
class CharSequenceAssert_containsSubsequence_with_var_args_Test {

  private static final ComparatorBasedComparisonStrategy CASE_INSENSITIVE_COMPARISON = new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.INSTANCE);

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsSubsequence("a", "b"));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_error_if_subsequence_is_null() {
    // GIVEN
    CharSequence[] subsequence = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat("Yoda").containsSubsequence(subsequence));
    // THEN
    then(exception).hasMessage(arrayOfValuesToLookForIsNull());
  }

  @Test
  void should_throw_error_if_any_value_of_subsequence_is_null() {
    // GIVEN
    String actual = "'author':'George Martin'";
    String[] subsequence = { "author", null };
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(actual).containsSubsequence(subsequence));
    // THEN
    then(exception).hasMessage("Expecting CharSequence elements not to be null but found one at index 1");
  }

  @Test
  void should_throw_error_if_subsequence_values_is_empty() {
    // GIVEN
    String[] emptySubsequence = {};
    // WHEN
    var exception = catchIllegalArgumentException(() -> assertThat("Yoda").containsSubsequence(emptySubsequence));
    // THEN
    then(exception).hasMessage(arrayOfValuesToLookForIsEmpty());
  }

  @Test
  void should_pass_if_actual_contains_subsequence() {
    assertThat("Yoda").containsSubsequence("Yo", "da");
  }

  @Test
  void should_pass_if_actual_contains_subsequence_with_values_between() {
    // GIVEN
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
    // WHEN/THEN
    then(actual).containsSubsequence("{", "title", "A Game of Thrones", "}");
  }

  @Test
  void should_pass_if_actual_contains_subsequence_that_specifies_multiple_times_the_same_value_bug_544() {
    assertThat("a-b-c-").containsSubsequence("a", "-", "b", "-", "c");
    assertThat("{ 'title':'A Game of Thrones', 'author':'George Martin'}").containsSubsequence("George", " ", "Martin");
  }

  @Test
  void should_fail_if_actual_does_not_contain_all_given_strings() {
    // GIVEN
    String actual = "Yoda";
    String[] subsequence = { "Yo", "da", "Han" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsSubsequence(subsequence));
    // THEN
    then(assertionError).hasMessage(shouldContain(actual, subsequence, newLinkedHashSet("Han")).create());
  }

  @Test
  void should_fail_if_actual_contains_values_but_not_in_given_order() {
    // GIVEN
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
    String[] subsequence = { "{", "author", "A Game of Thrones", "}" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsSubsequence(subsequence));
    // THEN
    then(assertionError).hasMessage(shouldContainSubsequence(actual, subsequence, 1).create());
  }

  @Test
  void should_fail_when_actual_does_not_contain_non_existing_elements_and_missing_subsequence_duplicates() {
    // GIVEN
    String actual = "v1 : v2 : v3 : v2";
    String[] subsequence = { "v2", "v2", "v2", "v3", "v4", "v5" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsSubsequence(subsequence));
    // THEN
    then(assertionError).hasMessage(shouldContain(actual, subsequence, newLinkedHashSet("v4", "v5")).create());
  }

  @Test
  void should_pass_if_actual_contains_subsequence_according_to_custom_comparison_strategy() {
    assertThat("Yoda").usingComparator(CaseInsensitiveStringComparator.INSTANCE).containsSubsequence("Yo", "da");
    assertThat("Yoda").usingComparator(CaseInsensitiveStringComparator.INSTANCE).containsSubsequence("Yo", "DA");
    assertThat("Yoda").usingComparator(CaseInsensitiveStringComparator.INSTANCE).containsSubsequence("YO", "dA");
  }

  @Test
  void should_fail_if_actual_does_not_contain_subsequence_according_to_custom_comparison_strategy() {
    // GIVEN
    String actual = "Yoda";
    String[] subsequence = { "Yo", "da", "Han" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                      .containsSubsequence(subsequence));
    // THEN
    then(assertionError).hasMessage(shouldContain(actual, subsequence, newLinkedHashSet("Han"),
                                                  CASE_INSENSITIVE_COMPARISON).create());
  }

  @Test
  void should_fail_if_actual_contains_values_but_not_in_given_order_according_to_custom_comparison_strategy() {
    // GIVEN
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
    String[] subsequence = { "{", "author", "A Game of Thrones", "}" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                      .containsSubsequence(subsequence));
    // THEN
    then(assertionError).hasMessage(shouldContainSubsequence(actual, subsequence, 1, CASE_INSENSITIVE_COMPARISON).create());
  }

  @Test
  void should_fail_if_actual_does_not_contain_all_occurrences_of_subsequence_values() {
    // GIVEN
    String actual = "v1 : v2 : v3";
    String[] subsequence = { "v2", "v2", "v3" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                      .containsSubsequence(subsequence));
    // THEN
    then(assertionError).hasMessage(shouldContainSubsequence(actual, subsequence, mapOf(entry("v2", 1)),
                                                             CASE_INSENSITIVE_COMPARISON).create());
  }

  @Test
  void should_fail_if_actual_does_not_contain_multiple_repeated_occurrences_of_subsequence_values() {
    // GIVEN
    String actual = "v1 : v2 : v2 : v3";
    String[] subsequence = { "v2", "v2", "v2", "v3" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                      .containsSubsequence(subsequence));
    // THEN
    then(assertionError).hasMessage(shouldContainSubsequence(actual, subsequence, mapOf(entry("v2", 2)),
                                                             CASE_INSENSITIVE_COMPARISON).create());
  }

  @Test
  void should_fail_if_actual_does_not_contain_repeated_occurrences_of_subsequence_values() {
    // GIVEN
    String actual = "Yoda";
    String[] subsequence = { "Yo", "da", "da" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                      .containsSubsequence(subsequence));
    // THEN
    then(assertionError).hasMessage(shouldContainSubsequence(actual, subsequence, mapOf(entry("da", 1)),
                                                             CASE_INSENSITIVE_COMPARISON).create());
  }

  @Test
  void should_fail_if_actual_does_not_contain_repeated_occurrences_of_subsequence_values_in_text_with_multiple_values() {
    // GIVEN
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
    String[] subsequence = { "George", "George" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                      .containsSubsequence(subsequence));
    // THEN
    then(assertionError).hasMessage(shouldContainSubsequence(actual, subsequence, mapOf(entry("George", 1)),
                                                             CASE_INSENSITIVE_COMPARISON).create());
  }

}
