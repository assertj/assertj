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

import static org.assertj.core.error.ElementsShouldNotBe.elementsShouldNotBe;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.newArrayList;


import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ObjectArrays;
import org.assertj.core.internal.ObjectArraysWithConditionBaseTest;
import org.junit.Test;


/**
 * Tests for
 * <code>{@link ObjectArrays#areNot(org.assertj.core.api.AssertionInfo, Object[], org.assertj.core.core.Condition)}</code>
 * .
 * 
 * @author Nicolas François
 * @author Mikhail Mazursky
 * @author Joel Costigliola
 */
public class ObjectArrays_assertAreNot_Test extends ObjectArraysWithConditionBaseTest {

  @Test
  public void should_pass_if_each_element_satisfies_condition() {
    actual = array("Solo", "Leia");
    arrays.assertAreNot(someInfo(), actual, jedi);
    verify(conditions).assertIsNotNull(jedi);
  }

  @Test
  public void should_throw_error_if_condition_is_null() {
    thrown.expectNullPointerException("The condition to evaluate should not be null");
    arrays.assertAreNot(someInfo(), actual, null);
    verify(conditions).assertIsNotNull(null);
  }

  @Test
  public void should_fail_if_condition_is_met() {
    testCondition.shouldMatch(false);
    AssertionInfo info = someInfo();
    try {
      actual = array("Solo", "Leia", "Yoda");
      arrays.assertAreNot(someInfo(), actual, jedi);
    } catch (AssertionError e) {
      verify(conditions).assertIsNotNull(jedi);
      verify(failures).failure(info, elementsShouldNotBe(actual, newArrayList("Yoda"), jedi));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
