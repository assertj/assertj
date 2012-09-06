/*
 * Created on Dec 15, 2010
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
package org.fest.assertions.internal.booleanarrays;

import static org.fest.assertions.error.ShouldContain.shouldContain;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.assertions.test.BooleanArrays.*;
import static org.fest.assertions.test.ErrorMessages.*;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.fest.util.Sets.newLinkedHashSet;

import static org.mockito.Mockito.verify;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.BooleanArrays;
import org.fest.assertions.internal.BooleanArraysBaseTest;

/**
 * Tests for <code>{@link BooleanArrays#assertContains(AssertionInfo, boolean[], boolean[])}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class BooleanArrays_assertContains_Test extends BooleanArraysBaseTest {

  @Test
  public void should_pass_if_actual_contains_given_values() {
    arrays.assertContains(someInfo(), actual, arrayOf(true));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_in_different_order() {
    arrays.assertContains(someInfo(), actual, arrayOf(false, true));
  }

  @Test
  public void should_pass_if_actual_contains_all_given_values() {
    arrays.assertContains(someInfo(), actual, arrayOf(true, false));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_more_than_once() {
    actual = arrayOf(true, true, false, false);
    arrays.assertContains(someInfo(), actual, arrayOf(true));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_even_if_duplicated() {
    arrays.assertContains(someInfo(), actual, arrayOf(true, true));
  }

  @Test
  public void should_throw_error_if_array_of_values_to_look_for_is_empty() {
    thrown.expectIllegalArgumentException(valuesToLookForIsEmpty());
    arrays.assertContains(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_throw_error_if_array_of_values_to_look_for_is_null() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    arrays.assertContains(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    arrays.assertContains(someInfo(), null, arrayOf(true));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_values() {
    AssertionInfo info = someInfo();
    actual = arrayOf(true);
    boolean[] expected = { false };
    try {
      arrays.assertContains(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContain(actual, expected, newLinkedHashSet(false)));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
