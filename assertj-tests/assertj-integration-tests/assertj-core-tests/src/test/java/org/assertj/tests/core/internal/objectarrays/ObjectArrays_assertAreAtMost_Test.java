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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.tests.core.internal.objectarrays;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.error.ElementsShouldBeAtMost.elementsShouldBeAtMost;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

/**
 * @author Nicolas François
 * @author Mikhail Mazursky
 * @author Joel Costigliola
 */
class ObjectArrays_assertAreAtMost_Test extends ObjectArraysWithConditionBaseTest {

  @Test
  void should_pass_if_satisfies_at_least_times_condition() {
    arrays.assertAreAtMost(INFO, array("Yoda", "Luke", "Leia"), 2, jedi);
  }

  @Test
  void should_pass_if_all_satisfies_condition() {
    arrays.assertAreAtMost(INFO, array("Chewbacca", "Leia", "Obiwan"), 2, jedi);
  }

  @Test
  void should_throw_error_if_condition_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertAreAtMost(INFO, array("Yoda", "Luke"), 2, null))
                                    .withMessage("The condition to evaluate should not be null");
  }

  @Test
  void should_fail_if_condition_is_not_met_enough() {
    // GIVEN
    var actual = array("Yoda", "Luke", "Obiwan");
    // WHEN
    expectAssertionError(() -> arrays.assertAreAtMost(INFO, actual, 2, jedi));
    // THEN
    verify(failures).failure(INFO, elementsShouldBeAtMost(actual, 2, jedi));
  }

}
