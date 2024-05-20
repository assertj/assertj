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

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContainCharSequence.shouldContain;
import static org.assertj.core.error.ShouldContainSubsequenceOfCharSequence.shouldContainSubsequence;
import static org.assertj.core.internal.ErrorMessages.arrayOfValuesToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.arrayOfValuesToLookForIsNull;
import static org.assertj.core.testkit.Maps.mapOf;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.WritableAssertionInfo;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;

class Strings_assertContainsSubsequence_Test extends StringsBaseTest {

  private static final WritableAssertionInfo INFO = someInfo();

  @Test
  void should_pass_if_actual_contains_subsequence() {
    strings.assertContainsSubsequence(INFO, "Yoda", array("Yo", "da"));
  }

  @Test
  void should_pass_if_actual_contains_subsequence_with_values_between() {
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
    String[] subsequence = { "{", "title", "A Game of Thrones", "}" };
    strings.assertContainsSubsequence(INFO, actual, subsequence);
  }

  @Test
  void should_fail_if_actual_does_not_contain_all_given_strings() {
    // GIVEN
    String actual = "Yoda";
    String[] subsequence = { "Yo", "da", "Han" };
    // WHEN
    expectAssertionError(() -> strings.assertContainsSubsequence(INFO, actual, subsequence));
    // THEN
    verify(failures).failure(INFO, shouldContain(actual, subsequence, newLinkedHashSet("Han")));
  }

  @Test
  void should_fail_if_actual_contains_values_but_not_in_given_order() {
    // GIVEN
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
    String[] subsequence = { "{", "author", "A Game of Thrones", "}" };
    // WHEN
    expectAssertionError(() -> strings.assertContainsSubsequence(INFO, actual, subsequence));
    // THEN
    verify(failures).failure(INFO, shouldContainSubsequence(actual, subsequence, 1));
  }

  @Test
  void should_throw_error_if_subsequence_is_null() {
    assertThatNullPointerException().isThrownBy(() -> strings.assertContainsSubsequence(INFO, "Yoda", null))
                                    .withMessage(arrayOfValuesToLookForIsNull());
  }

  @Test
  void should_throw_error_if_any_value_of_subsequence_is_null() {
    // GIVEN
    String actual = "'author':'George Martin'";
    String[] subsequence = { "author", null };
    // WHEN/THEN
    assertThatNullPointerException().isThrownBy(() -> strings.assertContainsSubsequence(INFO, actual, subsequence))
                                    .withMessage("Expecting CharSequence elements not to be null but found one at index 1");
  }

  @Test
  void should_throw_error_if_subsequence_values_is_empty() {
    // GIVEN
    String actual = "Yoda";
    String[] emptySubsequence = {};
    // WHEN/THEN
    assertThatIllegalArgumentException().isThrownBy(() -> strings.assertContainsSubsequence(INFO, actual, emptySubsequence))
                                        .withMessage(arrayOfValuesToLookForIsEmpty());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> strings.assertContainsSubsequence(INFO, actual, array("a", "b")));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_contains_subsequence_that_specifies_multiple_times_the_same_value_bug_544() {
    strings.assertContainsSubsequence(INFO, "a-b-c-", array("a", "-", "b", "-", "c"));
    strings.assertContainsSubsequence(INFO, "{ 'title':'A Game of Thrones', 'author':'George Martin'}",
                                      array("George", " ", "Martin"));
  }

  @Test
  void should_pass_if_actual_contains_subsequence_according_to_custom_comparison_strategy() {
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsSubsequence(INFO, "Yoda", array("Yo", "da"));
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsSubsequence(INFO, "Yoda", array("Yo", "DA"));
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsSubsequence(INFO, "Yoda", array("YO", "dA"));
  }

  @Test
  void should_fail_if_actual_does_not_contain_subsequence_according_to_custom_comparison_strategy() {
    // GIVEN
    String actual = "Yoda";
    String[] subsequence = { "Yo", "da", "Han" };
    // WHEN
    expectAssertionError(() -> stringsWithCaseInsensitiveComparisonStrategy.assertContainsSubsequence(INFO, actual, subsequence));
    // THEN
    verify(failures).failure(INFO, shouldContain(actual, subsequence, newLinkedHashSet("Han"), comparisonStrategy));
  }

  @Test
  void should_fail_if_actual_contains_values_but_not_in_given_order_according_to_custom_comparison_strategy() {
    // GIVEN
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
    String[] subsequence = { "{", "author", "A Game of Thrones", "}" };
    // WHEN
    expectAssertionError(() -> stringsWithCaseInsensitiveComparisonStrategy.assertContainsSubsequence(INFO, actual, subsequence));
    // THEN
    verify(failures).failure(INFO, shouldContainSubsequence(actual, subsequence, 1, comparisonStrategy));
  }

  @Test
  void should_fail_if_actual_does_not_contain_all_occurrences_of_subsequence_values() {
    // GIVEN
    String actual = "v1 : v2 : v3";
    String[] subsequence = { "v2", "v2", "v3" };
    // WHEN
    expectAssertionError(() -> stringsWithCaseInsensitiveComparisonStrategy.assertContainsSubsequence(INFO, actual, subsequence));
    // THEN
    verify(failures).failure(INFO, shouldContainSubsequence(actual, subsequence, mapOf(entry("v2", 1)), comparisonStrategy));
  }

  @Test
  void should_fail_if_actual_does_not_contain_multiple_repeated_occurrences_of_subsequence_values() {
    // GIVEN
    String actual = "v1 : v2 : v2 : v3";
    String[] subsequence = { "v2", "v2", "v2", "v3" };
    // WHEN
    expectAssertionError(() -> stringsWithCaseInsensitiveComparisonStrategy.assertContainsSubsequence(INFO, actual, subsequence));
    // THEN
    verify(failures).failure(INFO, shouldContainSubsequence(actual, subsequence, mapOf(entry("v2", 2)), comparisonStrategy));
  }

  @Test
  void should_fail_when_actual_does_not_contain_non_existing_elements_and_missing_subsequence_duplicates() {
    // GIVEN
    String actual = "v1 : v2 : v3 : v2";
    String[] subsequence = { "v2", "v2", "v2", "v3", "v4", "v5" };
    // WHEN
    expectAssertionError(() -> strings.assertContainsSubsequence(INFO, actual, subsequence));
    // THEN
    verify(failures).failure(INFO, shouldContain(actual, subsequence, newLinkedHashSet("v4", "v5")));
  }

  @Test
  void should_fail_if_actual_does_not_contain_repeated_occurrences_of_subsequence_values() {
    // GIVEN
    String actual = "Yoda";
    String[] subsequence = { "Yo", "da", "da" };
    // WHEN
    expectAssertionError(() -> stringsWithCaseInsensitiveComparisonStrategy.assertContainsSubsequence(INFO, actual, subsequence));
    // THEN
    verify(failures).failure(INFO, shouldContainSubsequence(actual, subsequence, mapOf(entry("da", 1)), comparisonStrategy));
  }

  @Test
  void should_fail_if_actual_does_not_contain_repeated_occurrences_of_subsequence_values_in_text_with_multiple_values() {
    // GIVEN
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
    String[] subsequence = new String[] { "George", "George" };
    // WHEN
    expectAssertionError(() -> stringsWithCaseInsensitiveComparisonStrategy.assertContainsSubsequence(INFO, actual, subsequence));
    // THEN
    verify(failures).failure(INFO, shouldContainSubsequence(actual, subsequence, mapOf(entry("George", 1)), comparisonStrategy));
  }
}
