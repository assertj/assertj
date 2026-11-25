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
import static org.assertj.core.api.Assertions.catchIllegalArgumentException;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.ShouldNotContain.shouldNotContain;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.junit.jupiter.api.Test;

/// @author Alex Ruiz
/// @author Joel Costigliola
class IterableAssert_doesNotContain_Test {

  private static final List<String> ACTUAL = List.of("Luke", "Yoda", "Leia");

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    List<String> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).doesNotContain("Yoda"));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_error_if_array_of_values_to_look_for_is_null() {
    // GIVEN
    List<String> actual = List.of();
    String[] values = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(actual).doesNotContain(values));
    // THEN
    then(exception).hasMessage(valuesToLookForIsNull());
  }

  @Test
  void should_throw_error_if_array_of_values_to_look_for_is_empty() {
    // WHEN
    var exception = catchIllegalArgumentException(() -> assertThat(ACTUAL).doesNotContain());
    // THEN
    then(exception).hasMessage(valuesToLookForIsEmpty());
  }

  @Test
  void should_pass_if_actual_does_not_contain_given_values() {
    assertThat(ACTUAL).doesNotContain("Han");
  }

  @Test
  void should_pass_if_actual_does_not_contain_given_values_even_if_duplicated() {
    assertThat(ACTUAL).doesNotContain("Han", "Han", "Anakin");
  }

  @Test
  void should_fail_if_actual_contains_given_values() {
    // GIVEN
    String[] values = { "Luke", "Yoda", "Han" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).doesNotContain(values));
    // THEN
    then(assertionError).hasMessage(shouldNotContain(ACTUAL, values, newLinkedHashSet("Luke", "Yoda")).create());
  }

  @Test
  void should_pass_if_actual_does_not_contain_given_values_according_to_custom_comparison_strategy() {
    assertThat(ACTUAL).usingElementComparator(CASE_INSENSITIVE_ORDER).doesNotContain("Han");
  }

  @Test
  void should_pass_if_actual_does_not_contain_given_values_even_if_duplicated_according_to_custom_comparison_strategy() {
    assertThat(ACTUAL).usingElementComparator(CASE_INSENSITIVE_ORDER).doesNotContain("Han", "Han", "Anakin");
  }

  @Test
  void should_fail_if_actual_contains_given_values_according_to_custom_comparison_strategy() {
    // GIVEN
    String[] values = { "LuKe", "YODA", "Han" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                                      .doesNotContain(values));
    // THEN
    then(assertionError).hasMessage(shouldNotContain(ACTUAL, values, newLinkedHashSet("LuKe", "YODA"),
                                                     new ComparatorBasedComparisonStrategy(CASE_INSENSITIVE_ORDER)).create());
  }

}
