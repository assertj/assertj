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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.internal.iterables;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContainOnly.shouldContainOnly;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.internal.iterables.SinglyIterableFactory.createSinglyIterable;
import static org.assertj.core.testkit.ObjectArrays.emptyArray;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.Lists.list;
import static org.mockito.Mockito.verify;

import java.util.Collection;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.IterablesBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Iterables#assertContainsOnly(AssertionInfo, Collection, Object[])}</code>.
 *
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class Iterables_assertContainsOnly_Test extends IterablesBaseTest {

  @Test
  void should_pass_if_actual_contains_given_values_only() {
    iterables.assertContainsOnly(someInfo(), actual, array("Luke", "Yoda", "Leia"));
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_with_null_elements() {
    actual.add(null);
    actual.add(null);
    iterables.assertContainsOnly(someInfo(), actual, array("Luke", null, "Yoda", "Leia", null));
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_in_different_order() {
    iterables.assertContainsOnly(someInfo(), actual, array("Leia", "Yoda", "Luke"));
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_more_than_once() {
    actual.addAll(list("Luke", "Luke"));
    iterables.assertContainsOnly(someInfo(), actual, array("Luke", "Yoda", "Leia"));
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_even_if_duplicated() {
    iterables.assertContainsOnly(someInfo(), actual, array("Luke", "Luke", "Luke", "Yoda", "Leia"));
  }

  @Test
  void should_pass_if_actual_and_given_values_are_empty() {
    actual.clear();
    iterables.assertContainsOnly(someInfo(), actual, emptyArray());
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    // GIVEN
    Object[] expected = array("Luke", "Yoda", "Leia");
    actual.clear();
    // WHEN
    expectAssertionError(() -> iterables.assertContainsOnly(someInfo(), actual, expected));
    // THEN
    verify(failures).failure(info, shouldContainOnly(actual, expected, list("Luke", "Yoda", "Leia"), emptyList()));
  }

  @Test
  void should_fail_if_actual_is_empty_and_array_of_values_to_look_for_is_not() {
    // GIVEN
    Object[] expected = emptyArray();
    // WHEN
    expectAssertionError(() -> iterables.assertContainsOnly(someInfo(), actual, expected));
    // THEN
    verify(failures).failure(info, shouldContainOnly(actual, expected, emptyList(), list("Luke", "Yoda", "Leia")));
  }

  @Test
  void should_throw_error_if_array_of_values_to_look_for_is_null() {
    // GIVEN
    Object[] expected = null;
    // WHEN
    NullPointerException npe = catchNullPointerException(() -> iterables.assertContainsOnly(someInfo(), actual, expected));
    // THEN
    then(npe).hasMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> iterables.assertContainsOnly(someInfo(), null, array("Yoda")));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_actual_does_not_contain_all_given_values() {
    // GIVEN
    Object[] expected = { "Luke", "Yoda", "Han" };
    // WHEN
    expectAssertionError(() -> iterables.assertContainsOnly(info, actual, expected));
    // THEN
    verify(failures).failure(info, shouldContainOnly(actual, expected, list("Han"), list("Leia")));
  }

  @Test
  void should_fail_if_actual_contains_additional_elements() {
    // GIVEN
    Object[] expected = { "Luke", "Yoda" };
    // WHEN
    expectAssertionError(() -> iterables.assertContainsOnly(info, actual, expected));
    // THEN
    verify(failures).failure(info, shouldContainOnly(actual, expected, emptyList(), list("Leia")));
  }

  @Test
  void should_fail_if_actual_contains_a_subset_of_expected_elements() {
    // GIVEN
    Object[] expected = { "Luke", "Yoda", "Obiwan", "Leia" };
    // WHEN
    expectAssertionError(() -> iterables.assertContainsOnly(info, actual, expected));
    // THEN
    verify(failures).failure(info, shouldContainOnly(actual, expected, list("Obiwan"), emptyList()));
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  void should_pass_if_actual_contains_given_values_only_according_to_custom_comparison_strategy() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertContainsOnly(someInfo(), actual, array("LUKE", "YODA", "Leia"));
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_in_different_order_according_to_custom_comparison_strategy() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertContainsOnly(someInfo(), actual, array("LEIA", "yoda", "LukE"));
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_more_than_once_according_to_custom_comparison_strategy() {
    actual.addAll(list("Luke", "Luke"));
    iterablesWithCaseInsensitiveComparisonStrategy.assertContainsOnly(someInfo(), actual, array("luke", "YOda", "LeIA"));
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_even_if_duplicated_according_to_custom_comparison_strategy() {
    actual.addAll(list("LUKE"));
    iterablesWithCaseInsensitiveComparisonStrategy.assertContainsOnly(someInfo(), actual,
                                                                      array("LUke", "LUKE", "lukE", "YOda", "Leia"));
  }

  @Test
  void should_fail_if_actual_does_not_contain_given_values_only_according_to_custom_comparison_strategy() {
    // GIVEN
    Object[] expected = { "Luke", "Yoda", "Han" };
    // WHEN
    expectAssertionError(() -> iterablesWithCaseInsensitiveComparisonStrategy.assertContainsOnly(info, actual, expected));
    // THEN
    verify(failures).failure(info, shouldContainOnly(actual, expected, list("Han"), list("Leia"), comparisonStrategy));
  }

  @Test
  void should_pass_if_nonrestartable_actual_contains_only_given_values() {
    iterables.assertContainsOnly(someInfo(), createSinglyIterable(actual), array("Luke", "Yoda", "Leia"));
  }
}
