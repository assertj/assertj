/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.internal;

import static org.assertj.core.error.ShouldContainAnyOf.shouldContainAnyOf;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.test.ObjectArrays.emptyArray;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.junit.Test;

public class Arrays_containsAnyOf_Test extends BaseArraysTest {

  @Test
  public void should_pass_if_actual_contains_given_values() {
    arrays.assertContainsAnyOf(someInfo(), failures, actual, array("Luke"));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_in_different_order() {
    arrays.assertContainsAnyOf(someInfo(), failures, actual, array("Leia", "Yoda"));
  }

  @Test
  public void should_pass_if_actual_contains_all_given_values() {
    arrays.assertContainsAnyOf(someInfo(), failures, actual, array("Luke", "Yoda", "Leia"));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_more_than_once() {
    actual = array("Luke", "Yoda", "Leia", "Luke", "Luke");
    arrays.assertContainsAnyOf(someInfo(), failures, actual, array("Luke"));
  }

  @Test
  public void should_pass_if_actual_contains_just_one_of_given_values() {
    arrays.assertContainsAnyOf(someInfo(), failures, actual, array("Luke", "John", "Tom"));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_even_if_duplicated() {
    arrays.assertContainsAnyOf(someInfo(), failures, actual, array("Luke", "Luke"));
  }

  @Test
  public void should_pass_if_actual_and_given_values_are_empty() {
    actual = new String[0];
    arrays.assertContainsAnyOf(someInfo(), failures, actual, array());
  }

  @Test
  public void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    thrown.expectAssertionError();
    arrays.assertContainsAnyOf(someInfo(), failures, actual, emptyArray());
  }

  @Test
  public void should_throw_error_if_array_of_values_to_look_for_is_null() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    arrays.assertContainsAnyOf(someInfo(), failures, actual, null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    arrays.assertContainsAnyOf(someInfo(), failures, null, array("Yoda"));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_any_of_the_given_values() {
    AssertionInfo info = someInfo();
    Object[] expected = { "Han", "John" };
    try {
      arrays.assertContainsAnyOf(info, failures, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainAnyOf(actual, expected));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  public void should_pass_if_actual_contains_given_values_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsAnyOf(someInfo(), failures, actual, array("LUKE"));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_in_different_order_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsAnyOf(someInfo(), failures, actual, array("LEIA", "yODa"));
  }

  @Test
  public void should_pass_if_actual_contains_all_given_values_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsAnyOf(someInfo(), failures, actual,
                                                           array("luke", "YODA", "leia"));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_more_than_once_according_to_custom_comparison_strategy() {
    actual = array("Luke", "Yoda", "Leia", "Luke", "Luke");
    arraysWithCustomComparisonStrategy.assertContainsAnyOf(someInfo(), failures, actual, array("LUke"));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_even_if_duplicated_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsAnyOf(someInfo(), failures, actual, array("LUke", "LuKe"));
  }

  @Test
  public void should_pass_if_actual_contains_just_one_of_given_values_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsAnyOf(someInfo(), failures, actual,
                                                           array("LuKe", "JoHn", "ToM"));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_values_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Object[] expected = { "Han", "John" };
    try {
      arraysWithCustomComparisonStrategy.assertContainsAnyOf(info, failures, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainAnyOf(actual, expected, caseInsensitiveStringComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
