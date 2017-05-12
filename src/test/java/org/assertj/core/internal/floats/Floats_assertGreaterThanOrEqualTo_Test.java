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
package org.assertj.core.internal.floats;

import static org.assertj.core.error.ShouldBeGreaterOrEqual.shouldBeGreaterOrEqual;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Floats;
import org.assertj.core.internal.FloatsBaseTest;
import org.junit.Test;


/**
 * Tests for <code>{@link Floats#assertGreaterThanOrEqualTo(AssertionInfo, Float, float)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Floats_assertGreaterThanOrEqualTo_Test extends FloatsBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    floats.assertGreaterThanOrEqualTo(someInfo(), null, 8f);
  }

  @Test
  public void should_pass_if_actual_is_greater_than_other() {
    floats.assertGreaterThanOrEqualTo(someInfo(), 8f, 6f);
  }

  @Test
  public void should_pass_if_actual_is_equal_to_other() {
    floats.assertGreaterThanOrEqualTo(someInfo(), 6f, 6f);
  }

  @Test
  public void should_fail_if_actual_is_less_than_other() {
    AssertionInfo info = someInfo();
    try {
      floats.assertGreaterThanOrEqualTo(info, 6f, 8f);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeGreaterOrEqual(6f, 8f));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    floatsWithAbsValueComparisonStrategy.assertGreaterThanOrEqualTo(someInfo(), null, 8f);
  }

  @Test
  public void should_pass_if_actual_is_greater_than_other_according_to_custom_comparison_strategy() {
    floatsWithAbsValueComparisonStrategy.assertGreaterThanOrEqualTo(someInfo(), -8f, 6f);
    floatsWithAbsValueComparisonStrategy.assertGreaterThanOrEqualTo(someInfo(), 8f, 6f);
  }

  @Test
  public void should_pass_if_actual_is_equal_to_other_according_to_custom_comparison_strategy() {
    floatsWithAbsValueComparisonStrategy.assertGreaterThanOrEqualTo(someInfo(), -6f, 6f);
    floatsWithAbsValueComparisonStrategy.assertGreaterThanOrEqualTo(someInfo(), 6f, 6f);
  }

  @Test
  public void should_fail_if_actual_is_less_than_other_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    try {
      floatsWithAbsValueComparisonStrategy.assertGreaterThanOrEqualTo(info, 6f, -8f);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeGreaterOrEqual(6f, -8f, absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
