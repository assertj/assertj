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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.internal.objectarrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldContainExactly.elementsDifferAtIndex;
import static org.assertj.core.error.ShouldContainExactly.shouldContainExactly;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Arrays.asList;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.ObjectArraysBaseTest;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Iterables#assertContainsExactly(AssertionInfo, Iterable, Object[])}</code>.
 *
 * @author Joel Costigliola
 */
class ObjectArrays_assertContainsExactly_Test extends ObjectArraysBaseTest {

  @Test
  void should_pass_if_actual_contains_exactly_given_values() {
    arrays.assertContainsExactly(someInfo(), actual, array("Luke", "Yoda", "Leia"));
  }

  @Test
  void should_pass_if_actual_contains_given_values_exactly_with_null_elements() {
    actual = array("Luke", "Yoda", "Leia", null);
    arrays.assertContainsExactly(someInfo(), actual, array("Luke", "Yoda", "Leia", null));
  }

  @Test
  void should_pass_if_actual_contains_given_values_exactly_with_duplicate_elements() {
    actual = array("Luke", "Yoda", "Yoda");
    arrays.assertContainsExactly(someInfo(), actual, array("Luke", "Yoda", "Yoda"));
  }

  @Test
  void should_pass_if_actual_and_given_values_are_empty() {
    arrays.assertContainsExactly(someInfo(), array(), array());
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContainsExactly(someInfo(), actual, array()));
  }

  @Test
  void should_fail_if_arrays_have_different_sizes() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContainsExactly(someInfo(), actual,
                                                                                                  array("Luke", "Yoda")));
  }

  @Test
  void should_throw_error_if_array_of_values_to_look_for_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertContainsExactly(someInfo(), actual, null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContainsExactly(someInfo(), null,
                                                                                                  array("Yoda")))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_given_values_exactly() {
    AssertionInfo info = someInfo();
    Object[] expected = { "Luke", "Yoda", "Han" };

    Throwable error = catchThrowable(() -> arrays.assertContainsExactly(info, actual, expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldContainExactly(actual, asList(expected),
                                                        newArrayList("Han"), newArrayList("Leia")));
  }

  @Test
  void should_fail_if_actual_contains_all_given_values_but_in_different_order() {
    AssertionInfo info = someInfo();
    Object[] expected = { "Luke", "Leia", "Yoda" };

    Throwable error = catchThrowable(() -> arrays.assertContainsExactly(info, actual, expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, elementsDifferAtIndex("Yoda", "Leia", 1));
  }

  @Test
  void should_fail_if_actual_contains_all_given_values_but_size_differ() {
    AssertionInfo info = someInfo();
    actual = array("Luke", "Leia", "Luke");
    Object[] expected = { "Luke", "Leia" };

    Throwable error = catchThrowable(() -> arrays.assertContainsExactly(info, actual, expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldContainExactly(actual, asList(expected),
                                                        newArrayList(), newArrayList("Luke"),
                                                        StandardComparisonStrategy.instance()));
  }

  @Test
  void should_fail_if_arrays_have_different_sizes_for_large_arrays() {
    // GIVEN
    Object[] actual = new Object[2000];
    Object[] expected = new Object[actual.length + 1];
    for (int i = 0; i < actual.length; i++) {
      actual[i] = String.valueOf(i);
      expected[i] = String.valueOf(i);
    }
    expected[actual.length] = "extra";
    AssertionInfo info = someInfo();

    // WHEN
    Throwable error = catchThrowable(() -> arrays.assertContainsExactly(info, actual, expected));

    // THEN
    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldContainExactly(actual, asList(expected),
                                                        newArrayList("extra"), newArrayList(),
                                                        StandardComparisonStrategy.instance()));
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  void should_pass_if_actual_contains_given_values_exactly_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsExactly(someInfo(), actual,
                                                             array("LUKE", "YODA", "Leia"));
  }

  @Test
  void should_fail_if_actual_does_not_contain_given_values_exactly_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Object[] expected = { "Luke", "Yoda", "Han" };

    Throwable error = catchThrowable(() -> arraysWithCustomComparisonStrategy.assertContainsExactly(info, actual, expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info,
                             shouldContainExactly(actual, asList(expected), newArrayList("Han"), newArrayList("Leia"),
                                                  caseInsensitiveStringComparisonStrategy));
  }

  @Test
  void should_fail_if_actual_contains_all_given_values_in_different_order_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Object[] expected = { "Luke", "Leia", "Yoda" };

    Throwable error = catchThrowable(() -> arraysWithCustomComparisonStrategy.assertContainsExactly(info, actual, expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, elementsDifferAtIndex("Yoda", "Leia", 1, caseInsensitiveStringComparisonStrategy));
  }

  @Test
  void should_fail_if_actual_contains_all_given_values_but_size_differ_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    actual = array("Luke", "Leia", "Luke");
    Object[] expected = { "Luke", "Leia" };

    Throwable error = catchThrowable(() -> arraysWithCustomComparisonStrategy.assertContainsExactly(info, actual, expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info,
                             shouldContainExactly(actual, asList(expected), newArrayList(), newArrayList("Luke"),
                                                  caseInsensitiveStringComparisonStrategy));
  }

}
