/*
 * Created on Nov 29, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.internal.objectarrays;

import static org.fest.assertions.error.ShouldContain.shouldContain;
import static org.fest.assertions.test.ErrorMessages.iterableToLookForIsNull;
import static org.fest.assertions.test.FailureMessages.actualIsNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.assertions.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.fest.util.Arrays.array;
import static org.fest.util.Collections.*;

import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.ObjectArrays;
import org.fest.assertions.internal.ObjectArraysBaseTest;

/**
 * Tests for <code>{@link ObjectArrays#assertContainsAll(AssertionInfo, Object[], Iterable)}</code>.
 * 
 * @author Joel Costigliola
 */
public class ObjectArrays_assertContainsAll_Test extends ObjectArraysBaseTest {

  @Test
  public void should_pass_if_actual_contains_all_iterable_values() {
    arrays.assertContainsAll(someInfo(), actual, list("Luke", "Yoda", "Leia"));
    arrays.assertContainsAll(someInfo(), actual, list("Luke", "Yoda"));
    // order is not important
    arrays.assertContainsAll(someInfo(), actual, list("Yoda", "Luke"));
  }

  @Test
  public void should_pass_if_actual_contains_all_iterable_values_more_than_once() {
    actual = array("Luke", "Yoda", "Leia", "Luke", "Luke");
    arrays.assertContainsAll(someInfo(), actual, list("Luke"));
  }

  @Test
  public void should_pass_if_iterable_is_empty() {
    arrays.assertContainsAll(someInfo(), actual, list());
  }

  @Test
  public void should_throw_error_if_iterable_to_look_for_is_null() {
    thrown.expectNullPointerException(iterableToLookForIsNull());
    arrays.assertContainsAll(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    arrays.assertContainsAll(someInfo(), null, list("Yoda"));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_all_iterable_values() {
    AssertionInfo info = someInfo();
    List<String> expected = list("Han", "Luke");
    try {
      arrays.assertContainsAll(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContain(actual, expected.toArray(), set("Han")));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  public void should_pass_if_actual_contains_all_iterable_values_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsAll(someInfo(), actual, list("LUKE"));
  }

  @Test
  public void should_pass_if_actual_contains_all_iterable_values_in_different_order_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsAll(someInfo(), actual, list("LEIa", "YodA"));
  }

  @Test
  public void should_pass_if_actual_contains_all_all_iterable_values_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsAll(someInfo(), actual, list("LukE", "YodA", "LeiA"));
  }

  @Test
  public void should_pass_if_actual_contains_all_iterable_values_more_than_once_according_to_custom_comparison_strategy() {
    actual = array("Luke", "Yoda", "Leia", "Luke", "Luke");
    arraysWithCustomComparisonStrategy.assertContainsAll(someInfo(), actual, list("LUKE"));
  }

  @Test
  public void should_pass_if_actual_contains_all_iterable_values_even_if_duplicated_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsAll(someInfo(), actual, list("LUKE", "LUKE"));
  }

  @Test
  public void should_pass_if_iterable_to_look_for_is_empty_whatever_custom_comparison_strategy_is() {
    arraysWithCustomComparisonStrategy.assertContainsAll(someInfo(), actual, list());
  }

  @Test
  public void should_throw_error_if_iterable_to_look_for_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectNullPointerException(iterableToLookForIsNull());
    arraysWithCustomComparisonStrategy.assertContainsAll(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_actual_does_not_contain_values_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    List<String> expected = list("Han", "LUKE");
    try {
      arraysWithCustomComparisonStrategy.assertContainsAll(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContain(actual, expected.toArray(), set("Han"), caseInsensitiveStringComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
