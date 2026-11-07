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
package org.assertj.core.internal.chararrays;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContainExactly.elementsDifferAtIndex;
import static org.assertj.core.error.ShouldContainExactly.shouldContainExactly;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.testkit.CharArrays.arrayOf;
import static org.assertj.core.testkit.CharArrays.emptyArray;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.Arrays.asList;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.CharArrays;
import org.assertj.core.internal.CharArraysBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link CharArrays#assertContainsExactly(AssertionInfo, char[], char[])}</code>.
 */
class CharArrays_assertContainsExactly_Test extends CharArraysBaseTest {

  @Test
  void should_pass_if_actual_contains_given_values_exactly() {
    arrays.assertContainsExactly(someInfo(), actual, arrayOf('a', 'b', 'c'));
  }

  @Test
  void should_pass_if_actual_and_given_values_are_empty() {
    arrays.assertContainsExactly(someInfo(), emptyArray(), emptyArray());
  }

  @Test
  void should_fail_if_actual_contains_given_values_exactly_but_in_different_order() {
    AssertionInfo info = someInfo();
    char[] expected = arrayOf('a', 'c', 'b');

    // WHEN
    var error = expectAssertionError(() -> arrays.assertContainsExactly(info, actual, expected));
    // THEN
    then(error).hasMessage(elementsDifferAtIndex('b', 'c', 1).create());
  }

  @Test
  void should_fail_if_arrays_have_different_sizes() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContainsExactly(someInfo(), actual,
                                                                                                  arrayOf('a', 'b')));
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContainsExactly(someInfo(), actual,
                                                                                                  emptyArray()));
  }

  @Test
  void should_throw_error_if_array_of_values_to_look_for_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertContainsExactly(someInfo(), actual, null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContainsExactly(someInfo(), null, arrayOf('b')))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_given_values_exactly() {
    AssertionInfo info = someInfo();
    char[] expected = { 'a', 'b', 'e' };

    // WHEN
    var error = expectAssertionError(() -> arrays.assertContainsExactly(info, actual, expected));
    // THEN
    then(error).hasMessage(shouldContainExactly(actual, asList(expected),
                                                newArrayList('e'), newArrayList('c')).create());
  }

  @Test
  void should_fail_if_actual_contains_all_given_values_but_size_differ() {
    AssertionInfo info = someInfo();
    char[] expected = { 'a', 'b', 'c', 'c' };

    // WHEN
    var error = expectAssertionError(() -> arrays.assertContainsExactly(info, actual, expected));
    // THEN
    then(error).hasMessage(shouldContainExactly(actual, asList(expected),
                                                newArrayList('c'), newArrayList()).create());
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  void should_pass_if_actual_contains_given_values_exactly_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsExactly(someInfo(), actual, arrayOf('a', 'B', 'c'));
  }

  @Test
  void should_pass_if_actual_contains_given_values_exactly_in_different_order_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    char[] expected = { 'A', 'c', 'b' };

    // WHEN
    var error = expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertContainsExactly(info, actual, expected));
    // THEN
    then(error).hasMessage(elementsDifferAtIndex('b', 'c', 1, caseInsensitiveComparisonStrategy).create());
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsExactly(someInfo(),
                                                                                                                              actual,
                                                                                                                              emptyArray()));
  }

  @Test
  void should_throw_error_if_array_of_values_to_look_for_is_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsExactly(someInfo(),
                                                                                                               actual,
                                                                                                               null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsExactly(someInfo(),
                                                                                                                              null,
                                                                                                                              arrayOf('b')))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_given_values_exactly_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    char[] expected = { 'a', 'B', 'e' };

    // WHEN
    var error = expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertContainsExactly(info, actual, expected));
    // THEN
    then(error).hasMessage(shouldContainExactly(actual, asList(expected), newArrayList('e'),
                                                newArrayList('c'),
                                                caseInsensitiveComparisonStrategy).create());
  }

  @Test
  void should_fail_if_actual_contains_all_given_values_but_size_differ_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    char[] expected = { 'a', 'b', 'C', 'c' };

    // WHEN
    var error = expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertContainsExactly(info, actual, expected));
    // THEN
    then(error).hasMessage(shouldContainExactly(actual, asList(expected),
                                                newArrayList('c'), newArrayList(),
                                                caseInsensitiveComparisonStrategy).create());
  }

}
