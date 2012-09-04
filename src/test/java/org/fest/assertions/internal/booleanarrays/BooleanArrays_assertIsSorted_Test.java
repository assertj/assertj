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
package org.fest.assertions.internal.booleanarrays;

import static org.fest.assertions.error.ShouldBeSorted.shouldBeSorted;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.assertions.test.BooleanArrays.*;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.BooleanArrays;
import org.fest.assertions.internal.BooleanArraysBaseTest;

/**
 * Tests for <code>{@link BooleanArrays#assertIsSorted(AssertionInfo, Object[])}</code>.
 * 
 * @author Joel Costigliola
 */
public class BooleanArrays_assertIsSorted_Test extends BooleanArraysBaseTest {

  @Override
  @Before
  public void setUp() {
    super.setUp();
    actual = new boolean[] { false, false, true, true, true };
  }

  @Test
  public void should_pass_if_actual_is_sorted_in_ascending_order() {
    arrays.assertIsSorted(someInfo(), actual);
  }

  @Test
  public void should_pass_if_actual_is_empty() {
    arrays.assertIsSorted(someInfo(), emptyArray());
  }

  @Test
  public void should_pass_if_actual_contains_only_one_element() {
    arrays.assertIsSorted(someInfo(), arrayOf(true));
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    arrays.assertIsSorted(someInfo(), (boolean[]) null);
  }

  @Test
  public void should_fail_if_actual_is_not_sorted_in_ascending_order() {
    AssertionInfo info = someInfo();
    actual = arrayOf(false, true, false);
    try {
      arrays.assertIsSorted(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeSorted(1, actual));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
