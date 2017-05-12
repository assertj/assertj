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
package org.assertj.core.internal.doubles;

import static org.assertj.core.error.ShouldBeGreaterOrEqual.shouldBeGreaterOrEqual;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Doubles;
import org.assertj.core.internal.DoublesBaseTest;
import org.junit.Test;


/**
 * Tests for <code>{@link Doubles#assertGreaterThanOrEqualTo(AssertionInfo, Double, double)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Doubles_assertGreaterThanOrEqualTo_Test extends DoublesBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    doubles.assertGreaterThanOrEqualTo(someInfo(), null, 8d);
  }

  @Test
  public void should_pass_if_actual_is_greater_than_other() {
    doubles.assertGreaterThanOrEqualTo(someInfo(), 8d, 6d);
  }

  @Test
  public void should_pass_if_actual_is_equal_to_other() {
    doubles.assertGreaterThanOrEqualTo(someInfo(), 6d, 6d);
  }

  @Test
  public void should_fail_if_actual_is_less_than_other() {
    AssertionInfo info = someInfo();
    try {
      doubles.assertGreaterThanOrEqualTo(info, 6d, 8d);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeGreaterOrEqual(6d, 8d));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    doublesWithAbsValueComparisonStrategy.assertGreaterThanOrEqualTo(someInfo(), null, 8d);
  }

  @Test
  public void should_pass_if_actual_is_greater_than_other_according_to_custom_comparison_strategy() {
    doublesWithAbsValueComparisonStrategy.assertGreaterThanOrEqualTo(someInfo(), -8d, 6d);
  }

  @Test
  public void should_pass_if_actual_is_equal_to_other_according_to_custom_comparison_strategy() {
    doublesWithAbsValueComparisonStrategy.assertGreaterThanOrEqualTo(someInfo(), -6d, 6d);
  }

  @Test
  public void should_fail_if_actual_is_less_than_other_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    try {
      doublesWithAbsValueComparisonStrategy.assertGreaterThanOrEqualTo(info, -6d, 8d);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeGreaterOrEqual(-6d, 8d, absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
