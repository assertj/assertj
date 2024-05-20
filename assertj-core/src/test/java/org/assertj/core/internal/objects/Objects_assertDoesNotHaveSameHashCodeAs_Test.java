/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.internal.objects;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotHaveSameHashCode.shouldNotHaveSameHashCode;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ObjectsBaseTest;
import org.assertj.core.testkit.Jedi;
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
    // GIVEN
    Object actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> objects.assertDoesNotHaveSameHashCodeAs(someInfo(), actual, "foo"));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_has_the_same_hash_code_as_other() {
    // GIVEN
    AssertionInfo info = someInfo();
    // WHEN
    // Jedi class hashCode is computed with the Jedi's name only
    expectAssertionError(() -> objects.assertDoesNotHaveSameHashCodeAs(info, greenYoda, greenYoda));
    // THEN
    verify(failures).failure(info, shouldNotHaveSameHashCode(greenYoda, greenYoda));
  }
}
