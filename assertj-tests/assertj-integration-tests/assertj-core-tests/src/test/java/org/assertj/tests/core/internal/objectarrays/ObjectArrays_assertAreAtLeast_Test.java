/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.internal.objectarrays;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.error.ElementsShouldBeAtLeast.elementsShouldBeAtLeast;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

/**
 * @author Nicolas François
 * @author Mikhail Mazursky
 * @author Joel Costigliola
 */
class ObjectArrays_assertAreAtLeast_Test extends ObjectArraysWithConditionBaseTest {

  @Test
  void should_pass_if_satisfies_at_least_times_condition() {
    arrays.assertAreAtLeast(INFO, array("Yoda", "Luke", "Leia"), 2, jedi);
  }

  @Test
  void should_pass_if_all_satisfies_condition_() {
    arrays.assertAreAtLeast(INFO, array("Yoda", "Luke", "Obiwan"), 2, jedi);
  }

  @Test
  void should_throw_error_if_condition_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertAreAtLeast(INFO, array("Yoda", "Luke"), 2, null))
                                    .withMessage("The condition to evaluate should not be null");
  }

  @Test
  void should_fail_if_condition_is_not_met() {
    // GIVEN
    var actual = array("Yoda", "Solo", "Leia");
    // WHEN
    expectAssertionError(() -> arrays.assertAreAtLeast(INFO, actual, 2, jedi));
    // THEN
    verify(failures).failure(INFO, elementsShouldBeAtLeast(actual, 2, jedi));
  }

}
