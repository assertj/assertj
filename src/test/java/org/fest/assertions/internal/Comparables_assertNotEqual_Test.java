/*
 * Created on Sep 14, 2010
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

import static org.fest.assertions.error.ShouldNotBeEqual.shouldNotBeEqual;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.actualIsNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.assertions.test.TestFailures.expectedAssertionErrorNotThrown;
import static org.mockito.Mockito.*;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.test.*;
import org.junit.*;

/**
 * Tests for <code>{@link Comparables#assertNotEqual(AssertionInfo, Comparable, Comparable)}</code>.
 *
 * @author Alex Ruiz
 */
public class Comparables_assertNotEqual_Test {

  @Rule public ExpectedException thrown = none();

  private Failures failures;
  private Comparables comparables;

  @Before public void setUp() {
    failures = spy(new Failures());
    comparables = new Comparables();
    comparables.failures = failures;
  }

  @Test public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    comparables.assertNotEqual(someInfo(), null, 8);
  }

  @Test public void should_pass_if_objects_are_not_equal() {
    Person a = spy(new Person("Han"));
    Person o = new Person("Yoda");
    comparables.assertNotEqual(someInfo(), a, o);
    verify(a).compareTo(o);
  }

  @Test public void should_fail_if_objects_are_equal() {
    AssertionInfo info = someInfo();
    try {
      comparables.assertNotEqual(info, "Yoda", "Yoda");
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotBeEqual("Yoda", "Yoda"));
      return;
    }
    throw expectedAssertionErrorNotThrown();
  }
}
