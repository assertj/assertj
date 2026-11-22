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
import static org.assertj.core.util.Arrays.array;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;
import java.util.Set;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.core.api.comparisonstrategy.StandardComparisonStrategy;
import org.assertj.tests.core.testkit.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.Test;

/// @author Billy Yuan
class CharSequenceAssert_doesNotContain_several_String_as_Iterable_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).doesNotContain(List.of("Yoda", "Luke")));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_error_if_values_are_null() {
    // GIVEN
    Iterable<CharSequence> values = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat("Yoda").doesNotContain(values));
    // THEN
    then(exception).hasMessage(arrayOfValuesToLookForIsNull());
  }

  @Test
  void should_throw_error_if_values_are_empty() {
    // GIVEN
    List<CharSequence> values = List.of();
    // WHEN
    var exception = catchIllegalArgumentException(() -> assertThat("Yoda").doesNotContain(values));
    // THEN
    then(exception).hasMessage(arrayOfValuesToLookForIsEmpty());
  }

  @Test
  void should_pass_if_actual_does_not_contain_any_of_given_values() {
    assertThat("Practice makes perfect").doesNotContain(List.of("practice", "made", "good"));
  }

  @Test
  void should_fail_if_actual_contains_any_of_given_values() {
    // GIVEN
    String actual = "Practice makes perfect";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).doesNotContain(List.of("practice", "make", "good")));
    // THEN
    then(assertionError).hasMessage(shouldNotContain(actual, array("practice", "make", "good"), Set.of("make"),
                                                     StandardComparisonStrategy.instance()).create());
  }

  @Test
  void should_fail_if_actual_contains_any_of_given_values_according_to_custom_comparison_strategy() {
    // GIVEN
    var comparisonStrategy = new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.INSTANCE);
    String actual = "Practice makes perfect";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                      .doesNotContain(List.of("practice", "made", "good")));
    // THEN
    then(assertionError).hasMessage(shouldNotContain(actual, array("practice", "made", "good"), Set.of("practice"),
                                                     comparisonStrategy).create());
  }

}
