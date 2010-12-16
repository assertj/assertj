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
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.internal;

import static org.fest.assertions.error.DoesNotEndWith.doesNotEndWith;
import static org.fest.assertions.test.Arrays.arrayOfBooleans;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.*;
import static org.fest.assertions.util.ArrayWrapperList.wrap;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.core.WritableAssertionInfo;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link BooleanArrays#assertEndsWith(AssertionInfo, boolean[], boolean[])}</code>.
 *
 * @author Alex Ruiz
 */
public class BooleanArrays_assertEndsWith_Test {

  private static WritableAssertionInfo info;
  private static boolean[] actual;

  @Rule public ExpectedException thrown = none();

  private Failures failures;
  private BooleanArrays arrays;

  @BeforeClass public static void setUpOnce() {
    info = new WritableAssertionInfo();
    actual = arrayOfBooleans(true, false, false, true);
  }

  @Before public void setUp() {
    failures = spy(Failures.instance());
    arrays = new BooleanArrays(failures);
  }

  @Test public void should_throw_error_if_sequence_is_null() {
    thrown.expectNullPointerException(arrayToLookForIsNull());
    arrays.assertEndsWith(info, actual, null);
  }

  @Test public void should_throw_error_if_sequence_is_empty() {
    thrown.expectIllegalArgumentException(arrayToLookForIsEmpty());
    arrays.assertEndsWith(info, actual, new boolean[0]);
  }

  @Test public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(unexpectedNull());
    arrays.assertEndsWith(info, null, arrayOfBooleans(true));
  }

  @Test public void should_fail_if_sequence_is_bigger_than_actual() {
    boolean[] sequence = { true, false, false, true, true, false };
    try {
      arrays.assertEndsWith(info, actual, sequence);
      fail();
    } catch (AssertionError e) {}
    assertThatFailureWasThrownWhenActualDoesNotEndWith(sequence);
  }

  @Test public void should_fail_if_actual_does_not_end_with_sequence() {
    boolean[] sequence = { true, false };
    try {
      arrays.assertEndsWith(info, actual, sequence);
      fail();
    } catch (AssertionError e) {}
    assertThatFailureWasThrownWhenActualDoesNotEndWith(sequence);
  }

  @Test public void should_fail_if_actual_ends_with_first_elements_of_sequence_only() {
    boolean[] sequence = { false, false };
    try {
      arrays.assertEndsWith(info, actual, sequence);
      fail();
    } catch (AssertionError e) {}
    assertThatFailureWasThrownWhenActualDoesNotEndWith(sequence);
  }

  private void assertThatFailureWasThrownWhenActualDoesNotEndWith(boolean[] sequence) {
    verify(failures).failure(info, doesNotEndWith(wrap(actual), wrap(sequence)));
  }

  @Test public void should_pass_if_actual_ends_with_sequence() {
    arrays.assertEndsWith(info, actual, arrayOfBooleans(false, true));
  }

  @Test public void should_pass_if_actual_and_sequence_are_equal() {
    arrays.assertEndsWith(info, actual, arrayOfBooleans(true, false, false, true));
  }
}
