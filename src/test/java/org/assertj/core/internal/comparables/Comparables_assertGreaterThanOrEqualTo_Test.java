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
package org.assertj.core.internal.comparables;

import static org.assertj.core.error.ShouldBeGreaterOrEqual.shouldBeGreaterOrEqual;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;


import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Comparables;
import org.assertj.core.internal.ComparablesBaseTest;
import org.junit.Test;


/**
 * Tests for <code>{@link Comparables#assertGreaterThanOrEqualTo(AssertionInfo, Comparable, Comparable)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Comparables_assertGreaterThanOrEqualTo_Test extends ComparablesBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    comparables.assertGreaterThanOrEqualTo(someInfo(), null, 8);
  }

  @Test
  public void should_pass_if_actual_is_greater_than_other() {
    comparables.assertGreaterThanOrEqualTo(someInfo(), 8, 6);
  }

  @Test
  public void should_pass_if_actual_is_equal_to_other() {
    comparables.assertGreaterThanOrEqualTo(someInfo(), "Yoda", "Yoda");
  }

  @Test
  public void should_fail_if_actual_is_less_than_other() {
    AssertionInfo info = someInfo();
    try {
      comparables.assertGreaterThanOrEqualTo(info, 6, 8);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeGreaterOrEqual(6, 8));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  public void should_pass_if_actual_is_greater_than_other_according_to_custom_comparison_strategy() {
    comparablesWithCustomComparisonStrategy.assertGreaterThanOrEqualTo(someInfo(), 8, -6);
  }

  @Test
  public void should_pass_if_actual_is_equal_to_other_according_to_custom_comparison_strategy() {
    comparablesWithCustomComparisonStrategy.assertGreaterThanOrEqualTo(someInfo(), -8, 8);
  }

  @Test
  public void should_fail_if_actual_is_less_than_other_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    try {
      comparablesWithCustomComparisonStrategy.assertGreaterThanOrEqualTo(info, 6, -8);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeGreaterOrEqual(6, -8, customComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
