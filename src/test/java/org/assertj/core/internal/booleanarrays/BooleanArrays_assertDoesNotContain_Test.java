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

import static org.assertj.core.error.ShouldNotContain.shouldNotContain;
import static org.assertj.core.internal.ErrorMessages.*;
import static org.assertj.core.test.BooleanArrays.*;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;


import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.BooleanArrays;
import org.assertj.core.internal.BooleanArraysBaseTest;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests for <code>{@link BooleanArrays#assertDoesNotContain(AssertionInfo, boolean[], boolean[])}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class BooleanArrays_assertDoesNotContain_Test extends BooleanArraysBaseTest {

  @Before
  public void setUpOnce() {
    super.setUp();
    actual = arrayOf(true, true);
  }

  @Test
  public void should_pass_if_actual_does_not_contain_given_values() {
    arrays.assertDoesNotContain(someInfo(), actual, arrayOf(false));
  }

  @Test
  public void should_pass_if_actual_does_not_contain_given_values_even_if_duplicated() {
    arrays.assertDoesNotContain(someInfo(), actual, arrayOf(false, false));
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
    arrays.assertDoesNotContain(someInfo(), null, arrayOf(true));
  }

  @Test
  public void should_fail_if_actual_contains_given_values() {
    AssertionInfo info = someInfo();
    boolean[] expected = arrayOf(true);
    try {
      arrays.assertDoesNotContain(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotContain(actual, expected, newLinkedHashSet(true)));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
