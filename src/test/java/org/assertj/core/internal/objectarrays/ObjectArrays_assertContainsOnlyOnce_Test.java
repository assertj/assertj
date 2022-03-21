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
import static org.assertj.core.error.ShouldContainsOnlyOnce.shouldContainsOnlyOnce;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.test.ObjectArrays.emptyArray;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.Mockito.verify;

import java.awt.*;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ObjectArrays;
import org.assertj.core.internal.ObjectArraysBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ObjectArrays#assertContainsOnlyOnce(AssertionInfo, Object[], Object[])}</code>.
 * 
 * @author William Delanoue
 */
class ObjectArrays_assertContainsOnlyOnce_Test extends ObjectArraysBaseTest {

  @Test
  void should_pass_if_actual_contains_given_values_only_once() {
    arrays.assertContainsOnlyOnce(someInfo(), actual, array("Luke", "Yoda", "Leia"));
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_once_even_if_actual_type_is_not_comparable() {
    // Rectangle class does not implement Comparable
    Rectangle r1 = new Rectangle(1, 1);
    Rectangle r2 = new Rectangle(2, 2);
    arrays.assertContainsOnlyOnce(someInfo(), array(r1, r2, r2), array(r1));
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_in_different_order() {
    arrays.assertContainsOnlyOnce(someInfo(), actual, array("Leia", "Yoda", "Luke"));
  }

  @Test
  void should_fail_if_actual_contains_given_values_more_than_once() {
    AssertionInfo info = someInfo();
    actual = array("Luke", "Yoda", "Han", "Luke", "Yoda", "Han", "Yoda", "Luke");
    String[] expected = { "Luke", "Yoda", "Leia" };

    Throwable error = catchThrowable(() -> arrays.assertContainsOnlyOnce(info, actual, expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info,
                             shouldContainsOnlyOnce(actual, expected, newLinkedHashSet("Leia"),
                                                    newLinkedHashSet("Luke", "Yoda")));
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_once_even_if_duplicated() {
    arrays.assertContainsOnlyOnce(someInfo(), actual, array("Luke", "Yoda", "Leia", "Luke", "Yoda", "Leia"));
  }

  @Test
  void should_pass_if_actual_and_given_values_are_empty() {
    actual = new String[] {};
    arrays.assertContainsOnlyOnce(someInfo(), actual, emptyArray());
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContainsOnlyOnce(someInfo(), actual,
                                                                                                   emptyArray()));
  }

  @Test
  void should_throw_error_if_array_of_values_to_look_for_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertContainsOnlyOnce(someInfo(), actual, null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContainsOnlyOnce(someInfo(), null,
                                                                                                   array("Yoda")))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_all_given_values() {
    AssertionInfo info = someInfo();
    String[] expected = { "Luke", "Yoda", "Han" };

    Throwable error = catchThrowable(() -> arrays.assertContainsOnlyOnce(info, actual, expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info,
                             shouldContainsOnlyOnce(actual, expected, newLinkedHashSet("Han"), newLinkedHashSet()));
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(someInfo(), actual, array("Luke", "yoda", "Leia"));
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_in_different_order_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(someInfo(), actual, array("Leia", "yoda", "Luke"));
  }

  @Test
  void should_fail_if_actual_contains_given_values_more_than_once_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    actual = array("Luke", "yODA", "Han", "luke", "yoda", "Han", "YodA");
    String[] expected = { "Luke", "yOda", "Leia" };

    Throwable error = catchThrowable(() -> arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(info, actual, expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(
                             info,
                             shouldContainsOnlyOnce(actual, expected, newLinkedHashSet("Leia"), newLinkedHashSet("Luke", "yOda"),
                                                    caseInsensitiveStringComparisonStrategy));
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_once_according_to_custom_comparison_strategy_even_if_duplicated_() {
    arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(someInfo(), actual,
                                                              array("Luke", "Yoda", "Leia", "Luke", "yODA", "LeiA"));
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(someInfo(),
                                                                                                                               actual,
                                                                                                                               emptyArray()));
  }

  @Test
  void should_throw_error_if_array_of_values_to_look_for_is_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(someInfo(),
                                                                                                                actual,
                                                                                                                null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(someInfo(),
                                                                                                                               null,
                                                                                                                               array("yoda")))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_all_given_values_only_once_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    String[] expected = { "Luke", "yoda", "han" };

    Throwable error = catchThrowable(() -> arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(info, actual, expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(
                             info,
                             shouldContainsOnlyOnce(actual, expected, newLinkedHashSet("han"), newLinkedHashSet(),
                                                    caseInsensitiveStringComparisonStrategy));
  }
}
