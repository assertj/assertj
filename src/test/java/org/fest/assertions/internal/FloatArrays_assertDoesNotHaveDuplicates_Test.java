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
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.internal;

import static org.fest.assertions.error.HasDuplicates.hasDuplicates;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.unexpectedNull;
import static org.fest.assertions.test.FloatArrayFactory.*;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.assertions.test.TestFailures.expectedAssertionErrorNotThrown;
import static org.fest.util.Collections.set;
import static org.mockito.Mockito.*;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link FloatArrays#assertDoesNotHaveDuplicates(AssertionInfo, float[])}</code>.
 *
 * @author Alex Ruiz
 */
public class FloatArrays_assertDoesNotHaveDuplicates_Test {

  @Rule public ExpectedException thrown = none();

  private Failures failures;
  private float[] actual;
  private FloatArrays arrays;

  @Before public void setUp() {
    failures = spy(new Failures());
    actual = array(6f, 8f);
    arrays = new FloatArrays();
    arrays.failures = failures;
  }

  @Test public void should_pass_if_actual_does_not_have_duplicates() {
    arrays.assertDoesNotHaveDuplicates(someInfo(), actual);
  }

  @Test public void should_pass_if_actual_is_empty() {
    arrays.assertDoesNotHaveDuplicates(someInfo(), emptyArray());
  }

  @Test public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(unexpectedNull());
    arrays.assertDoesNotHaveDuplicates(someInfo(), null);
  }

  @Test public void should_fail_if_actual_contains_duplicates() {
    AssertionInfo info = someInfo();
    actual = array(6f, 8f, 6f, 8f);
    try {
      arrays.assertDoesNotHaveDuplicates(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, hasDuplicates(actual, set(6f, 8f)));
      return;
    }
    throw expectedAssertionErrorNotThrown();
  }
}
