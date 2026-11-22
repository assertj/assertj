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
import static org.assertj.core.error.ShouldContainCharSequence.shouldContain;
import static org.assertj.core.error.ShouldContainSubsequenceOfCharSequence.shouldContainSubsequence;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.internal.ErrorMessages.arrayOfValuesToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.arrayOfValuesToLookForIsNull;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.tests.core.testkit.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.Test;

/// @author André Diermann
class CharSequenceAssert_containsSubsequence_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsSubsequence(List.of("a", "b")));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_error_if_subsequence_is_null() {
    // GIVEN
    Iterable<CharSequence> subsequence = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat("Yoda").containsSubsequence(subsequence));
    // THEN
    then(exception).hasMessage(arrayOfValuesToLookForIsNull());
  }

  @Test
  void should_throw_error_if_subsequence_values_is_empty() {
    // GIVEN
    List<CharSequence> subsequence = List.of();
    // WHEN
    var exception = catchIllegalArgumentException(() -> assertThat("Yoda").containsSubsequence(subsequence));
    // THEN
    then(exception).hasMessage(arrayOfValuesToLookForIsEmpty());
  }

  @Test
  void should_pass_if_actual_contains_subsequence_with_values_between() {
    // GIVEN
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
    // WHEN/THEN
    then(actual).containsSubsequence(List.of("{", "title", "A Game of Thrones", "}"));
  }

  @Test
  void should_fail_if_actual_does_not_contain_all_given_strings() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat("Yoda").containsSubsequence(List.of("Yo", "da", "Han")));
    // THEN
    then(assertionError).hasMessage(shouldContain("Yoda", array("Yo", "da", "Han"), newLinkedHashSet("Han")).create());
  }

  @Test
  void should_fail_if_actual_contains_values_but_not_in_given_order() {
    // GIVEN
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
    String[] subsequence = { "{", "author", "A Game of Thrones", "}" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsSubsequence(List.of(subsequence)));
    // THEN
    then(assertionError).hasMessage(shouldContainSubsequence(actual, subsequence, 1).create());
  }

  @Test
  void should_pass_if_actual_contains_subsequence_according_to_custom_comparison_strategy() {
    assertThat("Yoda").usingComparator(CaseInsensitiveStringComparator.INSTANCE).containsSubsequence(List.of("YO", "dA"));
  }

  @Test
  void should_fail_if_actual_contains_values_but_not_in_given_order_according_to_custom_comparison_strategy() {
    // GIVEN
    var comparisonStrategy = new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.INSTANCE);
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
    String[] subsequence = { "{", "AUTHOR", "a game of thrones", "}" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                      .containsSubsequence(List.of(subsequence)));
    // THEN
    then(assertionError).hasMessage(shouldContainSubsequence(actual, subsequence, 1, comparisonStrategy).create());
  }

}
