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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.internal.objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldNotHaveSameClass.shouldNotHaveSameClass;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Objects;
import org.assertj.core.internal.ObjectsBaseTest;
import org.assertj.core.test.Person;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Objects#assertDoesNotHaveSameClassAs(AssertionInfo, Object, Object)}</code>.
 * 
 * @author Nicolas François
 * @author Joel Costigliola
 */
class Objects_assertDoesNotHaveSameClassAs_Test extends ObjectsBaseTest {

  private static Person actual;

  @BeforeAll
  static void setUpOnce() {
    actual = new Person("Yoda");
  }

  @Test
  void should_pass_if_actual_does_not_have_not_same_class_as_other() {
    objects.assertDoesNotHaveSameClassAs(someInfo(), actual, "Luke");
  }

  @Test
  void should_throw_error_if_type_is_null() {
    assertThatNullPointerException().isThrownBy(() -> objects.assertDoesNotHaveSameClassAs(someInfo(), actual, null))
                                    .withMessage("The given object should not be null");
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> objects.assertDoesNotHaveSameClassAs(someInfo(), null,
                                                                                                          Object.class))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_has_same_type_as_other() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> objects.assertDoesNotHaveSameClassAs(info, actual, new Person("Luke")));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldNotHaveSameClass(actual, new Person("Luke")));
  }
}
