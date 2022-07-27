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
package org.assertj.core.internal.iterables;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContain.shouldContain;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.internal.iterables.SinglyIterableFactory.createSinglyIterable;
import static org.assertj.core.test.ObjectArrays.emptyArray;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.Mockito.verify;

import java.util.HashSet;
import java.util.Set;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.IterablesBaseTest;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Iterables#assertContains(AssertionInfo, Iterable, Object[])}</code>.
 *
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class Iterables_assertContains_Test extends IterablesBaseTest {

  @Test
  void should_pass_if_actual_contains_given_values() {
    iterables.assertContains(someInfo(), actual, array("Luke"));
  }

  @Test
  void should_pass_if_actual_contains_given_values_in_different_order() {
    iterables.assertContains(someInfo(), actual, array("Leia", "Yoda"));
  }

  @Test
  void should_pass_if_actual_contains_all_given_values() {
    iterables.assertContains(someInfo(), actual, array("Luke", "Yoda"));
  }

  @Test
  void should_pass_if_actual_contains_given_values_more_than_once() {
    actual.addAll(newArrayList("Luke", "Luke"));
    iterables.assertContains(someInfo(), actual, array("Luke"));
  }

  @Test
  void should_pass_if_actual_contains_given_values_even_if_duplicated() {
    iterables.assertContains(someInfo(), actual, array("Luke", "Luke"));
  }

  @Test
  void should_pass_if_actual_and_given_values_are_empty() {
    actual.clear();
    iterables.assertContains(someInfo(), actual, array());
  }

  @Test
  void should_pass_if_non_restartable_actual_contains_given_values() {
    iterables.assertContains(someInfo(), createSinglyIterable(actual), array("Luke", "Yoda", "Leia"));
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> iterables.assertContains(someInfo(), actual, emptyArray()));
  }

  @Test
  void should_throw_error_if_array_of_values_to_look_for_is_null() {
    assertThatNullPointerException().isThrownBy(() -> iterables.assertContains(someInfo(), actual, null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> iterables.assertContains(someInfo(), null, array("Yoda")))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_values() {
    // GIVEN
    AssertionInfo info = someInfo();
    Object[] expected = { "Han", "Luke" };
    // WHEN
    AssertionError error = expectAssertionError(() -> iterables.assertContains(info, actual, expected));
    // THEN
    then(error).hasMessageContaining("Expecting ArrayList:");
    verify(failures).failure(info, shouldContain(actual, expected, newLinkedHashSet("Han")));
  }

  @Test
  void should_fail_with_the_right_actual_type() {
    // GIVEN
    AssertionInfo info = someInfo();
    Object[] expected = { "Han", "Luke" };
    Set<String> actualSet = new HashSet<>(actual);
    // WHEN
    AssertionError error = expectAssertionError(() -> iterables.assertContains(info, actualSet, expected));
    // THEN
    then(error).hasMessageContaining("Expecting HashSet:");
    verify(failures).failure(info, shouldContain(HashSet.class, newArrayList(actualSet), expected, newLinkedHashSet("Han"),
                                                 StandardComparisonStrategy.instance()));
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  void should_pass_if_actual_contains_given_values_according_to_custom_comparison_strategy() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertContains(someInfo(), actual, array("LUKE"));
  }

  @Test
  void should_pass_if_actual_contains_given_values_in_different_order_according_to_custom_comparison_strategy() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertContains(someInfo(), actual, array("LEIA", "yODa"));
  }

  @Test
  void should_pass_if_actual_contains_all_given_values_according_to_custom_comparison_strategy() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertContains(someInfo(), actual, array("luke", "YODA"));
  }

  @Test
  void should_pass_if_actual_contains_given_values_more_than_once_according_to_custom_comparison_strategy() {
    actual.addAll(newArrayList("Luke", "Luke"));
    iterablesWithCaseInsensitiveComparisonStrategy.assertContains(someInfo(), actual, array("LUke"));
  }

  @Test
  void should_pass_if_actual_contains_given_values_even_if_duplicated_according_to_custom_comparison_strategy() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertContains(someInfo(), actual, array("LUke", "LuKe"));
  }

  @Test
  void should_fail_if_actual_does_not_contain_values_according_to_custom_comparison_strategy() {
    // GIVEN
    AssertionInfo info = someInfo();
    Object[] expected = { "Han", "Luke" };
    // WHEN
    AssertionError error = expectAssertionError(() -> iterablesWithCaseInsensitiveComparisonStrategy.assertContains(info, actual,
                                                                                                                    expected));
    // THEN
    then(error).hasMessageContaining("Expecting ArrayList:");
    verify(failures).failure(info, shouldContain(actual, expected, newLinkedHashSet("Han"), comparisonStrategy));
  }

}
