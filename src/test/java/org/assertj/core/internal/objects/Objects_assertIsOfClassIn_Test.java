/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.internal.objects;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.error.ShouldBeOfClassIn.shouldBeOfClassIn;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.io.File;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Objects;
import org.assertj.core.internal.ObjectsBaseTest;
import org.assertj.core.test.Person;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


/**
 * Tests for <code>{@link Objects#assertIsOfAnyClassIn(AssertionInfo, Object, Class[])}</code>.
 * 
 * @author Nicolas François
 * @author Joel Costigliola
 */
public class Objects_assertIsOfClassIn_Test extends ObjectsBaseTest {

  private static Person actual;

  @BeforeAll
  public static void setUpOnce() {
    actual = new Person("Yoda");
  }

  @Test
  public void should_pass_if_actual_is_of_class_in_types() {
    Class<?>[] types = new Class[] { File.class, Person.class, String.class };
    objects.assertIsOfAnyClassIn(someInfo(), actual, types);
  }

  @Test
  public void should_throw_error_if_type_is_null() {
    assertThatNullPointerException().isThrownBy(() -> objects.assertIsOfAnyClassIn(someInfo(), actual, null))
                                    .withMessage("The given types should not be null");
  }

  @Test
  public void should_fail_if_actual_is_null() {
    Class<?>[] types = new Class[] { File.class, Person.class, String.class };
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> objects.assertIsOfAnyClassIn(someInfo(), null, types))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_actual_is_not_of_class_in_types() {
    AssertionInfo info = someInfo();
    Class<?>[] types = new Class[] { File.class, String.class };
    try {
      objects.assertIsOfAnyClassIn(info, actual, types);
      failBecauseExpectedAssertionErrorWasNotThrown();
    } catch (AssertionError err) {
      verify(failures).failure(info, shouldBeOfClassIn(actual, types));
    }
  }

  @Test
  public void should_fail_if_actual_is_not_of_class_in_empty_types() {
    AssertionInfo info = someInfo();
    Class<?>[] types = new Class[] {};
    try {
      objects.assertIsOfAnyClassIn(info, actual, types);
      failBecauseExpectedAssertionErrorWasNotThrown();
    } catch (AssertionError err) {
      verify(failures).failure(info, shouldBeOfClassIn(actual, types));
    }
  }
}
