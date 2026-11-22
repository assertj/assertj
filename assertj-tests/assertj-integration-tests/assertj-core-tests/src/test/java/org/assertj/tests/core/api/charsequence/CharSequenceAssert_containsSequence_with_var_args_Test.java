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
import static org.assertj.core.error.ShouldContainSequenceOfCharSequence.shouldContainSequence;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.internal.ErrorMessages.arrayOfValuesToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.arrayOfValuesToLookForIsNull;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.tests.core.testkit.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.Test;

/// @author Billy Yuan
class CharSequenceAssert_containsSequence_with_var_args_Test {

  private static final String ACTUAL = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsSequence("{ ", "'title':",
                                                                                        "'A Game of Thrones'", ","));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_error_if_sequence_is_null() {
    // GIVEN
    CharSequence[] sequence = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(ACTUAL).containsSequence(sequence));
    // THEN
    then(exception).hasMessage(arrayOfValuesToLookForIsNull());
  }

  @Test
  void should_throw_error_if_any_value_of_sequence_is_null() {
    // GIVEN
    String[] sequence = { "author", null };
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(ACTUAL).containsSequence(sequence));
    // THEN
    then(exception).hasMessage("Expecting CharSequence elements not to be null but found one at index 1");
  }

  @Test
  void should_throw_error_if_sequence_values_are_empty() {
    // GIVEN
    String[] sequence = {};
    // WHEN
    var exception = catchIllegalArgumentException(() -> assertThat(ACTUAL).containsSequence(sequence));
    // THEN
    then(exception).hasMessage(arrayOfValuesToLookForIsEmpty());
  }

  @Test
  void should_pass_if_actual_contains_sequence() {
    assertThat(ACTUAL).containsSequence("{ ", "'title':", "'A Game of Thrones'", ",");
  }

  @Test
  void should_pass_if_actual_contains_sequence_that_specifies_multiple_times_the_same_value() {
    assertThat("a-b-c-").containsSequence("a", "-", "b", "-", "c");
  }

  @Test
  void should_fail_if_actual_contains_sequence_with_values_between() {
    // GIVEN
    String[] sequence = { "{ ", "'author':'George Martin'}" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).containsSequence(sequence));
    // THEN
    then(assertionError).hasMessage(shouldContainSequence(ACTUAL, sequence).create());
  }

  @Test
  void should_fail_if_actual_does_not_contain_all_given_strings() {
    // GIVEN
    String[] sequence = { "{ ", "'title':", "'A Game of Thrones'", "unexpectedString" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).containsSequence(sequence));
    // THEN
    then(assertionError).hasMessage(shouldContain(ACTUAL, sequence, newLinkedHashSet("unexpectedString")).create());
  }

  @Test
  void should_fail_if_actual_contains_values_but_not_in_the_given_order() {
    // GIVEN
    String[] sequence = { "'A Game of Thrones'", "'title':" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).containsSequence(sequence));
    // THEN
    then(assertionError).hasMessage(shouldContainSequence(ACTUAL, sequence).create());
  }

  @Test
  void should_pass_if_actual_contains_sequence_according_to_custom_comparison_strategy() {
    assertThat("Yoda").usingComparator(CaseInsensitiveStringComparator.INSTANCE).containsSequence("Yo", "da");
    assertThat("Yoda").usingComparator(CaseInsensitiveStringComparator.INSTANCE).containsSequence("Yo", "DA");
    assertThat("Yoda").usingComparator(CaseInsensitiveStringComparator.INSTANCE).containsSequence("YO", "dA");
  }

  @Test
  void should_fail_if_actual_does_not_contain_sequence_according_to_custom_comparison_strategy() {
    // GIVEN
    var comparisonStrategy = new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.INSTANCE);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat("Yoda").usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                      .containsSequence("Yo", "da", "Han"));
    // THEN
    then(assertionError).hasMessage(shouldContain("Yoda", array("Yo", "da", "Han"), newLinkedHashSet("Han"),
                                                  comparisonStrategy).create());
  }

  @Test
  void should_fail_if_actual_contains_values_but_not_in_given_order_according_to_custom_comparison_strategy() {
    // GIVEN
    var comparisonStrategy = new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.INSTANCE);
    String[] sequence = { ", 'author'", "'A Game of Thrones'" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                      .containsSequence(sequence));
    // THEN
    then(assertionError).hasMessage(shouldContainSequence(ACTUAL, sequence, comparisonStrategy).create());
  }

}
