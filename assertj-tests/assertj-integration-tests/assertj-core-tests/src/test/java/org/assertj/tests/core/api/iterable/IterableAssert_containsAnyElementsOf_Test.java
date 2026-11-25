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
package org.assertj.tests.core.api.iterable;

import static java.lang.String.CASE_INSENSITIVE_ORDER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ActualIsNotEmpty.actualIsNotEmpty;
import static org.assertj.core.error.ShouldContainAnyOf.shouldContainAnyOf;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.core.api.comparisonstrategy.StandardComparisonStrategy;
import org.junit.jupiter.api.Test;

/// @author Marko Bekhta
class IterableAssert_containsAnyElementsOf_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    List<String> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsAnyElementsOf(list("Yoda")));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_error_if_iterable_of_values_to_look_for_is_null() {
    // GIVEN
    List<String> values = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(list("Luke", "Yoda", "Leia")).containsAnyElementsOf(values));
    // THEN
    then(exception).hasMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_iterable_of_values_to_look_for_is_empty_and_actual_is_not() {
    // GIVEN
    List<String> actual = list("Luke", "Yoda", "Leia");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsAnyElementsOf(List.of()));
    // THEN
    then(assertionError).hasMessage(actualIsNotEmpty(actual).create());
  }

  @Test
  void should_pass_if_actual_and_given_values_are_empty() {
    assertThat(List.<String> of()).containsAnyElementsOf(List.of());
  }

  @Test
  void should_pass_if_actual_contains_just_one_of_given_values() {
    assertThat(list("Luke", "Yoda", "Leia")).containsAnyElementsOf(list("Luke", "John", "Tom"));
  }

  @Test
  void should_compile_as_containsAnyElementsOf_declares_bounded_wildcard_parameter() {
    // GIVEN
    Iterable<CharSequence> actual = list("some_value", "some_different_value");
    List<String> values = list("some_value", "some_other_value");
    // WHEN/THEN
    then(actual).containsAnyElementsOf(values);
  }

  @Test
  void should_fail_if_actual_does_not_contain_any_of_the_given_values() {
    // GIVEN
    List<String> actual = list("Luke", "Yoda", "Leia");
    List<String> expected = list("Han", "John");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsAnyElementsOf(expected));
    // THEN
    then(assertionError).hasMessage(shouldContainAnyOf(actual, expected.toArray(),
                                                       StandardComparisonStrategy.instance()).create());
  }

  @Test
  void should_pass_if_actual_contains_just_one_of_given_values_according_to_custom_comparison_strategy() {
    assertThat(list("Luke", "Yoda", "Leia")).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                            .containsAnyElementsOf(list("LuKe", "JoHn", "ToM"));
  }

  @Test
  void should_fail_if_actual_does_not_contain_values_according_to_custom_comparison_strategy() {
    // GIVEN
    List<String> actual = list("Luke", "Yoda", "Leia");
    List<String> expected = list("Han", "John");
    var comparisonStrategy = new ComparatorBasedComparisonStrategy(CASE_INSENSITIVE_ORDER);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                                      .containsAnyElementsOf(expected));
    // THEN
    then(assertionError).hasMessage(shouldContainAnyOf(actual, expected.toArray(), comparisonStrategy).create());
  }

}
