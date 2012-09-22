/*
 * Created on Apr 8, 2012
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
 * Tests for <code>{@link Objects#assertIsLenientEqualsToByIgnoringFields(AssertionInfo, Object, Object, String...)</code>.
 *
 * @author Nicolas François
 * @author Joel Costigliola
 */
public class Objects_assertIsLenientEqualsToByIgnoringFields_Test extends ObjectsBaseTest {

  @Test
  public void should_pass_when_fields_are_equal() {
    Jedi actual = new Jedi("Yoda", "Green");
    Jedi other = new Jedi("Yoda", "Green");
    objects.assertIsLenientEqualsToByIgnoringFields(someInfo(), actual, other);
  }

  @Test
  public void should_pass_when_not_ignored_fields_are_equal() {
    Jedi actual = new Jedi("Yoda", "Green");
    Jedi other = new Jedi("Yoda", "Blue");
    objects.assertIsLenientEqualsToByIgnoringFields(someInfo(), actual, other, "lightSaberColor");
  }

  @Test
  public void should_pass_when_not_ignored_inherited_fields_are_equal() {
    Jedi actual = new Jedi("Yoda", "Green");
    Jedi other = new Jedi("Luke", "Green");
    objects.assertIsLenientEqualsToByIgnoringFields(someInfo(), actual, other, "name");
  }

  @Test
  public void should_pass_when_field_values_are_null() {
    Jedi actual = new Jedi("Yoda", null);
    Jedi other = new Jedi("Yoda", null);
    objects.assertIsLenientEqualsToByIgnoringFields(someInfo(), actual, other, "name");
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    Jedi other = new Jedi("Yoda", "Green");
    objects.assertIsLenientEqualsToByIgnoringFields(someInfo(), null, other, "name");
  }

  @Test
  public void should_fail_when_some_field_values_differ() {
    AssertionInfo info = someInfo();
    Jedi actual = new Jedi("Yoda", "Green");
    Jedi other = new Jedi("Yoda", "Blue");
    try {
      objects.assertIsLenientEqualsToByIgnoringFields(info, actual, other, "name");
    } catch (AssertionError err) {
      verify(failures).failure(
          info,
          shouldBeLenientEqualByIgnoring(actual, newArrayList("lightSaberColor"), newArrayList((Object) "Blue"),
              newArrayList("name")));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_when_some_inherited_field_values_differ() {
    AssertionInfo info = someInfo();
    Jedi actual = new Jedi("Yoda", "Green");
    Jedi other = new Jedi("Luke", "Green");
    try {
      objects.assertIsLenientEqualsToByIgnoringFields(info, actual, other, "lightSaberColor");
    } catch (AssertionError err) {
      verify(failures).failure(
          info,
          shouldBeLenientEqualByIgnoring(actual, newArrayList("name"), newArrayList((Object) "Luke"),
              newArrayList("lightSaberColor")));
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
      objects.assertIsLenientEqualsToByIgnoringFields(info, actual, other, "name");
    } catch (AssertionError err) {
      verify(failures).failure(info, shouldBeInstance(other, actual.getClass()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_when_some_field_value_is_null_on_one_object_only() {
    AssertionInfo info = someInfo();
    Jedi actual = new Jedi("Yoda", null);
    Jedi other = new Jedi("Yoda", "Green");
    try {
      objects.assertIsLenientEqualsToByIgnoringFields(info, actual, other, "name");
    } catch (AssertionError err) {
      List<Object> expected = newArrayList((Object) "Green");
      verify(failures).failure(info,
          shouldBeLenientEqualByIgnoring(actual, newArrayList("lightSaberColor"), expected, newArrayList("name")));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
