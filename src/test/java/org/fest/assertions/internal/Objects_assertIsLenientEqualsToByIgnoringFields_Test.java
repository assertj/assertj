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
package org.fest.assertions.internal;

import static org.fest.assertions.error.ShouldBeLenientEqual.shouldBeLenientEqual;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.util.Collections.list;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.error.ShouldBeInstance;
import org.fest.assertions.test.Employee;
import org.fest.assertions.test.Jedi;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>{@link Objects#assertIsLenientEqualsToByIgnoringFields(AssertionInfo, Object, Object, String...)</code>.
 *
 * @author Nicolas François
 */
public class Objects_assertIsLenientEqualsToByIgnoringFields_Test {

  private Failures failures;
  private Objects objects;

  @Before public void setUp() {
    failures = spy(new Failures());
    objects = new Objects();
    objects.failures = failures;
  }

  @Test public void should_pass_when_same_fields() {
    Jedi actual = new Jedi("Yoda", "Green");
    Jedi other = new Jedi("Yoda", "Green");
    objects.assertIsLenientEqualsToByIgnoringFields(someInfo(), actual, other);
  }
  
  @Test public void should_pass_when_different_field_is_not_accepted() {
	 Jedi actual = new Jedi("Yoda", "Green");
	 Jedi other = new Jedi("Yoda", "Blue");
	 objects.assertIsLenientEqualsToByIgnoringFields(someInfo(), actual, other, "lightsaberColor");	 
  }
  
  @Test public void should_pass_when_value_is_null() {
    Jedi actual = new Jedi("Yoda", null);
    Jedi other = new Jedi("Yoda", null);
    objects.assertIsLenientEqualsToByIgnoringFields(someInfo(), actual, other, "name");	
  } 
  
  @Test public void should_fail_when_a_field_is_not_same() {
    AssertionInfo info = someInfo();
    Jedi actual = new Jedi("Yoda", "Green");
    Jedi other = new Jedi("Yoda", "Blue");
    try {
      objects.assertIsLenientEqualsToByIgnoringFields(info, actual, other, "name");	
      fail();
    } catch (AssertionError err) {}
    verify(failures).failure(info, shouldBeLenientEqual(actual, list("lightsaberColor"), list((Object) "Blue"))); 
  } 

  @Test public void should_fail_when_different_type() {
    AssertionInfo info = someInfo();
    Jedi actual = new Jedi("Yoda", "Green");
    Employee other = new Employee();
    try {
      objects.assertIsLenientEqualsToByIgnoringFields(info, actual, other, "name");
      fail();
    } catch (AssertionError err) {}
    verify(failures).failure(info, ShouldBeInstance.shouldBeInstance(other, actual.getClass())); 
  }   
  
}
