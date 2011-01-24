/*
 * Created on Dec 2, 2010
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

import static org.fest.assertions.error.ShouldStartWith.shouldStartWith;
import static org.fest.assertions.test.ErrorMessages.*;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.unexpectedNull;
import static org.fest.assertions.test.ObjectArrayFactory.emptyArray;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.assertions.test.TestFailures.expectedAssertionErrorNotThrown;
import static org.fest.util.Arrays.array;
import static org.fest.util.Collections.list;
import static org.mockito.Mockito.*;

import java.util.Collection;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link Collections#assertStartsWith(AssertionInfo, Collection, Object[])}</code>.
 *
 * @author Alex Ruiz
 */
public class Collections_assertStartsWith_Test {

  private static Collection<String> actual;

  @Rule public ExpectedException thrown = none();

  private Failures failures;
  private Collections collections;

  @BeforeClass public static void setUpOnce() {
    actual = list("Yoda", "Luke", "Leia", "Obi-Wan");
  }

  @Before public void setUp() {
    failures = spy(new Failures());
    collections = new Collections();
    collections.failures = failures;
  }

  @Test public void should_throw_error_if_sequence_is_null() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    collections.assertStartsWith(someInfo(), actual, null);
  }

  @Test public void should_throw_error_if_sequence_is_empty() {
    thrown.expectIllegalArgumentException(valuesToLookForIsEmpty());
    collections.assertStartsWith(someInfo(), actual, emptyArray());
  }

  @Test public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(unexpectedNull());
    collections.assertStartsWith(someInfo(), null, array("Yoda"));
  }

  @Test public void should_fail_if_sequence_is_bigger_than_actual() {
    AssertionInfo info = someInfo();
    Object[] sequence = { "Yoda", "Luke", "Leia", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin" };
    try {
      collections.assertStartsWith(info, actual, sequence);
    } catch (AssertionError e) {
      verifySequenceNotFound(info, sequence);
      return;
    }
    throw expectedAssertionErrorNotThrown();
  }

  @Test public void should_fail_if_actual_does_not_start_with_sequence() {
    AssertionInfo info = someInfo();
    Object[] sequence = { "Han", "C-3PO" };
    try {
      collections.assertStartsWith(info, actual, sequence);
    } catch (AssertionError e) {
      verifySequenceNotFound(info, sequence);
      return;
    }
    throw expectedAssertionErrorNotThrown();
  }

  @Test public void should_fail_if_actual_starts_with_first_elements_of_sequence_only() {
    AssertionInfo info = someInfo();
    Object[] sequence = { "Leia", "Obi-Wan", "Han" };
    try {
      collections.assertStartsWith(info, actual, sequence);
    } catch (AssertionError e) {
      verifySequenceNotFound(info, sequence);
      return;
    }
    throw expectedAssertionErrorNotThrown();
  }

  private void verifySequenceNotFound(AssertionInfo info, Object[] sequence) {
    verify(failures).failure(info, shouldStartWith(actual, sequence));
  }

  @Test public void should_pass_if_actual_starts_with_sequence() {
    collections.assertStartsWith(someInfo(), actual, array("Yoda", "Luke", "Leia"));
  }

  @Test public void should_pass_if_actual_and_sequence_are_equal() {
    collections.assertStartsWith(someInfo(), actual, array("Yoda", "Luke", "Leia", "Obi-Wan"));
  }
}
