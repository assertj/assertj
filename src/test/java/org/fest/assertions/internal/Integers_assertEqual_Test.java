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

import static org.fest.assertions.error.ShouldBeEqual.shouldBeEqual;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.actualIsNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.assertions.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.mockito.Mockito.*;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link Integers#assertEqual(AssertionInfo, Integer, int)}</code>.
 *
 * @author Alex Ruiz
 */
public class Integers_assertEqual_Test {

  @Rule public ExpectedException thrown = none();

  private Failures failures;
  private Integers integers;

  @Before public void setUp() {
    failures = spy(new Failures());
    integers = new Integers();
    integers.failures = failures;
  }

  @Test public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    integers.assertEqual(someInfo(), null, 8);
  }

  @Test public void should_pass_if_integers_are_equal() {
    integers.assertEqual(someInfo(), 8, 8);
  }

  @Test public void should_fail_if_integers_are_not_equal() {
    AssertionInfo info = someInfo();
    try {
      integers.assertEqual(info, 6, 8);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeEqual(6, 8));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
