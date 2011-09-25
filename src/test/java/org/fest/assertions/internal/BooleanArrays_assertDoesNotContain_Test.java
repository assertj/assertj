/*
 * Created on Dec 15, 2010
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

import static org.fest.assertions.error.ShouldNotContain.shouldNotContain;
import static org.fest.assertions.test.BooleanArrayFactory.*;
import static org.fest.assertions.test.ErrorMessages.*;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.actualIsNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.assertions.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.fest.util.Collections.set;
import static org.mockito.Mockito.*;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link BooleanArrays#assertDoesNotContain(AssertionInfo, boolean[], boolean[])}</code>.
 *
 * @author Alex Ruiz
 */
public class BooleanArrays_assertDoesNotContain_Test {

  private static boolean[] actual;

  @Rule public ExpectedException thrown = none();

  private Failures failures;
  private BooleanArrays arrays;

  @BeforeClass public static void setUpOnce() {
    actual = array(true, true);
  }

  @Before public void setUp() {
    failures = spy(new Failures());
    arrays = new BooleanArrays();
    arrays.failures = failures;
  }

  @Test public void should_pass_if_actual_does_not_contain_given_values() {
    arrays.assertDoesNotContain(someInfo(), actual, array(false));
  }

  @Test public void should_pass_if_actual_does_not_contain_given_values_even_if_duplicated() {
    arrays.assertDoesNotContain(someInfo(), actual, array(false, false));
  }

  @Test public void should_throw_error_if_array_of_values_to_look_for_is_empty() {
    thrown.expectIllegalArgumentException(valuesToLookForIsEmpty());
    arrays.assertDoesNotContain(someInfo(), actual, emptyArray());
  }

  @Test public void should_throw_error_if_array_of_values_to_look_for_is_null() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    arrays.assertDoesNotContain(someInfo(), actual, null);
  }

  @Test public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    arrays.assertDoesNotContain(someInfo(), null, array(true));
  }

  @Test public void should_fail_if_actual_contains_given_values() {
    AssertionInfo info = someInfo();
    boolean[] expected = array(true);
    try {
      arrays.assertDoesNotContain(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotContain(actual, expected, set(true)));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
