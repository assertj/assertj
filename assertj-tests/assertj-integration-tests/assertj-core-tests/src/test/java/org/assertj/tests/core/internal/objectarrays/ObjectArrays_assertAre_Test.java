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
import static org.assertj.core.error.ElementsShouldBe.elementsShouldBe;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

/**
 * @author Nicolas François
 * @author Mikhail Mazursky
 * @author Joel Costigliola
 */
class ObjectArrays_assertAre_Test extends ObjectArraysWithConditionBaseTest {

  @Test
  void should_pass_if_each_element_satisfies_condition() {
    arrays.assertAre(INFO, array("Yoda", "Luke"), jedi);
  }

  @Test
  void should_throw_error_if_condition_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertAre(INFO, actual, null))
                                    .withMessage("The condition to evaluate should not be null");
  }

  @Test
  void should_fail_if_Condition_is_not_met() {
    // GIVEN
    var actual = array("Yoda", "Luke", "Leia");
    // WHEN
    expectAssertionError(() -> arrays.assertAre(INFO, actual, jedi));
    // THEN
    verify(failures).failure(INFO, elementsShouldBe(actual, newArrayList("Leia"), jedi));
  }

}
