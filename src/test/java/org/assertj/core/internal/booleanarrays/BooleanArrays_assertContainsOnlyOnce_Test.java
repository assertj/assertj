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

import static org.assertj.core.error.ShouldContainsOnlyOnce.shouldContainsOnlyOnce;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.test.BooleanArrays.arrayOf;
import static org.assertj.core.test.BooleanArrays.emptyArray;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.BooleanArrays;
import org.assertj.core.internal.BooleanArraysBaseTest;
import org.junit.Test;

/**
 * Tests for <code>{@link BooleanArrays#assertContainsOnlyOnce(AssertionInfo, boolean[], boolean[])}</code>.
 * 
 * @author William Delanoue
 */
public class BooleanArrays_assertContainsOnlyOnce_Test extends BooleanArraysBaseTest {

  @Test
  public void should_pass_if_actual_contains_given_values_only() {
    arrays.assertContainsOnlyOnce(someInfo(), actual, arrayOf(true, false));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_only_in_different_order() {
    arrays.assertContainsOnlyOnce(someInfo(), actual, arrayOf(false, true));
  }

  @Test
  public void should_fail_if_actual_contains_given_values_only_more_than_once() {
    AssertionInfo info = someInfo();
    actual = arrayOf(true, true, false, false);
    boolean[] expected = { true, false };
    try {
      arrays.assertContainsOnlyOnce(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainsOnlyOnce(actual, expected, newLinkedHashSet(), newLinkedHashSet(true, false)));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_contains_given_values_only_even_if_duplicated() {
    arrays.assertContainsOnlyOnce(someInfo(), actual, arrayOf(true, true, false, false));
  }

  @Test
  public void should_pass_if_actual_and_given_values_are_empty() {
    actual = emptyArray();
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
    arrays.assertContainsOnlyOnce(someInfo(), null, arrayOf(true));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_given_values_only() {
    AssertionInfo info = someInfo();
    actual = arrayOf(true);
    boolean[] expected = { true, false };
    try {
      arrays.assertContainsOnlyOnce(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainsOnlyOnce(actual, expected, newLinkedHashSet(false), newLinkedHashSet()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
