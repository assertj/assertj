/*
 * Created on Dec 20, 2010
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

import static org.fest.assertions.error.DoesNotStartWith.doesNotStartWith;
import static org.fest.assertions.test.CharArrayFactory.*;
import static org.fest.assertions.test.ErrorMessages.*;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.unexpectedNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.assertions.util.ArrayWrapperList.wrap;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link CharArrays#assertStartsWith(AssertionInfo, char[], char[])}</code>.
 *
 * @author Alex Ruiz
 */
public class CharArrays_assertStartsWith_Test {

  private static char[] actual;

  @Rule public ExpectedException thrown = none();

  private Failures failures;
  private CharArrays arrays;

  @BeforeClass public static void setUpOnce() {
    actual = array('a', 'b', 'c', 'd');
  }

  @Before public void setUp() {
    failures = spy(Failures.instance());
    arrays = new CharArrays(failures);
  }

  @Test public void should_throw_error_if_sequence_is_null() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    arrays.assertStartsWith(someInfo(), actual, null);
  }

  @Test public void should_throw_error_if_sequence_is_empty() {
    thrown.expectIllegalArgumentException(valuesToLookForIsEmpty());
    arrays.assertStartsWith(someInfo(), actual, emptyArray());
  }

  @Test public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(unexpectedNull());
    arrays.assertStartsWith(someInfo(), null, array('a'));
  }

  @Test public void should_fail_if_sequence_is_bigger_than_actual() {
    AssertionInfo info = someInfo();
    char[] sequence = { 'a', 'b', 'c', 'd', 'e', 'f' };
    try {
      arrays.assertStartsWith(info, actual, sequence);
      fail();
    } catch (AssertionError e) {}
    assertThatFailureWasThrownWhenActualDoesNotStartWithSequence(info, sequence);
  }

  @Test public void should_fail_if_actual_does_not_start_with_sequence() {
    AssertionInfo info = someInfo();
    char[] sequence = { 'b', 'c' };
    try {
      arrays.assertStartsWith(info, actual, sequence);
      fail();
    } catch (AssertionError e) {}
    assertThatFailureWasThrownWhenActualDoesNotStartWithSequence(info, sequence);
  }

  @Test public void should_fail_if_actual_starts_with_first_elements_of_sequence_only() {
    AssertionInfo info = someInfo();
    char[] sequence = { 'a', 'x' };
    try {
      arrays.assertStartsWith(info, actual, sequence);
      fail();
    } catch (AssertionError e) {}
    assertThatFailureWasThrownWhenActualDoesNotStartWithSequence(info, sequence);
  }

  private void assertThatFailureWasThrownWhenActualDoesNotStartWithSequence(AssertionInfo info, char[] sequence) {
    verify(failures).failure(info, doesNotStartWith(wrap(actual), wrap(sequence)));
  }

  @Test public void should_pass_if_actual_starts_with_sequence() {
    arrays.assertStartsWith(someInfo(), actual, array('a', 'b', 'c'));
  }

  @Test public void should_pass_if_actual_and_sequence_are_equal() {
    arrays.assertStartsWith(someInfo(), actual, array('a', 'b', 'c', 'd'));
  }
}
