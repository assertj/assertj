/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.internal.objects;

import static org.assertj.core.error.ShouldBeInstanceOfAny.shouldBeInstanceOfAny;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import static org.mockito.Mockito.verify;

import java.io.File;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Objects;
import org.assertj.core.internal.ObjectsBaseTest;
import org.assertj.core.test.Person;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * Tests for <code>{@link Objects#assertIsInstanceOfAny(AssertionInfo, Object, Class[])}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Objects_assertIsInstanceOfAny_Test extends ObjectsBaseTest {

  private static Person actual;

  @BeforeClass
  public static void setUpOnce() {
    actual = new Person("Yoda");
  }

  @Test
  public void should_pass_if_actual_is_instance_of_any_type() {
    Class<?>[] types = { String.class, Person.class };
    objects.assertIsInstanceOfAny(someInfo(), actual, types);
  }

  @Test
  public void should_throw_error_if_array_of_types_is_null() {
    thrown.expectNullPointerException("The given array of types should not be null");
    objects.assertIsInstanceOfAny(someInfo(), actual, null);
  }

  @Test
  public void should_throw_error_if_array_of_types_is_empty() {
    thrown.expectIllegalArgumentException("The given array of types should not be empty");
    objects.assertIsInstanceOfAny(someInfo(), actual, new Class<?>[0]);
  }

  @Test
  public void should_throw_error_if_array_of_types_has_null_elements() {
    Class<?>[] types = { null, String.class };
    thrown.expectNullPointerException("The given array of types:<[null, java.lang.String]> should not have null elements");
    objects.assertIsInstanceOfAny(someInfo(), actual, types);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    Class<?>[] types = { Object.class };
    thrown.expectAssertionError(actualIsNull());
    objects.assertIsInstanceOfAny(someInfo(), null, types);
  }

  @Test
  public void should_fail_if_actual_is_not_instance_of_any_type() {
    AssertionInfo info = someInfo();
    Class<?>[] types = { String.class, File.class };
    try {
      objects.assertIsInstanceOfAny(info, actual, types);
      failBecauseExpectedAssertionErrorWasNotThrown();
    } catch (AssertionError err) {}
    verify(failures).failure(info, shouldBeInstanceOfAny(actual, types));
  }
}
