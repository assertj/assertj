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
import static org.assertj.core.error.ShouldContainCharSequenceOnlyOnce.shouldContainOnlyOnce;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.internal.ErrorMessages.charSequenceToLookForIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.tests.core.testkit.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.Test;

/// @author Pauline Iogna
/// @author Joel Costigliola
class CharSequenceAssert_containsOnlyOnce_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsOnlyOnce("Yoda"));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_error_if_sequence_is_null() {
    // WHEN
    var exception = catchNullPointerException(() -> assertThat("Yoda").containsOnlyOnce(null));
    // THEN
    then(exception).hasMessage(charSequenceToLookForIsNull());
  }

  @Test
  void should_pass_if_actual_contains_given_string_only_once() {
    assertThat("Yoda").containsOnlyOnce("Yo");
  }

  @Test
  void should_fail_if_actual_contains_given_string_more_than_once() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat("Yodayoda").containsOnlyOnce("oda"));
    // THEN
    then(assertionError).hasMessage(shouldContainOnlyOnce("Yodayoda", "oda", 2).create());
  }

  @Test
  void should_fail_if_actual_contains_sequence_only_once_but_in_different_case() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat("Yoda").containsOnlyOnce("yo"));
    // THEN
    then(assertionError).hasMessage(shouldContainOnlyOnce("Yoda", "yo", 0).create());
  }

  @Test
  void should_fail_if_actual_does_not_contain_given_string() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat("Yoda").containsOnlyOnce("Luke"));
    // THEN
    then(assertionError).hasMessage(shouldContainOnlyOnce("Yoda", "Luke", 0).create());
  }

  @Test
  void should_pass_if_actual_contains_sequence_only_once_according_to_custom_comparison_strategy() {
    assertThat("Yoda").usingComparator(CaseInsensitiveStringComparator.INSTANCE).containsOnlyOnce("Yo");
    assertThat("Yoda").usingComparator(CaseInsensitiveStringComparator.INSTANCE).containsOnlyOnce("yo");
    assertThat("Yoda").usingComparator(CaseInsensitiveStringComparator.INSTANCE).containsOnlyOnce("YO");
  }

  @Test
  void should_fail_if_actual_does_not_contain_sequence_only_once_according_to_custom_comparison_strategy() {
    // GIVEN
    var comparisonStrategy = new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.INSTANCE);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat("Yoda").usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                      .containsOnlyOnce("Luke"));
    // THEN
    then(assertionError).hasMessage(shouldContainOnlyOnce("Yoda", "Luke", 0, comparisonStrategy).create());
  }

  @Test
  void should_fail_if_actual_contains_sequence_several_times_according_to_custom_comparison_strategy() {
    // GIVEN
    var comparisonStrategy = new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.INSTANCE);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat("Yodayoda").usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                          .containsOnlyOnce("YODA"));
    // THEN
    then(assertionError).hasMessage(shouldContainOnlyOnce("Yodayoda", "YODA", 2, comparisonStrategy).create());
  }

}
