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
package org.assertj.core.internal.objectarrays;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldContainExactlyInAnyOrder.shouldContainExactlyInAnyOrder;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.ObjectArraysBaseTest;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Iterables#assertContainsExactlyInAnyOrder(AssertionInfo, Iterable, Object[])}</code>.
 *
 * @author Lovro Pandzic
 */
class ObjectArrays_assertContainsExactlyInAnyOrder_Test extends ObjectArraysBaseTest {

  @Test
  void should_pass_if_actual_contains_exactly_in_any_order_given_values() {
    arrays.assertContainsExactlyInAnyOrder(someInfo(), actual, array("Leia", "Yoda", "Luke"));
  }

  @Test
  void should_pass_if_actual_contains_given_values_exactly_in_any_order_with_null_elements() {
    actual = array("Luke", "Yoda", "Leia", null);
    arrays.assertContainsExactlyInAnyOrder(someInfo(), actual, array("Leia", null, "Yoda", "Luke"));
  }

  @Test
  void should_pass_if_actual_and_given_values_are_empty() {
    arrays.assertContainsExactlyInAnyOrder(someInfo(), array(), array());
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContainsExactlyInAnyOrder(someInfo(), actual,
                                                                                                            array()));
  }

  @Test
  void should_fail_if_arrays_have_different_sizes() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContainsExactlyInAnyOrder(someInfo(), actual,
                                                                                                            array("Luke",
                                                                                                                  "Yoda")));
  }

  @Test
  void should_throw_error_if_array_of_values_to_look_for_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertContainsExactlyInAnyOrder(someInfo(), actual, null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContainsExactlyInAnyOrder(someInfo(), null,
                                                                                                            array("Yoda")))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_given_values_exactly_in_any_order() {
    AssertionInfo info = someInfo();
    Object[] expected = { "Luke", "Yoda", "Han" };

    Throwable error = catchThrowable(() -> arrays.assertContainsExactlyInAnyOrder(info, actual, expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldContainExactlyInAnyOrder(actual, expected, newArrayList("Han"), newArrayList("Leia"),
                                                                  StandardComparisonStrategy.instance()));
  }

  @Test
  void should_fail_if_actual_contains_duplicates_and_expected_does_not() {
    AssertionInfo info = someInfo();
    actual = array("Luke", "Leia", "Luke");
    Object[] expected = { "Luke", "Leia" };

    Throwable error = catchThrowable(() -> arrays.assertContainsExactlyInAnyOrder(info, actual, expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info,
                             shouldContainExactlyInAnyOrder(actual, expected, emptyList(), newArrayList("Luke"),
                                                            StandardComparisonStrategy.instance()));
  }

  @Test
  void should_fail_if_expected_contains_duplicates_and_actual_does_not() {
    AssertionInfo info = someInfo();
    actual = array("Luke", "Leia");
    Object[] expected = { "Luke", "Leia", "Luke" };

    Throwable error = catchThrowable(() -> arrays.assertContainsExactlyInAnyOrder(info, actual, expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info,
                             shouldContainExactlyInAnyOrder(actual, expected, newArrayList("Luke"), emptyList(),
                                                            StandardComparisonStrategy.instance()));
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  void should_pass_if_actual_contains_given_values_exactly_in_any_order_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(someInfo(), actual,
                                                                       array("LUKE", "YODA", "Leia"));
  }

  @Test
  void should_fail_if_actual_does_not_contain_given_values_exactly_in_any_order_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Object[] expected = { "Luke", "Yoda", "Han" };

    Throwable error = catchThrowable(() -> arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(info, actual,
                                                                                                              expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldContainExactlyInAnyOrder(actual, expected, newArrayList("Han"), newArrayList("Leia"),
                                                                  caseInsensitiveStringComparisonStrategy));
  }

  @Test
  void should_fail_if_actual_contains_duplicates_and_expected_does_not_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    actual = array("Luke", "Leia", "Luke");
    Object[] expected = { "Luke", "Leia" };

    Throwable error = catchThrowable(() -> arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(info, actual,
                                                                                                              expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info,
                             shouldContainExactlyInAnyOrder(actual, expected, emptyList(), newArrayList("Luke"),
                                                            caseInsensitiveStringComparisonStrategy));
  }

  @Test
  void should_fail_if_expected_contains_duplicates_and_actual_does_not_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    actual = array("Luke", "Leia");
    Object[] expected = { "Luke", "Leia", "Luke" };

    Throwable error = catchThrowable(() -> arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(info, actual,
                                                                                                              expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info,
                             shouldContainExactlyInAnyOrder(actual, expected, newArrayList("Luke"), emptyList(),
                                                            caseInsensitiveStringComparisonStrategy));
  }

}
