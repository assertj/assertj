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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.internal.bytearrays;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.error.ShouldContainExactlyInAnyOrder.shouldContainExactlyInAnyOrder;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.test.ByteArrays.arrayOf;
import static org.assertj.core.test.ByteArrays.emptyArray;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.emptyList;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ByteArrays;
import org.assertj.core.internal.ByteArraysBaseTest;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.assertj.core.test.IntArrays;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ByteArrays#assertContainsExactlyInAnyOrder(AssertionInfo, byte[], int[])}</code>.
 */
public class ByteArrays_assertContainsExactlyInAnyOrder_with_Integer_Arguments_Test extends ByteArraysBaseTest {

  @Test
  public void should_pass_if_actual_contains_given_values_exactly_in_any_order() {
    arrays.assertContainsExactlyInAnyOrder(someInfo(), actual, IntArrays.arrayOf(6, 8, 10));
  }

  @Test
  public void should_pass_if_actual_and_given_values_are_empty() {
    arrays.assertContainsExactlyInAnyOrder(someInfo(), emptyArray(), IntArrays.emptyArray());
  }

  @Test
  public void should_pass_if_actual_contains_given_values_exactly_but_in_different_order() {
    AssertionInfo info = someInfo();
    arrays.assertContainsExactlyInAnyOrder(info, actual, IntArrays.arrayOf(6, 10, 8));
  }

  @Test
  public void should_fail_if_arrays_have_different_sizes() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContainsExactlyInAnyOrder(someInfo(), actual, IntArrays.arrayOf(6, 8)));
  }

  @Test
  public void should_fail_if_expected_is_empty_and_actual_is_not() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContainsExactlyInAnyOrder(someInfo(), actual, IntArrays.emptyArray()));
  }

  @Test
  public void should_throw_error_expected_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertContainsExactlyInAnyOrder(someInfo(), actual,
                                                                                             (int[]) null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContainsExactlyInAnyOrder(someInfo(), null, IntArrays.arrayOf(8)))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_actual_does_not_contain_given_values_exactly() {
    AssertionInfo info = someInfo();
    try {
      arrays.assertContainsExactlyInAnyOrder(info, actual, IntArrays.arrayOf(6, 8, 20));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainExactlyInAnyOrder(actual, arrayOf(6, 8, 20),
          newArrayList((byte) 20), newArrayList((byte) 10), StandardComparisonStrategy.instance()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_all_given_values_but_size_differ() {
    AssertionInfo info = someInfo();
    try {
      arrays.assertContainsExactlyInAnyOrder(info, actual, IntArrays.arrayOf(6, 8));
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainExactlyInAnyOrder(actual, arrayOf(6, 8), emptyList(), newArrayList((byte) 10),
              StandardComparisonStrategy.instance()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_duplicates_and_expected_does_not() {
    AssertionInfo info = someInfo();
    actual = arrayOf(6, 8, 8);
    try {
      arrays.assertContainsExactlyInAnyOrder(info, actual, IntArrays.arrayOf(6, 8));
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainExactlyInAnyOrder(actual, arrayOf(6, 8), emptyList(), newArrayList((byte) 8),
              StandardComparisonStrategy.instance()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_expected_contains_duplicates_and_actual_does_not() {
    AssertionInfo info = someInfo();
    actual = arrayOf(6, 8);
    try {
      arrays.assertContainsExactlyInAnyOrder(info, actual, IntArrays.arrayOf(6, 8, 8));
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainExactlyInAnyOrder(actual, arrayOf(6, 8, 8), newArrayList((byte) 8), emptyList(), StandardComparisonStrategy.instance()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  public void should_pass_if_actual_contains_given_values_exactly_in_any_order_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(someInfo(), actual, IntArrays.arrayOf(6, -8, 10));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_exactly_in_different_order_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(someInfo(), actual, IntArrays.arrayOf(-6, 10, 8));
  }

  @Test
  public void should_fail_if_expected_is_empty_and_actual_is_not_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(someInfo(), actual, IntArrays.emptyArray()));
  }

  @Test
  public void should_throw_error_if_expected_is_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(someInfo(),
                                                                                                                                              actual,
                                                                                                                                              (int[]) null))
                                                         .withMessage(valuesToLookForIsNull());
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(someInfo(), null, IntArrays.arrayOf(-8)))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_actual_does_not_contain_given_values_exactly_in_any_order_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    byte[] expected = {6, -8, 20};
    try {
      arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(info, actual, IntArrays.arrayOf(6, -8, 20));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainExactlyInAnyOrder(actual, expected, newArrayList((byte) 20),
          newArrayList((byte) 10), absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_all_given_values_but_size_differ_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    try {
      arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(info, actual, IntArrays.arrayOf(6, 8));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainExactlyInAnyOrder(actual, arrayOf(6, 8), emptyList(), newArrayList((byte) 10), absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_duplicates_and_expected_does_not_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    actual = arrayOf(6, 8, 8);
    try {
      arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(info, actual, IntArrays.arrayOf(6, 8));
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainExactlyInAnyOrder(actual, arrayOf(6, 8), emptyList(), newArrayList((byte) 8), absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_expected_contains_duplicates_and_actual_does_not_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    actual = arrayOf(6, 8);
    try {
      arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(info, actual, IntArrays.arrayOf(6, 8, 8));
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainExactlyInAnyOrder(actual, arrayOf(6, 8, 8), newArrayList((byte) 8), emptyList(), absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
