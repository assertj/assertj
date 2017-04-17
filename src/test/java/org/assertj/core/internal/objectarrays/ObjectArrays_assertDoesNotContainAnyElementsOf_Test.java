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

import static org.assertj.core.error.ShouldNotContain.shouldNotContain;
import static org.assertj.core.internal.ErrorMessages.iterableValuesToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.iterableValuesToLookForIsNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ObjectArraysBaseTest;
import org.junit.Test;

public class ObjectArrays_assertDoesNotContainAnyElementsOf_Test extends ObjectArraysBaseTest {

  @Test
  public void should_pass_if_actual_does_not_contain_any_elements_of_given_iterable() {
	arrays.assertDoesNotContainAnyElementsOf(someInfo(), actual, newArrayList("Han"));
  }

  @Test
  public void should_pass_if_actual_does_not_contain_any_elements_of_given_iterable_even_if_duplicated() {
	arrays.assertDoesNotContainAnyElementsOf(someInfo(), actual, newArrayList("Han", "Han", "Anakin"));
  }

  @Test
  public void should_throw_error_if_given_iterable_is_empty() {
	thrown.expectIllegalArgumentException(iterableValuesToLookForIsEmpty());
	arrays.assertDoesNotContainAnyElementsOf(someInfo(), actual, Collections.<String> emptyList());
  }

  @Test
  public void should_throw_error_if_given_iterable_is_null() {
	thrown.expectNullPointerException(iterableValuesToLookForIsNull());
	arrays.assertDoesNotContainAnyElementsOf(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
	thrown.expectAssertionError(actualIsNull());
	arrays.assertDoesNotContainAnyElementsOf(someInfo(), null, newArrayList("Yoda"));
  }

  @Test
  public void should_fail_if_actual_contains_one_element_of_given_iterable() {
	AssertionInfo info = someInfo();
	List<String> list = newArrayList("Vador", "Yoda", "Han");
	try {
	  arrays.assertDoesNotContainAnyElementsOf(info, actual, list);
	} catch (AssertionError e) {
	  verify(failures).failure(info, shouldNotContain(actual, list.toArray(), newLinkedHashSet("Yoda")));
	  return;
	}
	failBecauseExpectedAssertionErrorWasNotThrown();
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  public void should_pass_if_actual_does_not_contain_any_elements_of_given_iterable_according_to_custom_comparison_strategy() {
	arraysWithCustomComparisonStrategy.assertDoesNotContainAnyElementsOf(someInfo(), actual, newArrayList("Han"));
  }

  @Test
  public void should_pass_if_actual_does_not_contain_any_elements_of_given_iterable_even_if_duplicated_according_to_custom_comparison_strategy() {
	arraysWithCustomComparisonStrategy.assertDoesNotContainAnyElementsOf(someInfo(), actual,
	                                                                     newArrayList("Han", "Han", "Anakin"));
  }

  @Test
  public void should_fail_if_actual_contains_one_element_of_given_iterable_according_to_custom_comparison_strategy() {
	AssertionInfo info = someInfo();
	List<String> expected = newArrayList("LuKe", "YODA", "Han");
	try {
	  arraysWithCustomComparisonStrategy.assertDoesNotContainAnyElementsOf(info, actual, expected);
	} catch (AssertionError e) {
	  verify(failures).failure(info, shouldNotContain(actual, expected.toArray(), newLinkedHashSet("LuKe", "YODA"),
		                                              caseInsensitiveStringComparisonStrategy));
	  return;
	}
	failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
