/*
 * Created on Jun 3, 2012
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

import static org.fest.assertions.error.ShouldNotBeInstanceOfAny.shouldNotBeInstanceOfAny;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.assertions.test.TestData.someInfo;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.Objects;
import org.fest.assertions.internal.ObjectsBaseTest;
import org.fest.test.Person;

/**
 * Tests for <code>{@link Objects#assertIsNotInstanceOfAny(AssertionInfo, Object, Class[])}</code>.
 * 
 * @author Nicolas François
 * @author Joel Costigliola
 */
public class Objects_assertIsNotInstanceOfAny_Test extends ObjectsBaseTest {

  private static Person actual;

  @BeforeClass
  public static void setUpOnce() {
    actual = new Person("Yoda");
  }

  @Test
  public void should_pass_if_actual_is_not_instance_of_any_type() {
    Class<?>[] types = { String.class, File.class };
    objects.assertIsNotInstanceOfAny(someInfo(), actual, types);
  }

  @Test
  public void should_throw_error_if_array_of_types_is_null() {
    thrown.expectNullPointerException("The given array of types should not be null");
    objects.assertIsNotInstanceOfAny(someInfo(), actual, null);
  }

  @Test
  public void should_throw_error_if_array_of_types_is_empty() {
    thrown.expectIllegalArgumentException("The given array of types should not be empty");
    objects.assertIsNotInstanceOfAny(someInfo(), actual, new Class<?>[0]);
  }

  @Test
  public void should_throw_error_if_array_of_types_has_null_elements() {
    Class<?>[] types = { null, String.class };
    thrown.expectNullPointerException("The given array of types:<[null, java.lang.String]> should not have null elements");
    objects.assertIsNotInstanceOfAny(someInfo(), actual, types);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    Class<?>[] types = { Object.class };
    thrown.expectAssertionError(actualIsNull());
    objects.assertIsNotInstanceOfAny(someInfo(), null, types);
  }

  @Test
  public void should_fail_if_actual_is_instance_of_any_type() {
    AssertionInfo info = someInfo();
    Class<?>[] types = { String.class, Person.class };
    try {
      objects.assertIsNotInstanceOfAny(info, actual, types);
      fail();
    } catch (AssertionError err) {}
    verify(failures).failure(info, shouldNotBeInstanceOfAny(actual, types));
  }
}
