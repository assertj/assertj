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
package org.assertj.core.internal.iterables;

import static org.assertj.core.error.ShouldContainAnyOf.shouldContainAnyOf;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.test.ObjectArrays.emptyArray;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.IterablesBaseTest;
import org.junit.Test;

/**
 * Tests for <code>{@link Iterables#assertContainsAnyOf(AssertionInfo, Iterable, Object[])} </code>.
 *
 * @author Marko Bekhta
 */
public class Iterables_assertContainsAnyOf_Test extends IterablesBaseTest {

  @Test
  public void should_pass_if_actual_contains_given_values() {
    iterables.assertContainsAnyOf(someInfo(), actual, array("Luke"));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_in_different_order() {
    iterables.assertContainsAnyOf(someInfo(), actual, array("Leia", "Yoda"));
  }

  @Test
  public void should_pass_if_actual_contains_all_given_values() {
    iterables.assertContainsAnyOf(someInfo(), actual, array("Luke", "Yoda", "Leia"));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_more_than_once() {
    actual.addAll(newArrayList("Luke", "Luke"));
    iterables.assertContainsAnyOf(someInfo(), actual, array("Luke"));
  }

  @Test
  public void should_pass_if_actual_contains_just_one_of_given_values() {
    iterables.assertContainsAnyOf(someInfo(), actual, array("Luke", "John", "Tom"));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_even_if_duplicated() {
    iterables.assertContainsAnyOf(someInfo(), actual, array("Luke", "Luke"));
  }

  @Test
  public void should_pass_if_actual_and_given_values_are_empty() {
    actual.clear();
    iterables.assertContainsAnyOf(someInfo(), actual, array());
  }

  @Test
  public void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    thrown.expectAssertionError();
    iterables.assertContainsAnyOf(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_throw_error_if_array_of_values_to_look_for_is_null() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    iterables.assertContainsAnyOf(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    iterables.assertContainsAnyOf(someInfo(), null, array("Yoda"));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_any_of_the_given_values() {
    AssertionInfo info = someInfo();
    Object[] expected = { "Han", "John" };
    try {
      iterables.assertContainsAnyOf(info, actual, expected);
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
    iterablesWithCaseInsensitiveComparisonStrategy.assertContainsAnyOf(someInfo(), actual, array("LUKE"));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_in_different_order_according_to_custom_comparison_strategy() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertContainsAnyOf(someInfo(), actual, array("LEIA", "yODa"));
  }

  @Test
  public void should_pass_if_actual_contains_all_given_values_according_to_custom_comparison_strategy() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertContainsAnyOf(someInfo(), actual,
                                                                       array("luke", "YODA", "leia"));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_more_than_once_according_to_custom_comparison_strategy() {
    actual.addAll(newArrayList("Luke", "Luke"));
    iterablesWithCaseInsensitiveComparisonStrategy.assertContainsAnyOf(someInfo(), actual, array("LUke"));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_even_if_duplicated_according_to_custom_comparison_strategy() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertContainsAnyOf(someInfo(), actual, array("LUke", "LuKe"));
  }

  @Test
  public void should_pass_if_actual_contains_just_one_of_given_values_according_to_custom_comparison_strategy() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertContainsAnyOf(someInfo(), actual,
                                                                       array("LuKe", "JoHn", "ToM"));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_values_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Object[] expected = { "Han", "John" };
    try {
      iterablesWithCaseInsensitiveComparisonStrategy.assertContainsAnyOf(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainAnyOf(actual, expected, comparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
