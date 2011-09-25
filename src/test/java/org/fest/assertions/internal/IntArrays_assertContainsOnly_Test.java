/*
 * Created on Dec 14, 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.internal;

import static org.fest.assertions.error.ShouldContainOnly.shouldContainOnly;
import static org.fest.assertions.test.ErrorMessages.*;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.actualIsNull;
import static org.fest.assertions.test.IntArrayFactory.*;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.assertions.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.fest.util.Collections.set;
import static org.mockito.Mockito.*;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link IntArrays#assertContainsOnly(AssertionInfo, int[], int[])}</code>.
 *
 * @author Alex Ruiz
 */
public class IntArrays_assertContainsOnly_Test {

  @Rule public ExpectedException thrown = none();

  private Failures failures;
  private int[] actual;
  private IntArrays arrays;

  @Before public void setUp() {
    failures = spy(new Failures());
    actual = array(6, 8, 10);
    arrays = new IntArrays();
    arrays.failures = failures;
  }

  @Test public void should_pass_if_actual_contains_given_values_only() {
    arrays.assertContainsOnly(someInfo(), actual, array(6, 8, 10));
  }

  @Test public void should_pass_if_actual_contains_given_values_only_in_different_order() {
    arrays.assertContainsOnly(someInfo(), actual, array(10, 8, 6));
  }

  @Test public void should_pass_if_actual_contains_given_values_only_more_than_once() {
    actual = array(6, 8, 10, 8, 8, 8);
    arrays.assertContainsOnly(someInfo(), actual, array(6, 8, 10));
  }

  @Test public void should_pass_if_actual_contains_given_values_only_even_if_duplicated() {
    arrays.assertContainsOnly(someInfo(), actual, array(6, 8, 10, 6, 8, 10));
  }

  @Test public void should_throw_error_if_array_of_values_to_look_for_is_empty() {
    thrown.expectIllegalArgumentException(valuesToLookForIsEmpty());
    arrays.assertContainsOnly(someInfo(), actual, emptyArray());
  }

  @Test public void should_throw_error_if_array_of_values_to_look_for_is_null() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    arrays.assertContainsOnly(someInfo(), actual, null);
  }

  @Test public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    arrays.assertContainsOnly(someInfo(), null, array(8));
  }

  @Test public void should_fail_if_actual_does_not_contain_given_values_only() {
    AssertionInfo info = someInfo();
    int[] expected = { 6, 8, 20 };
    try {
      arrays.assertContainsOnly(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainOnly(actual, expected, set(20), set(10)));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
