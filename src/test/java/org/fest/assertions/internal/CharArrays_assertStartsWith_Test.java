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
import static org.fest.assertions.test.ArrayFactory.arrayOfChars;
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
 * Tests for <code>{@link CharArrays#assertStartsWith(AssertionInfo, char[], char[])}</code>.
 *
 * @author Alex Ruiz
 */
public class CharArrays_assertStartsWith_Test {

  private static WritableAssertionInfo info;
  private static char[] actual;

  @Rule public ExpectedException thrown = none();

  private Failures failures;
  private CharArrays arrays;

  @BeforeClass public static void setUpOnce() {
    info = new WritableAssertionInfo();
    actual = arrayOfChars('a', 'b', 'c', 'd');
  }

  @Before public void setUp() {
    failures = spy(Failures.instance());
    arrays = new CharArrays(failures);
  }

  @Test public void should_throw_error_if_sequence_is_null() {
    thrown.expectNullPointerException(arrayToLookForIsNull());
    arrays.assertStartsWith(info, actual, null);
  }

  @Test public void should_throw_error_if_sequence_is_empty() {
    thrown.expectIllegalArgumentException(arrayToLookForIsEmpty());
    arrays.assertStartsWith(info, actual, new char[0]);
  }

  @Test public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(unexpectedNull());
    arrays.assertStartsWith(info, null, arrayOfChars('a'));
  }

  @Test public void should_fail_if_sequence_is_bigger_than_actual() {
    char[] sequence = { 'a', 'b', 'c', 'd', 'e', 'f' };
    try {
      arrays.assertStartsWith(info, actual, sequence);
      fail();
    } catch (AssertionError e) {}
    assertThatFailureWasThrownWhenActualDoesNotStartWith(sequence);
  }

  @Test public void should_fail_if_actual_does_not_start_with_sequence() {
    char[] sequence = { 'b', 'c' };
    try {
      arrays.assertStartsWith(info, actual, sequence);
      fail();
    } catch (AssertionError e) {}
    assertThatFailureWasThrownWhenActualDoesNotStartWith(sequence);
  }

  @Test public void should_fail_if_actual_starts_with_first_elements_of_sequence_only() {
    char[] sequence = { 'a', 'x' };
    try {
      arrays.assertStartsWith(info, actual, sequence);
      fail();
    } catch (AssertionError e) {}
    assertThatFailureWasThrownWhenActualDoesNotStartWith(sequence);
  }

  private void assertThatFailureWasThrownWhenActualDoesNotStartWith(char[] sequence) {
    verify(failures).failure(info, doesNotStartWith(wrap(actual), wrap(sequence)));
  }

  @Test public void should_pass_if_actual_starts_with_sequence() {
    arrays.assertStartsWith(info, actual, arrayOfChars('a', 'b', 'c'));
  }

  @Test public void should_pass_if_actual_and_sequence_are_equal() {
    arrays.assertStartsWith(info, actual, arrayOfChars('a', 'b', 'c', 'd'));
  }
}
