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

import static org.fest.assertions.error.ShouldNotContain.shouldNotContain;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.assertions.test.ErrorMessages.*;
import static org.fest.assertions.test.ObjectArrays.emptyArray;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.fest.util.Arrays.array;
import static org.fest.util.Sets.newLinkedHashSet;

import static org.mockito.Mockito.verify;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.ObjectArrays;
import org.fest.assertions.internal.ObjectArraysBaseTest;

/**
 * Tests for <code>{@link ObjectArrays#assertDoesNotContain(AssertionInfo, Object[], Object[])}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class ObjectArrays_assertDoesNotContain_Test extends ObjectArraysBaseTest {

  @Test
  public void should_pass_if_actual_does_not_contain_given_values() {
    arrays.assertDoesNotContain(someInfo(), actual, array("Han"));
  }

  @Test
  public void should_pass_if_actual_does_not_contain_given_values_even_if_duplicated() {
    arrays.assertDoesNotContain(someInfo(), actual, array("Han", "Han", "Anakin"));
  }

  @Test
  public void should_throw_error_if_array_of_values_to_look_for_is_empty() {
    thrown.expectIllegalArgumentException(valuesToLookForIsEmpty());
    arrays.assertDoesNotContain(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_throw_error_if_array_of_values_to_look_for_is_null() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    arrays.assertDoesNotContain(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    arrays.assertDoesNotContain(someInfo(), null, array("Yoda"));
  }

  @Test
  public void should_fail_if_actual_contains_given_values() {
    AssertionInfo info = someInfo();
    Object[] expected = { "Luke", "Yoda", "Han" };
    try {
      arrays.assertDoesNotContain(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotContain(actual, expected, newLinkedHashSet("Luke", "Yoda")));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_does_not_contain_given_values_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertDoesNotContain(someInfo(), actual, array("Han"));
  }

  @Test
  public void should_pass_if_actual_does_not_contain_given_values_even_if_duplicated_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertDoesNotContain(someInfo(), actual, array("Han", "HAn", "Anakin"));
  }

  @Test
  public void should_throw_error_if_array_of_values_to_look_for_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    arraysWithCustomComparisonStrategy.assertDoesNotContain(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_actual_contains_given_values_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Object[] expected = { "LUKE", "Yoda", "Han" };
    try {
      arraysWithCustomComparisonStrategy.assertDoesNotContain(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldNotContain(actual, expected, newLinkedHashSet("LUKE", "Yoda"), caseInsensitiveStringComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
