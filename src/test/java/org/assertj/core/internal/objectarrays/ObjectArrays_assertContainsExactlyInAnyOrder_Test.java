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
package org.assertj.core.internal.objectarrays;

import org.assertj.core.api.*;
import org.assertj.core.internal.*;
import org.junit.*;

import static org.assertj.core.error.ShouldContainExactlyInAnyOrder.*;
import static org.assertj.core.internal.ErrorMessages.*;
import static org.assertj.core.test.TestData.*;
import static org.assertj.core.test.TestFailures.*;
import static org.assertj.core.util.Arrays.*;
import static org.assertj.core.util.FailureMessages.*;
import static org.assertj.core.util.Lists.*;
import static org.mockito.Mockito.*;

/**
 * Tests for <code>{@link Iterables#assertContainsExactlyInAnyOrder(AssertionInfo, Iterable, Object[])}</code>.
 *
 * @author Lovro Pandzic
 */
public class ObjectArrays_assertContainsExactlyInAnyOrder_Test extends ObjectArraysBaseTest {

  @Test
  public void should_pass_if_actual_contains_exactly_in_any_order_given_values() {
    arrays.assertContainsExactlyInAnyOrder(someInfo(), actual, array("Leia", "Yoda", "Luke"));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_exactly_in_any_order_with_null_elements() {
    actual = array("Luke", "Yoda", "Leia", null);
    arrays.assertContainsExactlyInAnyOrder(someInfo(), actual, array("Leia", null, "Yoda", "Luke"));
  }

  @Test
  public void should_pass_if_actual_and_given_values_are_empty() {
    arrays.assertContainsExactlyInAnyOrder(someInfo(), array(), array());
  }

  @Test
  public void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    thrown.expectAssertionError();
    arrays.assertContainsExactlyInAnyOrder(someInfo(), actual, array());
  }

  @Test
  public void should_fail_if_arrays_have_different_sizes() {
    thrown.expectAssertionError();
    arrays.assertContainsExactlyInAnyOrder(someInfo(), actual, array("Luke", "Yoda"));
  }

  @Test
  public void should_throw_error_if_array_of_values_to_look_for_is_null() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    arrays.assertContainsExactlyInAnyOrder(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    arrays.assertContainsExactlyInAnyOrder(someInfo(), null, array("Yoda"));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_given_values_exactly_in_any_order() {
    AssertionInfo info = someInfo();
    Object[] expected = {"Luke", "Yoda", "Han"};
    try {
      arrays.assertContainsExactlyInAnyOrder(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainExactlyInAnyOrder(actual, expected, newArrayList("Han"), newArrayList("Leia"), StandardComparisonStrategy.instance()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_duplicates_and_expected_does_not() {
    AssertionInfo info = someInfo();
    actual = array("Luke", "Leia", "Luke");
    Object[] expected = {"Luke", "Leia"};
    try {
      arrays.assertContainsExactlyInAnyOrder(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainExactlyInAnyOrder(actual, expected, emptyList(), newArrayList("Luke"), StandardComparisonStrategy.instance()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_expected_contains_duplicates_and_actual_does_not() {
    AssertionInfo info = someInfo();
    actual = array("Luke", "Leia");
    Object[] expected = {"Luke", "Leia", "Luke"};
    try {
      arrays.assertContainsExactlyInAnyOrder(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainExactlyInAnyOrder(actual, expected, newArrayList("Luke"), emptyList(), StandardComparisonStrategy.instance()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  public void should_pass_if_actual_contains_given_values_exactly_in_any_order_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(someInfo(), actual,
        array("LUKE", "YODA", "Leia"));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_given_values_exactly_in_any_order_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Object[] expected = {"Luke", "Yoda", "Han"};
    try {
      arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainExactlyInAnyOrder(actual, expected, newArrayList("Han"), newArrayList("Leia"),
          caseInsensitiveStringComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_duplicates_and_expected_does_not_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    actual = array("Luke", "Leia", "Luke");
    Object[] expected = {"Luke", "Leia"};
    try {
      arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainExactlyInAnyOrder(actual, expected, emptyList(), newArrayList("Luke"), caseInsensitiveStringComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_expected_contains_duplicates_and_actual_does_not_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    actual = array("Luke", "Leia");
    Object[] expected = {"Luke", "Leia", "Luke"};
    try {
      arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainExactlyInAnyOrder(actual, expected, newArrayList("Luke"), emptyList(), caseInsensitiveStringComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
