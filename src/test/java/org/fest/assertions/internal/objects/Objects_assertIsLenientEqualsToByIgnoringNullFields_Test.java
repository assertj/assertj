/*
 * Created on Apr 8, 2012
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
 * Copyright @2012 the original author or authors.
 */
package org.fest.assertions.internal.objects;

import static org.fest.assertions.error.ShouldBeInstance.shouldBeInstance;
import static org.fest.assertions.error.ShouldBeLenientEqualByIgnoring.shouldBeLenientEqualByIgnoring;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.util.Lists.newArrayList;

import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.ObjectsBaseTest;
import org.fest.test.Employee;
import org.fest.test.Jedi;

/**
 * Tests for <code>{@link Objects#assertIsLenientEqualsToByIgnoringNull(AssertionInfo, Object, Object)</code>.
 *
 * @author Nicolas François
 * @author Joel Costigliola
 */
public class Objects_assertIsLenientEqualsToByIgnoringNullFields_Test extends ObjectsBaseTest {

  @Test
  public void should_pass_when_fields_are_equal() {
    Jedi actual = new Jedi("Yoda", "Green");
    Jedi other = new Jedi("Yoda", "Green");
    objects.assertIsLenientEqualsToByIgnoringNullFields(someInfo(), actual, other);
  }

  @Test
  public void should_pass_when_some_other_field_is_null_but_not_actual() {
    Jedi actual = new Jedi("Yoda", "Green");
    Jedi other = new Jedi("Yoda", null);
    objects.assertIsLenientEqualsToByIgnoringNullFields(someInfo(), actual, other);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    Jedi other = new Jedi("Yoda", "Green");
    objects.assertIsLenientEqualsToByIgnoringNullFields(someInfo(), null, other);
  }

  @Test
  public void should_fail_when_some_actual_field_is_null_but_not_other() {
    AssertionInfo info = someInfo();
    Jedi actual = new Jedi("Yoda", null);
    Jedi other = new Jedi("Yoda", "Green");
    try {
      objects.assertIsLenientEqualsToByIgnoringNullFields(info, actual, other);
    } catch (AssertionError err) {
      List<String> emptyList = newArrayList();
      verify(failures).failure(info,
          shouldBeLenientEqualByIgnoring(actual, newArrayList("lightSaberColor"), newArrayList((Object) "Green"), emptyList));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_when_a_field_differ() {
    AssertionInfo info = someInfo();
    Jedi actual = new Jedi("Yoda", "Green");
    Jedi other = new Jedi("Soda", "Green");
    try {
      objects.assertIsLenientEqualsToByIgnoringNullFields(info, actual, other);
    } catch (AssertionError err) {
      List<String> emptyList = newArrayList();
      verify(failures).failure(info,
          shouldBeLenientEqualByIgnoring(actual, newArrayList("name"), newArrayList((Object) "Soda"), emptyList));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_when_objects_to_compare_are_of_different_types() {
    AssertionInfo info = someInfo();
    Jedi actual = new Jedi("Yoda", "Green");
    Employee other = new Employee();
    try {
      objects.assertIsLenientEqualsToByIgnoringNullFields(info, actual, other);
    } catch (AssertionError err) {
      verify(failures).failure(info, shouldBeInstance(other, actual.getClass()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
