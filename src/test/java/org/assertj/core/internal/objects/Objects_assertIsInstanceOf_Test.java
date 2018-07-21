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
import static org.assertj.core.error.ShouldBeInstance.shouldBeInstance;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;


import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Objects;
import org.assertj.core.internal.ObjectsBaseTest;
import org.assertj.core.test.Person;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


/**
 * Tests for <code>{@link Objects#assertIsInstanceOf(AssertionInfo, Object, Class)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Objects_assertIsInstanceOf_Test extends ObjectsBaseTest {

  private static Person actual;

  @BeforeAll
  public static void setUpOnce() {
    actual = new Person("Yoda");
  }

  @Test
  public void should_pass_if_actual_is_instance_of_type() {
    objects.assertIsInstanceOf(someInfo(), actual, Person.class);
  }

  @Test
  public void should_throw_error_if_type_is_null() {
    assertThatNullPointerException().isThrownBy(() -> objects.assertIsInstanceOf(someInfo(), actual, null))
                                    .withMessage("The given type should not be null");
  }

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> objects.assertIsInstanceOf(someInfo(), null, Object.class))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_actual_is_not_instance_of_type() {
    AssertionInfo info = someInfo();
    try {
      objects.assertIsInstanceOf(info, actual, String.class);
    } catch (AssertionError err) {
      verify(failures).failure(info, shouldBeInstance(actual, String.class));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
