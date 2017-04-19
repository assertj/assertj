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

import static org.assertj.core.error.ShouldBeSubsetOf.shouldBeSubsetOf;
import static org.assertj.core.internal.ErrorMessages.iterableToLookForIsNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ObjectArraysBaseTest;
import org.junit.Test;


public class ObjectArrays_assertIsSubsetOf_Test extends ObjectArraysBaseTest {

  @Test
  public void should_pass_if_actual_is_subset_of_set() {
    actual = array("Yoda", "Luke");
    arrays.assertIsSubsetOf(someInfo(), actual, newArrayList("Luke", "Yoda", "Obi-Wan"));
  }

  @Test
  public void should_pass_if_actual_has_the_same_elements_as_set() {
    actual = array("Yoda", "Luke");
    arrays.assertIsSubsetOf(someInfo(), actual, newArrayList("Luke", "Yoda"));
  }

  @Test
  public void should_pass_if_actual_is_empty() {
    actual = new String[0];
    arrays.assertIsSubsetOf(someInfo(), actual, newArrayList("Luke", "Yoda"));
  }

  @Test
  public void should_pass_if_actual_and_set_are_both_empty() {
    actual = new String[0];
    arrays.assertIsSubsetOf(someInfo(), actual, newArrayList());
  }

  @Test
  public void should_pass_if_actual_has_duplicates_but_all_elements_are_in_values() {
    actual = array("Yoda", "Yoda");
    arrays.assertIsSubsetOf(someInfo(), actual, newArrayList("Yoda"));
  }

  @Test
  public void should_pass_if_values_has_duplicates_but_all_elements_are_in_values() {
    actual = array("Yoda", "C-3PO");
    arrays.assertIsSubsetOf(someInfo(), actual, newArrayList("Yoda", "Yoda", "C-3PO"));
  }

  @Test
  public void should_pass_if_both_actual_and_values_have_duplicates_but_all_elements_are_in_values() {
    actual = array("Yoda", "Yoda", "Yoda", "C-3PO", "Obi-Wan");
    arrays.assertIsSubsetOf(someInfo(), actual, newArrayList("Yoda", "Yoda", "C-3PO", "C-3PO", "Obi-Wan"));
  }

  @Test
  public void should_throw_error_if_set_is_null() {
    actual = array("Yoda", "Luke");
    thrown.expectNullPointerException(iterableToLookForIsNull());
    arrays.assertIsSubsetOf(someInfo(), actual, null);
  }

  @Test
  public void should_throw_error_if_actual_is_null() {
    actual = null;
    thrown.expectAssertionError(actualIsNull());
    arrays.assertIsSubsetOf(someInfo(), actual, newArrayList());
  }

  @Test
  public void should_fail_if_actual_is_not_subset_of_values() {
    AssertionInfo info = someInfo();
    actual = array("Yoda");
    List<String> values = newArrayList("C-3PO", "Leila");
    List<String> extra = newArrayList("Yoda");
    try {
      arrays.assertIsSubsetOf(info, actual, values);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeSubsetOf(actual, values, extra));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  public void should_pass_if_actual_is_subset_of_values_according_to_custom_comparison_strategy() {
    actual = array("Yoda", "Luke");
    arraysWithCustomComparisonStrategy.assertIsSubsetOf(someInfo(), actual, newArrayList("yoda", "lUKE"));
  }

  @Test
  public void should_pass_if_actual_contains_duplicates_according_to_custom_comparison_strategy() {
    actual = array("Luke", "Luke");
    arraysWithCustomComparisonStrategy.assertIsSubsetOf(someInfo(), actual, newArrayList("LUke", "yoda"));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_even_if_duplicated_according_to_custom_comparison_strategy() {
    actual = array("Yoda", "Luke");
    arraysWithCustomComparisonStrategy.assertIsSubsetOf(someInfo(), actual, newArrayList("LUke", "LuKe", "yoda"));
  }

  @Test
  public void should_fail_if_actual_is_not_subset_of_values_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    actual = array("Yoda", "Luke");
    List<String> values = newArrayList("yoda", "C-3PO");
    List<String> extra = newArrayList("Luke");
    try {
      arraysWithCustomComparisonStrategy.assertIsSubsetOf(info, actual, values);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeSubsetOf(actual, values, extra, caseInsensitiveStringComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
