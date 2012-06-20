/*
 * Created on Jun 11, 2012
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
 * Copyright @2010-2012 the original author or authors.
 */
package org.fest.assertions.internal;

import static org.fest.assertions.error.ShouldHaveSameClass.shouldHaveSameClass;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.actualIsNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.assertions.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.test.ExpectedException;
import org.fest.assertions.test.Person;

/**
 * Tests for <code>{@link Objects#assertHasSameClassAs(AssertionInfo, Object, Object)}</code>.
 *
 * @author Nicolas François
 */
public class Objects_assertHasNotSameClassAs_Test {

  private static Person actual;

  @BeforeClass public static void setUpOnce() {
    actual = new Person("Yoda");
  }

  @Rule public ExpectedException thrown = none();

  private Failures failures;
  private Objects objects;

  @Before public void setUp() {
    failures = spy(new Failures());
    objects = new Objects();
    objects.failures = failures;
  }

  @Test public void should_pass_if_actual_has_not_same_type_as_other() {
    objects.assertHasSameClassAs(someInfo(), actual, new Person("Luke"));
  }

  @Test public void should_throw_error_if_type_is_null() {
    thrown.expectNullPointerException("The given object should not be null");
    objects.assertHasSameClassAs(someInfo(), actual, null);
  }

  @Test public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    objects.assertHasSameClassAs(someInfo(), null, Object.class);
  }

  @Test public void should_pass_if_actual_has_same_type_as_other() {
    AssertionInfo info = someInfo();
    try {
      objects.assertHasSameClassAs(info, actual, "Yoda");
      failBecauseExpectedAssertionErrorWasNotThrown();
    } catch (AssertionError err) {
      verify(failures).failure(info, shouldHaveSameClass(actual, "Yoda"));
    }
  }
}
