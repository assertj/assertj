/*
 * Created on Oct 19, 2010
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

import static org.fest.assertions.error.ShouldBeGreaterOrEqual.shouldBeGreaterOrEqual;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.unexpectedNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.assertions.test.TestFailures.expectedAssertionErrorNotThrown;
import static org.mockito.Mockito.*;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link Comparables#assertGreaterThanOrEqualTo(AssertionInfo, Comparable, Comparable)}</code>.
 *
 * @author Alex Ruiz
 */
public class Comparables_assertGreaterThanOrEqualTo_Test {

  @Rule public ExpectedException thrown = none();

  private Failures failures;
  private Comparables comparables;

  @Before public void setUp() {
    failures = spy(new Failures());
    comparables = new Comparables();
    comparables.failures = failures;
  }

  @Test public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(unexpectedNull());
    comparables.assertGreaterThanOrEqualTo(someInfo(), null, 8);
  }

  @Test public void should_pass_if_actual_is_greater_than_other() {
    comparables.assertGreaterThanOrEqualTo(someInfo(), 8, 6);
  }

  @Test public void should_pass_if_actual_is_equal_to_other() {
    comparables.assertGreaterThanOrEqualTo(someInfo(), "Yoda", "Yoda");
  }

  @Test public void should_fail_if_actual_is_less_than_other() {
    AssertionInfo info = someInfo();
    try {
      comparables.assertGreaterThanOrEqualTo(info, 6, 8);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeGreaterOrEqual(6, 8));
      return;
    }
    throw expectedAssertionErrorNotThrown();
  }
}
