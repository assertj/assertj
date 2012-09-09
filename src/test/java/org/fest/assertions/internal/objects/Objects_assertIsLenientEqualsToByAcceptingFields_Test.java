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

import static junit.framework.Assert.assertEquals;

import static org.fest.assertions.error.ShouldBeInstance.shouldBeInstance;
import static org.fest.assertions.error.ShouldBeLenientEqualByAccepting.shouldBeLenientEqualByAccepting;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.util.Lists.newArrayList;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.ObjectsBaseTest;
import org.fest.test.Employee;
import org.fest.test.Jedi;
import org.fest.util.IntrospectionError;

/**
 * Tests for <code>{@link Objects#assertIsLenientEqualsToByAcceptingFields(AssertionInfo, Object, Object, String...)</code>.
 *
 * @author Nicolas François
 * @author Joel Costigliola
 */
public class Objects_assertIsLenientEqualsToByAcceptingFields_Test extends ObjectsBaseTest {

  @Test
  public void should_pass_when_selected_fields_are_equal() {
    Jedi actual = new Jedi("Yoda", "Green");
    Jedi other = new Jedi("Yoda", "Green");
    objects.assertIsLenientEqualsToByAcceptingFields(someInfo(), actual, other, "name", "lightSaberColor");
  }

  @Test
  public void should_pass_even_if_non_accepted_fields_differ() {
    Jedi actual = new Jedi("Yoda", "Green");
    Jedi other = new Jedi("Yoda", "Blue");
    objects.assertIsLenientEqualsToByAcceptingFields(someInfo(), actual, other, "name");
  }

  @Test
  public void should_pass_when_field_value_is_null() {
    Jedi actual = new Jedi("Yoda", null);
    Jedi other = new Jedi("Yoda", null);
    objects.assertIsLenientEqualsToByAcceptingFields(someInfo(), actual, other, "name", "lightSaberColor");
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    Jedi other = new Jedi("Yoda", "Green");
    objects.assertIsLenientEqualsToByAcceptingFields(someInfo(), null, other, "name", "lightSaberColor");
  }

  @Test
  public void should_fail_when_some_selected_field_values_differ() {
    AssertionInfo info = someInfo();
    Jedi actual = new Jedi("Yoda", "Green");
    Jedi other = new Jedi("Yoda", "Blue");
    try {
      objects.assertIsLenientEqualsToByAcceptingFields(info, actual, other, "name", "lightSaberColor");
    } catch (AssertionError err) {
      List<Object> expected = newArrayList((Object) "Blue");
      verify(failures).failure(
          info,
          shouldBeLenientEqualByAccepting(actual, newArrayList("lightSaberColor"), expected,
              newArrayList("name", "lightSaberColor")));
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
      objects.assertIsLenientEqualsToByAcceptingFields(info, actual, other, "name", "lightSaberColor");
    } catch (AssertionError err) {
      List<Object> expected = newArrayList((Object) "Luke");
      verify(failures).failure(
          info,
          shouldBeLenientEqualByAccepting(actual, newArrayList("name"), expected,
              newArrayList("name", "lightSaberColor")));
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
      objects.assertIsLenientEqualsToByAcceptingFields(info, actual, other, "name");
    } catch (AssertionError err) {
      verify(failures).failure(info, shouldBeInstance(other, actual.getClass()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_when_selected_field_does_not_exist() {
    AssertionInfo info = someInfo();
    Jedi actual = new Jedi("Yoda", "Green");
    Jedi other = new Jedi("Yoda", "Blue");
    try {
      objects.assertIsLenientEqualsToByAcceptingFields(info, actual, other, "age");
    } catch (IntrospectionError expected) {
      String msg = String.format("No getter for property '%s' in %s", "age", actual.getClass().getName());
      assertEquals(msg, expected.getMessage());
      return;
    }
    fail("expecting an IntrospectionError to be thrown");
  }

}
