/*
 * Created on Dec 15, 2010
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

import static org.fest.assertions.error.ShouldContainSequence.shouldContainSequence;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.assertions.test.BooleanArrays.*;
import static org.fest.assertions.test.ErrorMessages.*;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.BooleanArrays;
import org.fest.assertions.internal.BooleanArraysBaseTest;

/**
 * Tests for <code>{@link BooleanArrays#assertContainsSequence(AssertionInfo, boolean[], boolean[])}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class BooleanArrays_assertContainsSequence_Test extends BooleanArraysBaseTest {

  @Override
  @Before
  public void setUp() {
    super.setUp();
    actual = arrayOf(true, false, false, true);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    arrays.assertContainsSequence(someInfo(), null, arrayOf(true));
  }

  @Test
  public void should_throw_error_if_sequence_is_null() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    arrays.assertContainsSequence(someInfo(), actual, null);
  }

  @Test
  public void should_throw_error_if_sequence_is_empty() {
    thrown.expectIllegalArgumentException(valuesToLookForIsEmpty());
    arrays.assertContainsSequence(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_fail_if_sequence_is_bigger_than_actual() {
    AssertionInfo info = someInfo();
    boolean[] sequence = { true, true, true, false, false, false };
    try {
      arrays.assertContainsSequence(info, actual, sequence);
    } catch (AssertionError e) {
      verifyFailureThrownWhenSequenceNotFound(info, sequence);
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_does_not_contain_whole_sequence() {
    AssertionInfo info = someInfo();
    boolean[] sequence = { false, true };
    try {
      arrays.assertContainsSequence(info, actual, sequence);
    } catch (AssertionError e) {
      verifyFailureThrownWhenSequenceNotFound(info, sequence);
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_first_elements_of_sequence() {
    AssertionInfo info = someInfo();
    boolean[] sequence = { true, true };
    try {
      arrays.assertContainsSequence(info, actual, sequence);
    } catch (AssertionError e) {
      verifyFailureThrownWhenSequenceNotFound(info, sequence);
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  private void verifyFailureThrownWhenSequenceNotFound(AssertionInfo info, boolean[] sequence) {
    verify(failures).failure(info, shouldContainSequence(actual, sequence));
  }

  @Test
  public void should_pass_if_actual_contains_sequence() {
    arrays.assertContainsSequence(someInfo(), actual, arrayOf(true, false));
  }

  @Test
  public void should_pass_if_actual_and_sequence_are_equal() {
    arrays.assertContainsSequence(someInfo(), actual, arrayOf(true, false, false, true));
  }
}
