/*
 * Created on Nov 22, 2010
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
import static org.fest.assertions.test.FailureMessages.unexpectedNull;
import static org.fest.util.Arrays.array;
import static org.fest.util.Collections.list;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.util.Collection;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.core.WritableAssertionInfo;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link Collections#assertContainSequence(AssertionInfo, Collection, Object[])}</code>.
 *
 * @author Alex Ruiz
 */
public class Collections_assertContainSequence_Test {

  private static WritableAssertionInfo info;
  private static Collection<String> actual;

  @Rule public ExpectedException thrown = none();

  private Failures failures;
  private Collections collections;

  @BeforeClass public static void setUpOnce() {
    info = new WritableAssertionInfo();
    actual = list("Yoda", "Luke", "Leia", "Obi-Wan");
  }

  @Before public void setUp() {
    failures = spy(Failures.instance());
    collections = new Collections(failures);
  }

  @Test public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(unexpectedNull());
    collections.assertContainSequence(info, null, array("Yoda"));
  }

  @Test public void should_throw_error_if_sequence_is_null() {
    thrown.expectNullPointerException("The sequence to look for should not be null");
    collections.assertContainSequence(info, actual, null);
  }

  @Test public void should_throw_error_if_sequence_is_empty() {
    thrown.expectIllegalArgumentException("The sequence to look for should not be empty");
    collections.assertContainSequence(info, actual, new Object[0]);
  }

  @Test public void should_throw_error_if_sequence_is_bigger_than_actual() {
    Object[] sequence = { "Luke", "Leia", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin" };
    try {
      collections.assertContainSequence(info, actual, sequence);
      fail();
    } catch (AssertionError e) {}
    verify(failures).failure(info, doesNotContainSequence(actual, sequence));
  }

  @Test public void should_fail_if_actual_does_not_contain_whole_sequence() {
    Object[] sequence = { "Han", "C-3PO" };
    try {
      collections.assertContainSequence(info, actual, sequence);
      fail();
    } catch (AssertionError e) {}
    verify(failures).failure(info, doesNotContainSequence(actual, sequence));
  }

  @Test public void should_fail_if_actual_contains_first_elements_of_sequence() {
    Object[] sequence = { "Leia", "Obi-Wan", "Han" };
    try {
      collections.assertContainSequence(info, actual, sequence);
      fail();
    } catch (AssertionError e) {}
    verify(failures).failure(info, doesNotContainSequence(actual, sequence));
  }

  @Test public void should_pass_if_actual_contains_sequence() {
    collections.assertContainSequence(info, actual, array("Luke", "Leia"));
  }

  @Test public void should_pass_if_actual_and_sequence_are_equal() {
    collections.assertContainSequence(info, actual, array("Yoda", "Luke", "Leia", "Obi-Wan"));
  }
}
