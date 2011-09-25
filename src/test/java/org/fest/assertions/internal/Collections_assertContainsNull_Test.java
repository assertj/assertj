/*
 * Created on Sep 30, 2010
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

import static org.fest.assertions.error.ShouldContainNull.shouldContainNull;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.actualIsNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.assertions.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.fest.util.Collections.list;

import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.*;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.test.ExpectedException;

/**
 * Tests for <code>{@link Collections#assertContainsNull(AssertionInfo, Collection)}</code>.
 *
 * @author Joel Costigliola
 */
public class Collections_assertContainsNull_Test {

  @Rule public ExpectedException thrown = none();

  private List<String> actual;
  private Failures failures;
  private Collections collections;

  @Before public void setUp() {
    actual = list("Luke", "Yoda", null);
    failures = spy(new Failures());
    collections = new Collections();
    collections.failures = failures;
  }

  @Test public void should_pass_if_actual_contains_null() {
    collections.assertContainsNull(someInfo(), actual);
  }

  @Test public void should_pass_if_actual_contains_only_null_values() {
    actual = list(null, null);
    collections.assertContainsNull(someInfo(), actual);
  }

  @Test public void should_pass_if_actual_contains_null_more_than_once() {
    actual.add(null);
    collections.assertContainsNull(someInfo(), actual);
  }

  @Test public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    collections.assertContainsNull(someInfo(), null);
  }

  @Test public void should_fail_if_actual_does_not_contain_null() {
    AssertionInfo info = someInfo();
    actual = list("Luke", "Yoda");
    try {
      collections.assertContainsNull(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainNull(actual));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
