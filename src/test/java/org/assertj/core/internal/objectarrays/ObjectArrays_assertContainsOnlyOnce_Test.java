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

import static org.assertj.core.error.ShouldContainsOnlyOnce.shouldContainsOnlyOnce;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.test.ObjectArrays.emptyArray;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.Mockito.verify;

import java.awt.Rectangle;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ObjectArrays;
import org.assertj.core.internal.ObjectArraysBaseTest;
import org.junit.Test;

/**
 * Tests for <code>{@link ObjectArrays#assertContainsOnlyOnce(AssertionInfo, Object[], Object[])}</code>.
 * 
 * @author William Delanoue
 */
public class ObjectArrays_assertContainsOnlyOnce_Test extends ObjectArraysBaseTest {

  @Test
  public void should_pass_if_actual_contains_given_values_only_once() {
    arrays.assertContainsOnlyOnce(someInfo(), actual, array("Luke", "Yoda", "Leia"));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_only_once_even_if_actual_type_is_not_comparable() {
    // Rectangle class does not implement Comparable
    Rectangle r1 = new Rectangle(1, 1);
    Rectangle r2 = new Rectangle(2, 2);
    arrays.assertContainsOnlyOnce(someInfo(), array(r1, r2, r2), array(r1));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_only_in_different_order() {
    arrays.assertContainsOnlyOnce(someInfo(), actual, array("Leia", "Yoda", "Luke"));
  }

  @Test
  public void should_fail_if_actual_contains_given_values_more_than_once() {
    AssertionInfo info = someInfo();
    actual = array("Luke", "Yoda", "Han", "Luke", "Yoda", "Han", "Yoda", "Luke");
    String[] expected = { "Luke", "Yoda", "Leia" };
    try {
      arrays.assertContainsOnlyOnce(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainsOnlyOnce(actual, expected, newLinkedHashSet("Leia"), newLinkedHashSet("Luke", "Yoda")));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_contains_given_values_only_once_even_if_duplicated() {
    arrays.assertContainsOnlyOnce(someInfo(), actual, array("Luke", "Yoda", "Leia", "Luke", "Yoda", "Leia"));
  }

  @Test
  public void should_pass_if_actual_and_given_values_are_empty() {
    actual = new String[] {};
    arrays.assertContainsOnlyOnce(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    thrown.expectAssertionError();
    arrays.assertContainsOnlyOnce(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_throw_error_if_array_of_values_to_look_for_is_null() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    arrays.assertContainsOnlyOnce(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    arrays.assertContainsOnlyOnce(someInfo(), null, array("Yoda"));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_all_given_values() {
    AssertionInfo info = someInfo();
    String[] expected = { "Luke", "Yoda", "Han" };
    try {
      arrays.assertContainsOnlyOnce(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainsOnlyOnce(actual, expected, newLinkedHashSet("Han"), newLinkedHashSet()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_contains_given_values_only_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(someInfo(), actual, array("Luke", "yoda", "Leia"));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_only_in_different_order_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(someInfo(), actual, array("Leia", "yoda", "Luke"));
  }

  @Test
  public void should_fail_if_actual_contains_given_values_more_than_once_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    actual = array("Luke", "yODA", "Han", "luke", "yoda", "Han", "YodA");
    String[] expected = { "Luke", "yOda", "Leia" };
    try {
      arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(
          info,
          shouldContainsOnlyOnce(actual, expected, newLinkedHashSet("Leia"), newLinkedHashSet("Luke", "yOda"),
              caseInsensitiveStringComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_contains_given_values_only_once_according_to_custom_comparison_strategy_even_if_duplicated_() {
    arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(someInfo(), actual,
        array("Luke", "Yoda", "Leia", "Luke", "yODA", "LeiA"));
  }

  @Test
  public void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError();
    arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_throw_error_if_array_of_values_to_look_for_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(someInfo(), null, array("yoda"));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_all_given_values_only_once_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    String[] expected = { "Luke", "yoda", "han" };
    try {
      arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(
          info,
          shouldContainsOnlyOnce(actual, expected, newLinkedHashSet("han"), newLinkedHashSet(),
              caseInsensitiveStringComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
