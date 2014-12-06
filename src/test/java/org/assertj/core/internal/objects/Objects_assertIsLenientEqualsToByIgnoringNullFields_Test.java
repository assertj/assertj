/*
 * Created on Apr 8, 2012
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2012 the original author or authors.
 */
package org.assertj.core.internal.objects;

import static org.assertj.core.error.ShouldBeInstance.shouldBeInstance;
import static org.assertj.core.error.ShouldBeEqualToIgnoringFields.shouldBeEqualToIgnoringGivenFields;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.Assertions;
import org.assertj.core.internal.ObjectsBaseTest;
import org.assertj.core.test.Employee;
import org.assertj.core.test.Jedi;
import org.assertj.core.test.TestClassWithRandomId;
import org.assertj.core.util.introspection.FieldSupport;
import org.junit.Test;

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
	objects.assertIsLenientEqualsToIgnoringNullFields(someInfo(), actual, other);
  }

  @Test
  public void should_pass_when_some_other_field_is_null_but_not_actual() {
	Jedi actual = new Jedi("Yoda", "Green");
	Jedi other = new Jedi("Yoda", null);
	objects.assertIsLenientEqualsToIgnoringNullFields(someInfo(), actual, other);
  }

  @Test
  public void should_fail_if_actual_is_null() {
	thrown.expectAssertionError(actualIsNull());
	Jedi other = new Jedi("Yoda", "Green");
	objects.assertIsLenientEqualsToIgnoringNullFields(someInfo(), null, other);
  }

  @Test
  public void should_fail_when_some_actual_field_is_null_but_not_other() {
	AssertionInfo info = someInfo();
	Jedi actual = new Jedi("Yoda", null);
	Jedi other = new Jedi("Yoda", "Green");
	try {
	  objects.assertIsLenientEqualsToIgnoringNullFields(info, actual, other);
	} catch (AssertionError err) {
	  verify(failures).failure(info,
		                       shouldBeEqualToIgnoringGivenFields(actual, newArrayList("lightSaberColor"),
		                                                          newArrayList((Object) null),
		                                                          newArrayList((Object) "Green"),
		                                                          newArrayList("strangeNotReadablePrivateField")));
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
	  objects.assertIsLenientEqualsToIgnoringNullFields(info, actual, other);
	} catch (AssertionError err) {
	  verify(failures).failure(info,
		                       shouldBeEqualToIgnoringGivenFields(actual, newArrayList("name"),
		                                                          newArrayList((Object) "Yoda"),
		                                                          newArrayList((Object) "Soda"),
		                                                          newArrayList("strangeNotReadablePrivateField")));
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
	  objects.assertIsLenientEqualsToIgnoringNullFields(info, actual, other);
	} catch (AssertionError err) {
	  verify(failures).failure(info, shouldBeInstance(other, actual.getClass()));
	  return;
	}
	failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_when_private_fields_differ_but_are_not_compared() {
	boolean allowedToUsePrivateFields = FieldSupport.comparison().isAllowedToUsePrivateFields();
    Assertions.setAllowComparingPrivateFields(false);
	TestClassWithRandomId actual = new TestClassWithRandomId("1", 1);
	TestClassWithRandomId other = new TestClassWithRandomId(null, 1);
	// s field is ignored because null in other, and id also because it is private without public getter
	objects.assertIsLenientEqualsToIgnoringNullFields(someInfo(), actual, other);
	// reset
	Assertions.setAllowComparingPrivateFields(allowedToUsePrivateFields);
  }

}
