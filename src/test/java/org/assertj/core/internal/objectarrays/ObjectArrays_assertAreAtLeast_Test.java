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
package org.assertj.core.internal.objectarrays;

import static org.assertj.core.error.ElementsShouldBeAtLeast.elementsShouldBeAtLeast;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.Arrays.array;


import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.Condition;
import org.assertj.core.internal.ObjectArrays;
import org.assertj.core.internal.ObjectArraysWithConditionBaseTest;
import org.junit.Test;


/**
 * Tests for <code>{@link ObjectArrays#assertAreAtLeast(AssertionInfo, Object[], int, Condition)}</code> .
 * 
 * @author Nicolas François
 * @author Mikhail Mazursky
 * @author Joel Costigliola
 */
public class ObjectArrays_assertAreAtLeast_Test extends ObjectArraysWithConditionBaseTest {

  @Test
  public void should_pass_if_satisfies_at_least_times_condition() {
    actual = array("Yoda", "Luke", "Leia");
    arrays.assertAreAtLeast(someInfo(), actual, 2, jedi);
    verify(conditions).assertIsNotNull(jedi);
  }

  @Test
  public void should_pass_if_all_satisfies_condition_() {
    actual = array("Yoda", "Luke", "Obiwan");
    arrays.assertAreAtLeast(someInfo(), actual, 2, jedi);
    verify(conditions).assertIsNotNull(jedi);
  }

  @Test
  public void should_throw_error_if_condition_is_null() {
    thrown.expectNullPointerException("The condition to evaluate should not be null");
    actual = array("Yoda", "Luke");
    arrays.assertAreAtLeast(someInfo(), actual, 2, null);
    verify(conditions).assertIsNotNull(null);
  }

  @Test
  public void should_fail_if_condition_is_not_met_enough() {
    testCondition.shouldMatch(false);
    AssertionInfo info = someInfo();
    try {
      actual = array("Yoda", "Solo", "Leia");
      arrays.assertAreAtLeast(someInfo(), actual, 2, jedi);
    } catch (AssertionError e) {
      verify(conditions).assertIsNotNull(jedi);
      verify(failures).failure(info, elementsShouldBeAtLeast(actual, 2, jedi));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
