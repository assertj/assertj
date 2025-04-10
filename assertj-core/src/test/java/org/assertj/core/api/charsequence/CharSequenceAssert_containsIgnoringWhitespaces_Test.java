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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.api.charsequence;

import static java.lang.String.CASE_INSENSITIVE_ORDER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContainCharSequence.shouldContainIgnoringWhitespaces;
import static org.assertj.core.internal.ErrorMessages.charSequenceToLookForIsNull;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import java.util.Comparator;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.core.api.comparisonstrategy.StandardComparisonStrategy;
import org.assertj.core.testkit.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * @author Johannes Becker
 */
class CharSequenceAssert_containsIgnoringWhitespaces_Test {

  @ParameterizedTest
  @ValueSource(strings = { "Yo", "a n dLuke", "YodaandLuke", "Yoda\tand\nLuke" })
  void should_pass_if_actual_contains_value_when_whitespaces_are_ignored(String value) {
    // GIVEN
    String actual = "Yoda and Luke";
    // WHEN / THEN
    assertThat(actual).containsIgnoringWhitespaces(value);
  }

  @Test
  void should_pass_if_actual_contains_all_given_strings() {
    // GIVEN
    String actual = "Yoda and Luke";
    String[] values = array("Yo", "da", "a n d", "L u k    e");
    // WHEN / THEN
    assertThat(actual).containsIgnoringWhitespaces(values);
  }

  @Test
  void should_fail_if_actual_does_not_contain_value() {
    // GIVEN
    String actual = "Yoda";
    String value = "Luke";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).containsIgnoringWhitespaces(value));
    // THEN
    then(assertionError).hasMessage(shouldContainIgnoringWhitespaces("Yoda", "Luke",
                                                                     StandardComparisonStrategy.instance()).create());
  }

  @Test
  void should_fail_if_actual_contains_value_but_in_different_case() {
    // GIVEN
    String actual = "Yoda";
    String value = "yo";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).containsIgnoringWhitespaces(value));
    // THEN
    then(assertionError).hasMessage(shouldContainIgnoringWhitespaces("Yoda", "yo",
                                                                     StandardComparisonStrategy.instance()).create());
  }

  @Test
  void should_fail_if_actual_contains_value_with_whitespaces_but_in_different_case() {
    // GIVEN
    String actual = "Yoda and Luke";
    String value = "a n dluke";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).containsIgnoringWhitespaces(value));
    // THEN
    then(assertionError).hasMessage(shouldContainIgnoringWhitespaces("Yoda and Luke", "a n dluke",
                                                                     StandardComparisonStrategy.instance()).create());
  }

  @Test
  void should_throw_error_if_value_is_null() {
    // GIVEN
    String actual = "Yoda";
    String value = null;
    // WHEN / THEN
    assertThatNullPointerException().isThrownBy(() -> assertThat(actual).containsIgnoringWhitespaces(value))
                                    .withMessage(charSequenceToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String actual = null;
    String value = "Yoda";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).containsIgnoringWhitespaces(value));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_all_given_strings() {
    // GIVEN
    String actual = "Yoda";
    String[] values = array("Yo", "da", "Han");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).containsIgnoringWhitespaces(values));
    // THEN
    then(assertionError).hasMessage(shouldContainIgnoringWhitespaces(actual, values, newLinkedHashSet("Han"),
                                                                     StandardComparisonStrategy.instance()).create());
  }

  @ParameterizedTest
  @ValueSource(strings = { "Yo", "yo", "YO", "a n dluke", "A N Dluke", "and L u   k" })
  void should_pass_if_actual_contains_value_according_to_custom_comparison_strategy(String value) {
    // GIVEN
    String actual = "Yoda and Luke";
    // WHEN / THEN
    assertThat(actual).usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                      .containsIgnoringWhitespaces(value);
  }

  @Test
  void should_pass_if_actual_contains_all_given_strings_according_to_custom_comparison_strategy() {
    // GIVEN
    String actual = "Yoda and Luke";
    String[] values = array("YO", "dA", "Aa", " n d l");
    // WHEN / THEN
    assertThat(actual).usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                      .containsIgnoringWhitespaces(values);
  }

  @Test
  void should_fail_if_actual_does_not_contain_value_according_to_custom_comparison_strategy() {
    // GIVEN
    String actual = "Yoda";
    String value = "Luke";
    Comparator<String> comparator = CASE_INSENSITIVE_ORDER;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).usingComparator(comparator)
                                                                                 .containsIgnoringWhitespaces(value));
    // THEN
    then(assertionError).hasMessage(shouldContainIgnoringWhitespaces("Yoda", "Luke",
                                                                     new ComparatorBasedComparisonStrategy(comparator)).create());
  }

  @Test
  void should_fail_if_actual_does_not_contain_all_given_strings_according_to_custom_comparison_strategy() {
    // GIVEN
    String actual = "Yoda";
    String[] values = array("Yo", "da", "Han");
    Comparator<String> comparator = CASE_INSENSITIVE_ORDER;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).usingComparator(comparator)
                                                                                 .containsIgnoringWhitespaces(values));
    // THEN
    then(assertionError).hasMessage(shouldContainIgnoringWhitespaces(actual, values, newLinkedHashSet("Han"),
                                                                     new ComparatorBasedComparisonStrategy(comparator)).create());
  }

}
