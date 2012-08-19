/*
 * Created on Jun 11, 2012
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
 * Copyright @2010-2012 the original author or authors.
 */
package org.fest.assertions.internal.objects;

import static org.fest.assertions.error.ShouldNotHaveSameClass.shouldNotHaveSameClass;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;

import static org.mockito.Mockito.verify;

import org.junit.BeforeClass;
import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.Objects;
import org.fest.assertions.internal.ObjectsBaseTest;
import org.fest.test.Person;

/**
 * Tests for <code>{@link Objects#assertDoesNotHaveSameClassAs(AssertionInfo, Object, Object)}</code>.
 * 
 * @author Nicolas François
 * @author Joel Costigliola
 */
public class Objects_assertDoesNotHaveNotSameClassAs_Test extends ObjectsBaseTest {

  private static Person actual;

  @BeforeClass
  public static void setUpOnce() {
    actual = new Person("Yoda");
  }

  @Test
  public void should_pass_if_actual_does_not_have_not_same_class_as_other() {
    objects.assertDoesNotHaveSameClassAs(someInfo(), actual, "Luke");
  }

  @Test
  public void should_throw_error_if_type_is_null() {
    thrown.expectNullPointerException("The given object should not be null");
    objects.assertDoesNotHaveSameClassAs(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    objects.assertDoesNotHaveSameClassAs(someInfo(), null, Object.class);
  }

  @Test
  public void should_fail_if_actual_has_same_type_as_other() {
    AssertionInfo info = someInfo();
    try {
      objects.assertDoesNotHaveSameClassAs(info, actual, new Person("Luke"));
      failBecauseExpectedAssertionErrorWasNotThrown();
    } catch (AssertionError err) {
      verify(failures).failure(info, shouldNotHaveSameClass(actual, new Person("Luke")));
    }
  }
}
