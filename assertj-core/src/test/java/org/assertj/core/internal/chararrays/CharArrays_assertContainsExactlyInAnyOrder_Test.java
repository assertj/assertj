/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.internal.chararrays;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldContainExactlyInAnyOrder.shouldContainExactlyInAnyOrder;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.testkit.CharArrays.arrayOf;
import static org.assertj.core.testkit.CharArrays.emptyArray;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.CharArrays;
import org.assertj.core.internal.CharArraysBaseTest;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link CharArrays#assertContainsExactlyInAnyOrder(AssertionInfo, char[], char[])}</code>.
 */
class CharArrays_assertContainsExactlyInAnyOrder_Test extends CharArraysBaseTest {

  @Test
  void should_pass_if_actual_contains_given_values_exactly_in_any_order() {
    arrays.assertContainsExactlyInAnyOrder(someInfo(), actual, arrayOf('a', 'b', 'c'));
  }

  @Test
  void should_pass_if_actual_and_given_values_are_empty() {
    arrays.assertContainsExactlyInAnyOrder(someInfo(), emptyArray(), emptyArray());
  }

  @Test
  void should_pass_if_actual_contains_given_values_exactly_but_in_different_order() {
    AssertionInfo info = someInfo();
    arrays.assertContainsExactlyInAnyOrder(info, actual, arrayOf('a', 'c', 'b'));
  }

  @Test
  void should_fail_if_arrays_have_different_sizes() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContainsExactlyInAnyOrder(someInfo(), actual,
                                                                                                            arrayOf('a', 'b')));
  }

  @Test
  void should_fail_if_expected_empty_and_actual_is_not() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContainsExactlyInAnyOrder(someInfo(), actual,
                                                                                                            emptyArray()));
  }

  @Test
  void should_throw_error_if_expected_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertContainsExactlyInAnyOrder(someInfo(), actual, null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContainsExactlyInAnyOrder(someInfo(), null,
                                                                                                            arrayOf('b')))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_given_values_exactly() {
    AssertionInfo info = someInfo();
    char[] expected = { 'a', 'b', 'e' };

    Throwable error = catchThrowable(() -> arrays.assertContainsExactlyInAnyOrder(info, actual, expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldContainExactlyInAnyOrder(actual, expected, newArrayList('e'), newArrayList('c'),
                                                                  StandardComparisonStrategy.instance()));
  }

  @Test
  void should_fail_if_actual_contains_all_given_values_but_size_differ() {
    AssertionInfo info = someInfo();
    char[] expected = { 'a', 'b' };

    Throwable error = catchThrowable(() -> arrays.assertContainsExactlyInAnyOrder(info, actual, expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info,
                             shouldContainExactlyInAnyOrder(actual, expected, emptyList(), newArrayList('c'),
                                                            StandardComparisonStrategy.instance()));
  }

  @Test
  void should_fail_if_actual_contains_duplicates_and_expected_does_not() {
    AssertionInfo info = someInfo();
    actual = arrayOf('a', 'b', 'b');
    char[] expected = { 'a', 'b' };

    Throwable error = catchThrowable(() -> arrays.assertContainsExactlyInAnyOrder(info, actual, expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info,
                             shouldContainExactlyInAnyOrder(actual, expected, emptyList(), newArrayList('b'),
                                                            StandardComparisonStrategy.instance()));
  }

  @Test
  void should_fail_if_expected_contains_duplicates_and_actual_does_not() {
    AssertionInfo info = someInfo();
    actual = arrayOf('a', 'b');
    char[] expected = { 'a', 'b', 'b' };

    Throwable error = catchThrowable(() -> arrays.assertContainsExactlyInAnyOrder(info, actual, expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info,
                             shouldContainExactlyInAnyOrder(actual, expected, newArrayList('b'), emptyList(),
                                                            StandardComparisonStrategy.instance()));
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  void should_pass_if_actual_contains_given_values_exactly_in_any_order_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(someInfo(), actual, arrayOf('a', 'B', 'c'));
  }

  @Test
  void should_pass_if_actual_contains_given_values_exactly_in_different_order_according_to_custom_comparison_strategy() {
    char[] expected = { 'A', 'c', 'b' };
    arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(someInfo(), actual, expected);
  }

  @Test
  void should_fail_if_expected_empty_and_actual_is_not_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(someInfo(),
                                                                                                                                        actual,
                                                                                                                                        emptyArray()));
  }

  @Test
  void should_throw_error_if_expected_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(someInfo(),
                                                                                                                         actual,
                                                                                                                         null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(someInfo(),
                                                                                                                                        null,
                                                                                                                                        arrayOf('b')))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_given_values_exactly_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    char[] expected = { 'a', 'B', 'e' };

    Throwable error = catchThrowable(() -> arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(info, actual,
                                                                                                              expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldContainExactlyInAnyOrder(actual, expected, newArrayList('e'), newArrayList('c'),
                                                                  caseInsensitiveComparisonStrategy));
  }

  @Test
  void should_fail_if_actual_contains_all_given_values_but_size_differ_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    char[] expected = { 'a', 'b' };

    Throwable error = catchThrowable(() -> arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(info, actual,
                                                                                                              expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info,
                             shouldContainExactlyInAnyOrder(actual, expected, emptyList(), newArrayList('c'),
                                                            caseInsensitiveComparisonStrategy));
  }

  @Test
  void should_fail_if_actual_contains_duplicates_and_expected_does_not_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    actual = arrayOf('a', 'b', 'b');
    char[] expected = { 'a', 'b' };

    Throwable error = catchThrowable(() -> arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(info, actual,
                                                                                                              expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info,
                             shouldContainExactlyInAnyOrder(actual, expected, emptyList(), newArrayList('b'),
                                                            caseInsensitiveComparisonStrategy));
  }

  @Test
  void should_fail_if_expected_contains_duplicates_and_actual_does_not_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    actual = arrayOf('a', 'b');
    char[] expected = { 'a', 'b', 'b' };

    Throwable error = catchThrowable(() -> arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(info, actual,
                                                                                                              expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info,
                             shouldContainExactlyInAnyOrder(actual, expected, newArrayList('b'), emptyList(),
                                                            caseInsensitiveComparisonStrategy));
  }

}
