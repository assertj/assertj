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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.internal.objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldNotHaveSameHashCode.shouldNotHaveSameHashCode;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ObjectsBaseTest;
import org.assertj.core.test.Jedi;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class Objects_assertDoesNotHaveSameHashCodeAs_Test extends ObjectsBaseTest {

  private static Jedi greenYoda;

  @BeforeAll
  static void setUpOnce() {
    greenYoda = new Jedi("Yoda", "green");
  }

  @Test
  void should_pass_if_actual_does_not_have_the_same_hash_code_as_other() {
    // GIVEN
    // Jedi class hashCode is computed with the Jedi's name only
    Jedi luke = new Jedi("Luke", "Red");
    // THEN
    objects.assertDoesNotHaveSameHashCodeAs(someInfo(), greenYoda, luke);
    objects.assertDoesNotHaveSameHashCodeAs(someInfo(), luke, greenYoda);
    objects.assertDoesNotHaveSameHashCodeAs(someInfo(), greenYoda, new Jedi("Luke", "green"));
    objects.assertDoesNotHaveSameHashCodeAs(someInfo(), new Jedi("Luke", "green"), greenYoda);
  }

  @Test
  void should_throw_error_if_other_is_null() {
    assertThatNullPointerException().isThrownBy(() -> objects.assertDoesNotHaveSameHashCodeAs(someInfo(), greenYoda, null))
                                    .withMessage("The object used to compare actual's hash code with should not be null");
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> objects.assertDoesNotHaveSameHashCodeAs(someInfo(), null, greenYoda))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_has_the_same_hash_code_as_other() {
    // GIVEN
    AssertionInfo info = someInfo();
    // WHEN
    // Jedi class hashCode is computed with the Jedi's name only
    Throwable error = catchThrowable(() -> objects.assertDoesNotHaveSameHashCodeAs(info, greenYoda, greenYoda));
    // THEN
    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldNotHaveSameHashCode(greenYoda, greenYoda));
  }
}
