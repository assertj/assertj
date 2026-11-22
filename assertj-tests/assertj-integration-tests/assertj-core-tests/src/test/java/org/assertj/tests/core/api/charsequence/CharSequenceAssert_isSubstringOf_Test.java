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
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeSubstring.shouldBeSubstring;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.core.api.comparisonstrategy.StandardComparisonStrategy;
import org.assertj.tests.core.testkit.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.Test;

class CharSequenceAssert_isSubstringOf_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isSubstringOf("Yoda"));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_error_if_sequence_is_null() {
    // WHEN
    var exception = catchNullPointerException(() -> assertThat("Yoda").isSubstringOf(null));
    // THEN
    then(exception).hasMessage("Expecting CharSequence not to be null");
  }

  @Test
  void should_pass_if_actual_is_a_substring_of_given_string() {
    assertThat("Yo").isSubstringOf("Yoda");
  }

  @Test
  void should_pass_if_actual_is_equal_to_given_string() {
    assertThat("Yoda").isSubstringOf("Yoda");
  }

  @Test
  void should_pass_if_actual_is_empty() {
    assertThat("").isSubstringOf("Yoda");
    assertThat("").isSubstringOf("");
  }

  @Test
  void should_fail_if_actual_contains_given_string() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat("Yoda").isSubstringOf("oda"));
    // THEN
    then(assertionError).hasMessage(shouldBeSubstring("Yoda", "oda", StandardComparisonStrategy.instance()).create());
  }

  @Test
  void should_fail_if_actual_completely_different_from_given_string() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat("Yoda").isSubstringOf("Luke"));
    // THEN
    then(assertionError).hasMessage(shouldBeSubstring("Yoda", "Luke", StandardComparisonStrategy.instance()).create());
  }

  @Test
  void should_pass_if_actual_is_a_part_of_sequence_only_according_to_custom_comparison_strategy() {
    assertThat("Yo").usingComparator(CaseInsensitiveStringComparator.INSTANCE).isSubstringOf("Yoda");
    assertThat("yo").usingComparator(CaseInsensitiveStringComparator.INSTANCE).isSubstringOf("Yoda");
    assertThat("YO").usingComparator(CaseInsensitiveStringComparator.INSTANCE).isSubstringOf("Yoda");
  }

  @Test
  void should_fail_if_actual_is_not_a_substring_of_sequence_according_to_custom_comparison_strategy() {
    // GIVEN
    var comparisonStrategy = new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.INSTANCE);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat("Yoda").usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                      .isSubstringOf("Luke"));
    // THEN
    then(assertionError).hasMessage(shouldBeSubstring("Yoda", "Luke", comparisonStrategy).create());
  }

}
