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
package org.assertj.core.internal.booleanarrays;

import static org.assertj.core.error.ShouldContainExactly.elementsDifferAtIndex;
import static org.assertj.core.error.ShouldContainExactly.shouldContainExactly;
import static org.assertj.core.error.ShouldContainExactly.shouldHaveSameSize;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.test.BooleanArrays.arrayOf;
import static org.assertj.core.test.BooleanArrays.emptyArray;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.BooleanArrays;
import org.assertj.core.internal.BooleanArraysBaseTest;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.junit.Test;

/**
 * Tests for <code>{@link BooleanArrays#assertContainsExactly(AssertionInfo, boolean[], boolean[])}</code>.
 */
public class BooleanArrays_assertContainsExactly_Test extends BooleanArraysBaseTest {

  @Test
  public void should_pass_if_actual_contains_given_values_exactly() {
	arrays.assertContainsExactly(someInfo(), actual, arrayOf(true, false));
  }

  @Test
  public void should_pass_if_actual_and_given_values_are_empty() {
	arrays.assertContainsExactly(someInfo(), emptyArray(), emptyArray());
  }

  @Test
  public void should_fail_if_actual_contains_given_values_exactly_but_in_different_order() {
	AssertionInfo info = someInfo();
	try {
	  arrays.assertContainsExactly(info, actual, arrayOf(false, true));
	} catch (AssertionError e) {
	  verify(failures).failure(info, elementsDifferAtIndex(true, false, 0));
	  return;
	}
	failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_arrays_have_different_sizes() {
	thrown.expectAssertionError();
	arrays.assertContainsExactly(someInfo(), actual, arrayOf(true));
  }

  @Test
  public void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
	thrown.expectAssertionError();
	arrays.assertContainsExactly(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_throw_error_if_array_of_values_to_look_for_is_null() {
	thrown.expectNullPointerException(valuesToLookForIsNull());
	arrays.assertContainsExactly(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
	thrown.expectAssertionError(actualIsNull());
	arrays.assertContainsExactly(someInfo(), null, arrayOf(true));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_given_values_exactly() {
	AssertionInfo info = someInfo();
	boolean[] expected = { true, true };
	try {
	  arrays.assertContainsExactly(info, actual, expected);
	} catch (AssertionError e) {
      verify(failures).failure(info, shouldContainExactly(actual, expected, newArrayList(), newArrayList(false)));
	  return;
	}
	failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_all_given_values_but_size_differ() {
    AssertionInfo info = someInfo();
    boolean[] expected = { true };
    try {
      arrays.assertContainsExactly(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info,
                               shouldHaveSameSize(actual, expected, 2, 1, StandardComparisonStrategy.instance()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
