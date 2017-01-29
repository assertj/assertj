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

import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.assertj.core.error.ShouldNotBeInstance.shouldNotBeInstance;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Objects;
import org.assertj.core.internal.ObjectsBaseTest;
import org.assertj.core.test.Person;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * Tests for <code>{@link Objects#assertIsNotInstanceOf(AssertionInfo, Object, Class)}</code>.
 * 
 * @author Nicolas François
 * @author Joel Costigliola
 */
public class Objects_assertIsNotInstanceOf_Test extends ObjectsBaseTest {

  private static Person actual;

  @BeforeClass
  public static void setUpOnce() {
    actual = new Person("Yoda");
  }

  @Test
  public void should_pass_if_actual_is_not_instance_of_type() {
    objects.assertIsNotInstanceOf(someInfo(), actual, String.class);
  }

  @Test
  public void should_throw_error_if_type_is_null() {
    thrown.expectNullPointerException("The given type should not be null");
    objects.assertIsNotInstanceOf(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    objects.assertIsNotInstanceOf(someInfo(), null, Object.class);
  }

  @Test
  public void should_fail_if_actual_is_instance_of_type() {
    AssertionInfo info = someInfo();
    try {
      objects.assertIsNotInstanceOf(info, actual, Person.class);
      failBecauseExceptionWasNotThrown(AssertionError.class);
    } catch (AssertionError err) {}
    verify(failures).failure(info, shouldNotBeInstance(actual, Person.class));
  }
}
