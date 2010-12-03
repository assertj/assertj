/*
 * Created on Nov 29, 2010
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

import static org.fest.assertions.error.DoesNotContainSequence.doesNotContainSequence;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.*;
import static org.fest.assertions.util.ArrayWrapperList.wrap;
import static org.fest.util.Arrays.array;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.core.WritableAssertionInfo;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link ObjectArrays#assertContainsSequence(AssertionInfo, Object[], Object[])}</code>.
 *
 * @author Alex Ruiz
 */
public class ObjectArrays_assertContainsSequence_Test {

  private static WritableAssertionInfo info;
  private static Object[] actual;

  @Rule public ExpectedException thrown = none();

  private Failures failures;
  private ObjectArrays arrays;

  @BeforeClass public static void setUpOnce() {
    info = new WritableAssertionInfo();
    actual = array("Yoda", "Luke", "Leia", "Obi-Wan");
  }

  @Before public void setUp() {
    failures = spy(Failures.instance());
    arrays = new ObjectArrays(failures);
  }

  @Test public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(unexpectedNull());
    arrays.assertContainsSequence(info, null, array("Yoda"));
  }

  @Test public void should_throw_error_if_sequence_is_null() {
    thrown.expectNullPointerException(arrayToLookForIsNull());
    arrays.assertContainsSequence(info, actual, null);
  }

  @Test public void should_throw_error_if_sequence_is_empty() {
    thrown.expectIllegalArgumentException(arrayToLookForIsEmpty());
    arrays.assertContainsSequence(info, actual, new Object[0]);
  }

  @Test public void should_fail_if_sequence_is_bigger_than_actual() {
    Object[] sequence = { "Luke", "Leia", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin" };
    try {
      arrays.assertContainsSequence(info, actual, sequence);
      fail();
    } catch (AssertionError e) {}
    assertThatFailureWasThrownWhenSequenceWasNotFound(sequence);
  }

  @Test public void should_fail_if_actual_does_not_contain_whole_sequence() {
    Object[] sequence = { "Han", "C-3PO" };
    try {
      arrays.assertContainsSequence(info, actual, sequence);
      fail();
    } catch (AssertionError e) {}
    assertThatFailureWasThrownWhenSequenceWasNotFound(sequence);
  }

  @Test public void should_fail_if_actual_contains_first_elements_of_sequence() {
    Object[] sequence = { "Leia", "Obi-Wan", "Han" };
    try {
      arrays.assertContainsSequence(info, actual, sequence);
      fail();
    } catch (AssertionError e) {}
    assertThatFailureWasThrownWhenSequenceWasNotFound(sequence);
  }

  private void assertThatFailureWasThrownWhenSequenceWasNotFound(Object[] sequence) {
    verify(failures).failure(info, doesNotContainSequence(wrap(actual), wrap(sequence)));
  }

  @Test public void should_pass_if_actual_contains_sequence() {
    arrays.assertContainsSequence(info, actual, array("Luke", "Leia"));
  }

  @Test public void should_pass_if_actual_and_sequence_are_equal() {
    arrays.assertContainsSequence(info, actual, array("Yoda", "Luke", "Leia", "Obi-Wan"));
  }
}
