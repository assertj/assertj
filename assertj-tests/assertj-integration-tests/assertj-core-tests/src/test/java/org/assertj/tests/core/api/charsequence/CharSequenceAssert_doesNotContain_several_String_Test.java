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
import static org.assertj.core.error.ShouldNotContainCharSequence.shouldNotContain;
import static org.assertj.core.internal.ErrorMessages.arrayOfValuesToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.arrayOfValuesToLookForIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.Set;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.core.api.comparisonstrategy.StandardComparisonStrategy;
import org.assertj.tests.core.testkit.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.Test;

/// @author Alex Ruiz
/// @author Billy Yuan
class CharSequenceAssert_doesNotContain_several_String_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).doesNotContain("Yoda", "Luke"));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_error_if_list_of_values_is_null() {
    // GIVEN
    String[] values = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat("Yoda").doesNotContain(values));
    // THEN
    then(exception).hasMessage(arrayOfValuesToLookForIsNull());
  }

  @Test
  void should_throw_error_if_list_of_values_is_empty() {
    // GIVEN
    String[] values = {};
    // WHEN
    var exception = catchIllegalArgumentException(() -> assertThat("Yoda").doesNotContain(values));
    // THEN
    then(exception).hasMessage(arrayOfValuesToLookForIsEmpty());
  }

  @Test
  void should_throw_error_if_any_element_of_values_is_null() {
    // GIVEN
    String[] values = { "author", null };
    // WHEN
    var exception = catchNullPointerException(() -> assertThat("Yoda").doesNotContain(values));
    // THEN
    then(exception).hasMessage("Expecting CharSequence elements not to be null but found one at index 1");
  }

  @Test
  void should_pass_if_actual_does_not_contain_all_of_given_values() {
    assertThat("Practice makes perfect").doesNotContain("practice", "made", "good");
  }

  @Test
  void should_fail_if_actual_contains_any_of_given_values() {
    // GIVEN
    String actual = "Practice makes perfect";
    String[] values = { "practice", "make", "good" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).doesNotContain(values));
    // THEN
    then(assertionError).hasMessage(shouldNotContain(actual, values, Set.of("make"),
                                                     StandardComparisonStrategy.instance()).create());
  }

  @Test
  void should_pass_if_actual_does_not_contain_all_of_given_values_according_to_custom_comparison_strategy() {
    assertThat("Practice makes perfect").usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                        .doesNotContain("p1ractice", "made", "good");
  }

  @Test
  void should_fail_if_actual_contains_any_of_given_values_according_to_custom_comparison_strategy() {
    // GIVEN
    var comparisonStrategy = new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.INSTANCE);
    String actual = "Practice makes perfect";
    String[] values = { "practice", "made", "good" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                      .doesNotContain(values));
    // THEN
    then(assertionError).hasMessage(shouldNotContain(actual, values, Set.of("practice"), comparisonStrategy).create());
  }

}
