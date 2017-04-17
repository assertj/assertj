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

import static org.assertj.core.error.ShouldContain.shouldContain;
import static org.assertj.core.internal.ErrorMessages.iterableToLookForIsNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import static org.mockito.Mockito.verify;

import java.util.List;


import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ObjectArrays;
import org.assertj.core.internal.ObjectArraysBaseTest;
import org.junit.Test;

/**
 * Tests for <code>{@link ObjectArrays#assertContainsAll(AssertionInfo, Object[], Iterable)}</code>.
 * 
 * @author Joel Costigliola
 */
public class ObjectArrays_assertContainsAll_Test extends ObjectArraysBaseTest {

  @Test
  public void should_pass_if_actual_contains_all_iterable_values() {
    arrays.assertContainsAll(someInfo(), actual, newArrayList("Luke", "Yoda", "Leia"));
    arrays.assertContainsAll(someInfo(), actual, newArrayList("Luke", "Yoda"));
    // order is not important
    arrays.assertContainsAll(someInfo(), actual, newArrayList("Yoda", "Luke"));
  }

  @Test
  public void should_pass_if_actual_contains_all_iterable_values_more_than_once() {
    actual = array("Luke", "Yoda", "Leia", "Luke", "Luke");
    arrays.assertContainsAll(someInfo(), actual, newArrayList("Luke"));
  }

  @Test
  public void should_pass_if_iterable_is_empty() {
    arrays.assertContainsAll(someInfo(), actual, newArrayList());
  }

  @Test
  public void should_throw_error_if_iterable_to_look_for_is_null() {
    thrown.expectNullPointerException(iterableToLookForIsNull());
    arrays.assertContainsAll(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    arrays.assertContainsAll(someInfo(), null, newArrayList("Yoda"));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_all_iterable_values() {
    AssertionInfo info = someInfo();
    List<String> expected = newArrayList("Han", "Luke");
    try {
      arrays.assertContainsAll(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContain(actual, expected.toArray(), newLinkedHashSet("Han")));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  public void should_pass_if_actual_contains_all_iterable_values_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsAll(someInfo(), actual, newArrayList("LUKE"));
  }

  @Test
  public void should_pass_if_actual_contains_all_iterable_values_in_different_order_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsAll(someInfo(), actual, newArrayList("LEIa", "YodA"));
  }

  @Test
  public void should_pass_if_actual_contains_all_all_iterable_values_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsAll(someInfo(), actual, newArrayList("LukE", "YodA", "LeiA"));
  }

  @Test
  public void should_pass_if_actual_contains_all_iterable_values_more_than_once_according_to_custom_comparison_strategy() {
    actual = array("Luke", "Yoda", "Leia", "Luke", "Luke");
    arraysWithCustomComparisonStrategy.assertContainsAll(someInfo(), actual, newArrayList("LUKE"));
  }

  @Test
  public void should_pass_if_actual_contains_all_iterable_values_even_if_duplicated_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsAll(someInfo(), actual, newArrayList("LUKE", "LUKE"));
  }

  @Test
  public void should_pass_if_iterable_to_look_for_is_empty_whatever_custom_comparison_strategy_is() {
    arraysWithCustomComparisonStrategy.assertContainsAll(someInfo(), actual, newArrayList());
  }

  @Test
  public void should_throw_error_if_iterable_to_look_for_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectNullPointerException(iterableToLookForIsNull());
    arraysWithCustomComparisonStrategy.assertContainsAll(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_actual_does_not_contain_values_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    List<String> expected = newArrayList("Han", "LUKE");
    try {
      arraysWithCustomComparisonStrategy.assertContainsAll(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContain(actual, expected.toArray(), newLinkedHashSet("Han"), caseInsensitiveStringComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
